package org.semanticweb.yars.nx.sort;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeArrayComparator;
import org.semanticweb.yars.nx.mem.LowMemorySniffer;
import org.semanticweb.yars.nx.mem.MemoryManager;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.nx.sort.MergeSortIterator.MergeSortArgs;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;
import org.semanticweb.yars.util.FlyweightNodeIterator;
import org.semanticweb.yars.util.PleaseCloseTheDoorWhenYouLeaveIterator;
import org.semanticweb.yars.util.SniffNodeArrayLengthIterator;


public class SortIterator implements Iterator<Node[]>, Iterable<Node[]>{
	public static final String PREFIX="batch";
	public static final String SUFFIX=".nq.gz";
	
	private static final int INTERNAL_BATCH = 5000;
	private static final double KILL_SLOW_RATIO = 4;
	private static final int IN_A_ROW = 3;
	
	static transient Logger _log = Logger.getLogger(SortIterator.class.getName());

	private MergeSortIterator _merge = null;
	private Iterator<Node[]> _single = null;
	
	private long _dupes;
	private long _count = 0;
	
	public SortIterator(Iterator<Node[]> in) throws IOException, ParseException{
		this(new SortArgs(in));
	}
	
	public SortIterator(Iterator<Node[]> in, short nxlength) throws IOException, ParseException{
		this(new SortArgs(in, nxlength));
	}
	
	public SortIterator(SortArgs args) throws IOException, ParseException{
		sort(args);
	}
	
