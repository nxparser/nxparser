package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeArrayComparator;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.reorder.ReorderIterator;
import org.semanticweb.yars.nx.sort.SortIterator;
import org.semanticweb.yars.nx.sort.SortIterator.SortArgs;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;
import org.semanticweb.yars.util.CheckSortedIterator;
import org.semanticweb.yars.util.SniffNodeArrayLengthIterator;

public class Sort {
	static transient Logger _log = Logger.getLogger(Sort.class.getName());
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws org.semanticweb.yars.nx.parser.ParseException 
	 */
	public static void main(String[] args) throws org.semanticweb.yars.nx.parser.ParseException, IOException {
		Options	options = Main.getStandardOptions();
		
		Option reorderO = new Option("re", "reorder (prior to sort): e.g. 0123 for SPOC 3012 for CSPO");
		reorderO.setArgs(1);
		reorderO.setRequired(false);
		options.addOption(reorderO);
		
		Option sortOrderO = new Option("so", "sort order: e.g. 0123 for SPOC 3012 for CSPO (written order preserved)");
		sortOrderO.setArgs(1);
		sortOrderO.setRequired(false);
		options.addOption(sortOrderO);
		
		Option numericOrderO = new Option("no", "numeric order: e.g. 2 for objects of order SPOC/0123, 21 for objects and predicates (independent of sort order)");
		numericOrderO.setArgs(1);
		numericOrderO.setRequired(false);
		options.addOption(numericOrderO);
		
		Option reverseOrderO = new Option("ro", "reverse order: e.g. 2 for objects of order SPOC/0123, 21 for objects and predicates (independent of sort order)");
		reverseOrderO.setArgs(1);
		reverseOrderO.setRequired(false);
		options.addOption(reverseOrderO);
		
		Option tmpO = new Option("tmp", "Deprecated! tmp folder for batches");
		tmpO.setArgs(1);
		tmpO.setRequired(false);
		options.addOption(tmpO);
		
		Option adO = new Option("ad", "allow duplicates");
		adO.setArgs(0);
		adO.setRequired(false);
		options.addOption(adO);
		
		Option batchO = new Option("b", "set batch size (default calculated based on magic numbers, tuple length and heap space)");
		batchO.setArgs(1);
		batchO.setRequired(false);
		options.addOption(batchO);
		
		Option nogzipbO = new Option("nbz", "no batch gzipping, takes more disk, less time (default gzipped)");
		nogzipbO.setArgs(0);
		nogzipbO.setRequired(false);
		options.addOption(nogzipbO);
		
		Option adaptiveO = new Option("ab", "adaptive batching based on monitoring of heap-space (default static batches, experimental/not recommended)");
		adaptiveO.setArgs(0);
		adaptiveO.setRequired(false);
		options.addOption(adaptiveO);
		
		Option flyweightO = new Option("fw", "flyweight cache size for input iterator (default off, experimental/not recommended)");
		flyweightO.setArgs(1);
		flyweightO.setRequired(false);
		options.addOption(flyweightO);
		
		Option verifyO = new Option("v", "verify sort order (debug mode)");
		verifyO.setArgs(0);
		verifyO.setRequired(false);
		options.addOption(verifyO);
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("***ERROR: " + e.getClass() + ": " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}
		
		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}
		
		if(cmd.hasOption("b") && cmd.hasOption("ab")){
			System.err.println("***ERROR: Please set -b *OR* -ab.");
		}

		InputStream is = Main.getMainInputStream(cmd);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Main.getMainOutputStream(cmd)));
		int ticks = Main.getTicks(cmd);
		
		NxParser nxp = new NxParser();
		nxp.parse(is);
		Iterator<Node[]> it = nxp;
		
		if(cmd.hasOption("re")){
			int[] re_mask = Reorder.getMask(cmd.getOptionValue("re"));
			it = new ReorderIterator(it, re_mask);
		}
		
		
		Callback cb = new CallbackNxBufferedWriter(bw);
		
		org.semanticweb.yars.nx.NodeArrayComparator.NodeArrayComparatorArgs nca = new org.semanticweb.yars.nx.NodeArrayComparator.NodeArrayComparatorArgs();
		if(cmd.hasOption("so")){
			nca.setOrder(Main.getIntegerMask(cmd.getOptionValue("so")));
		}
		
		if(cmd.hasOption("no")){
			nca.setNumeric(Main.getBooleanMask(cmd.getOptionValue("no")));
		}
		
		if(cmd.hasOption("ro")){
			nca.setReverse(Main.getBooleanMask(cmd.getOptionValue("ro")));
		}
		
		if(cmd.hasOption("ad")){
			nca.setNoEquals(true);
			nca.setNoZero(true);
		}
		
		if (cmd.hasOption("tmp")) {
			throw new UnsupportedOperationException(
					"Use java temp directory specification instead.");
		}

		SniffNodeArrayLengthIterator sit = new SniffNodeArrayLengthIterator(it);
		
		NodeArrayComparator nc = new NodeArrayComparator(nca);
		
		SortArgs sa = new SortArgs(sit, sit.nxLength());
		sa.setTicks(ticks);
		sa.setComparator(nc);
		
		if(cmd.hasOption("b"))
			sa.setLinesPerBatch(Integer.parseInt(cmd.getOptionValue("b")));
		else if(cmd.hasOption("ab"))
			sa.setAdaptiveBatches();
		
		if(cmd.hasOption("fw"))
			sa.setFlyWeight(Integer.parseInt(cmd.getOptionValue("fw")));
		
		if(cmd.hasOption("nbz"))
			sa.setGzipBatches(false);
		
		SortIterator si = new SortIterator(sa);
		Iterator<Node[]> iter = si;
		
		CheckSortedIterator csi = null;
		if(cmd.hasOption("v")){
			_log.info("Also verifying sort order...");
			csi = new CheckSortedIterator(si, nc);
			iter = csi;
		}
		
		cb.startDocument();
		while(iter.hasNext()){
			cb.processStatement(iter.next());
		}
		cb.endDocument();
		
		if(cmd.hasOption("v")){
			_log.info("Also verifying sort order...");
			iter = new CheckSortedIterator(si, nc);
		}
		
		_log.info("Finished sort. Sorted "+si.count()+" with "+si.duplicates()+" duplicates.");
		
		is.close();
		bw.close();
		
		if(csi != null){
			_log.info("Sort order okay? : " + csi.isOkay());
		}
	}
}