	/**
	 * Returns a sorted iterator over input
	 * @param args
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private void sort(SortArgs args) throws IOException, ParseException {
		long count=0;
		int i = 0;
		long dupes = 0;
		
		LowMemorySniffer lms = null;
		if(args._linesPerBatch==SortArgs.ADAPTIVE_BATCHES){
			lms = new LowMemorySniffer();
		}

		if(lms==null)
			_log.info("Using batches with "+args._linesPerBatch+" statements each.");
		else
			_log.info("Using adaptive batches.");
		
		Iterator<Node[]> in = args._in;
		if(args._fw>0){
			in = new FlyweightNodeIterator(args._fw, in);
		}
		
		List<Path> tempFiles = new LinkedList<Path>();
		
		// generated sorted batches
		Iterator<Node[]> one = null;
		while (in.hasNext()) {
			//treeset is slower than Collections.sort() for once-off
			//sorting, but doesn't require a memory-heavy array copy
			TreeSet<Node[]> sset = new TreeSet<Node[]>(args._nc);

			boolean notdupe;
			long b4 = System.currentTimeMillis();
			long fastest = Long.MAX_VALUE;
			int in_a_row = 0;
			
			while (in.hasNext() &&
					((lms!=null && !lms.lowMemory()) ||
					(lms==null &&  sset.size() < args._linesPerBatch))){
				Node[] quad = in.next();

				notdupe = sset.add(quad);
				if(!notdupe)
					dupes++;
				count++;
				
				if(args._ticks>0 && count%args._ticks==0){
					_log.info("Batched "+count+" in "+i+" files with "+dupes+" duplicates.");
				}
				
				//check if we get a number of slow batches in a row
				//if so, we might be hitting heap space problems
				if(lms!=null && count%INTERNAL_BATCH == 0){
					long t = System.currentTimeMillis()-b4;
					b4 = System.currentTimeMillis();
					if(t<fastest){
						fastest = t;
						in_a_row = 0;
					} else if(fastest*KILL_SLOW_RATIO < t){
						in_a_row++;
						if(in_a_row>=IN_A_ROW){
							_log.info("Slow mini-batch #"+in_a_row+" "+t+" vs. "+fastest+" fastest... batch size: "+sset.size());
							break;
						}
					} else{
						in_a_row = 0;
					}
				}
			}

			Iterator<Node[]> it = sset.iterator();

			if(!args._in.hasNext() && i == 0){
				one = it;
				break;
			}
			
			Path temp = Files.createTempFile(args._tmpDir, PREFIX + i , SUFFIX);
			tempFiles.add(temp);
			temp.toFile().deleteOnExit();
			
			OutputStream os = Files.newOutputStream(temp);
			if(args._gzipBatch)
				os = new GZIPOutputStream(os);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
			Callback cbBatch = new CallbackNxBufferedWriter(bw); 
			i++;

			_log.info("Dumping batch size: "+sset.size());
			cbBatch.startDocument();
			while (it.hasNext()) {
				Node[] quad = it.next();
				//System.out.println(quad[0].toN3()+" "+quad[1].toN3()+" "+quad[2].toN3()+" "+quad[3].toN3()+" .");
				cbBatch.processStatement(quad);
			}
			cbBatch.endDocument();
			bw.close();
			os.close();
			_log.info("Parsed and sorted "+count+" lines in "+i+" files with "+dupes+" duplicates.");
		}

		if(i>0){
			_log.info("Merging "+i+" segment files.");
			
			List<Iterator<Node[]>> tempFileIterators = new ArrayList<Iterator<Node[]>>();
			for(Path tempFilePath : tempFiles){
				InputStream is = Files.newInputStream(tempFilePath, StandardOpenOption.READ);
				
				if(args._gzipBatch){
					is = new GZIPInputStream(is, 8192);
				}
				
				NxParser nxp = new NxParser();
				nxp.parse(is, StandardCharsets.UTF_8);

				PleaseCloseTheDoorWhenYouLeaveIterator<Node[]> it = new PleaseCloseTheDoorWhenYouLeaveIterator<Node[]>(
						nxp, is);

				tempFileIterators.add(it);
			}

			@SuppressWarnings("unchecked")
			MergeSortArgs msa = new MergeSortArgs(tempFileIterators.toArray(new Iterator[0]));
			msa.setComparator(args._nc);
			msa.setDuplicates(dupes);
//			msa.setLinesPerBatch(_linesPerBatch/segments.length);
			msa.setTicks(args._ticks);
			
			_merge = new MergeSortIterator(msa);
			_dupes = _merge.duplicates();

			for (Path tempFilePath : tempFiles) {
				tempFilePath.toFile().delete();
			}
			args._tmpDir.toFile().delete();
		} else if (one != null) {
			_single = one;
			_dupes = dupes;
		}
	}
	
	public long duplicates(){
		if(_merge==null && _single==null)
			return 0;
		else if(_merge!=null)
			return _merge.duplicates();
		else
			return _dupes;
	}
	
	public long count(){
		return _count;
	}

	public boolean hasNext() {
		if(_merge==null && _single==null)
			return false;
		else if(_merge!=null)
			return _merge.hasNext();
		else
			return _single.hasNext();
	}

	public Node[] next() {
		if(_merge==null && _single==null)
			throw new NoSuchElementException();
		else if(_merge!=null){
			_count++;
			return _merge.next();
		}
		else{
			_count++;
			return _single.next();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public static class SortArgs {
		public static final int DEFAULT_FW = 0;
		public static final boolean DEFAULT_GZIP_BATCH = true;
		
		public static final int ADAPTIVE_BATCHES = Integer.MIN_VALUE;
		
		private final Iterator<Node[]> _in;
		private final short _nxlength;
		
		private int _linesPerBatch;
		private Comparator<Node[]> _nc;
		private Path _tmpDir;
		
		private boolean _gzipBatch = DEFAULT_GZIP_BATCH;
		
		private int _ticks = 0;
		
		private int _fw = 0;
		
		public SortArgs(Iterator<Node[]> in){
			SniffNodeArrayLengthIterator si = new SniffNodeArrayLengthIterator(in);
			
			_in = si;
			_nxlength = si.nxLength();
			
			initDefaults(_nxlength);
		}
		
		public SortArgs(Iterator<Node[]> in, short nxlength){
			_in = in;
			_nxlength = nxlength;
			
			initDefaults(nxlength);
		}
		
		public SortArgs(Iterator<Node[]> in, short nxlength, int linesPerBatch) {
			// to avoid the calculation of linesPerBatch
			_in = in;
			_nxlength = nxlength;
			_linesPerBatch = linesPerBatch;
	
			// from initDefaults(int):
			_nc = NodeArrayComparator.NC;

			try {
				_tmpDir = Files.createTempDirectory(PREFIX);
				_tmpDir.toFile().deleteOnExit();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			_ticks = 0;

			_fw = DEFAULT_FW;
		}
		
		private void initDefaults(int nxlength){
			_nc = NodeArrayComparator.NC;

			try {
				_tmpDir = Files.createTempDirectory(PREFIX);
				_tmpDir.toFile().deleteOnExit();
			} catch (IOException e) {
				throw new RuntimeException(e);
			};

			_ticks = 0;
			
			_fw = DEFAULT_FW;
			
//			_linesPerBatch = ADAPTIVE_BATCHES;
			if(_nxlength>0)
				_linesPerBatch = MemoryManager.estimateMaxStatements(nxlength);
			else _linesPerBatch = 1;
		}
		
		public void setComparator(Comparator<Node[]> nc){
			_nc = nc;
		}
		
		/**
		 * Set adaptive batches based on monitoring heap-space.
		 * Overrides setLinesPerBatch.
		 */
		public void setAdaptiveBatches(){
			_linesPerBatch = ADAPTIVE_BATCHES;
		}
		
		/**
		 * Set the number of tuples per batch. Overrides setAdaptiveBatch
		 * @param linesPerBatch
		 */
		public void setLinesPerBatch(int linesPerBatch){
			_linesPerBatch = linesPerBatch;
		}
		
		/**
		 * Set GZipping of batch files (slower but less disk used)
		 * @param tmpDir
		 */
		public void setGzipBatches(boolean gzbat){
			_gzipBatch = gzbat;
		}
		
		public void setTicks(int ticks){
			_ticks = ticks;
		}
		
		/**
		 * Sets a flyweight cache which caches input Nodes
		 * and recycles references to increase in-memory
		 * savings (default is 0: off).
		 * 
		 * Only really useful if adaptive batches are used.
		 * 
		 * @param fw size of flyweight cache
		 * @return
		 */
		public void setFlyWeight(int fw){
			_fw = fw;
		}
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}
}
