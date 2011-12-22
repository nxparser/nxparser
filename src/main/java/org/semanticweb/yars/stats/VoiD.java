/**
 * 
 */
package org.semanticweb.yars.stats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.filter.NodeFilter;
import org.semanticweb.yars.nx.filter.NodeFilter.ClassFilter;
import org.semanticweb.yars.nx.filter.NodeFilter.EqualsFilter;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.stats.output.ToVoid;


/**
 * @author juergen
 *
 */
public class VoiD {
	private static final DecimalFormat twoDigit = new DecimalFormat("###,###,###.##");
	final private static Runtime runtime = Runtime.getRuntime();
	private final static NumberFormat FMT = NumberFormat.getInstance();
	{
		FMT.setMaximumFractionDigits(2);
		FMT.setMinimumFractionDigits(2);
	}
	
	/**
	 * @return
	 **/
	public static long getFreeMem() {
		return runtime.maxMemory() - runtime.totalMemory();
	}
	
	/**
	 * @param string
	 */
	public static void logMem(String msg) {
		long mem = runtime.maxMemory() - runtime.totalMemory();
		float KBytes = mem / (float) 1024;
		float MBytes = KBytes / 1024;
		float GBytes = MBytes / 1024;
		if (GBytes >= 1)
			System.err.println("[MEMLOG] ("+msg+") FreeMemory: " + FMT.format(GBytes) + " GB");
		else if (MBytes >= 1) {
			System.err.println("[MEMLOG] ("+msg+") FreeMemory: " + FMT.format(MBytes) + " MB");
		} else
			System.err.println("[MEMLOG] ("+msg+") FreeMemory: " + FMT.format(KBytes) + " KB");
	}
	
	
	InputAnalyser input;
	private  String _dataID;
	/**
	 * @param file 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws ParseException 
	 * 
	 */
	
	public void analyseVoid(InputStream in, String dataSetId, OutputStream out) throws IOException, ParseException {
		input = new InputAnalyser(new NxParser(in));
		_dataID = dataSetId;
		int [] subject = {0};
		int [] predicate = {1};
		int [] object = {2};
		
		//triple counter
		CountStmtAnalyser stmt = new CountStmtAnalyser(input);
		
		//distinct classes
		int [] classes = {1,2};
		NodeFilter[] classFilter = {new EqualsFilter(RDF.TYPE.toString()),new ClassFilter(Resource.class)};
		NodeTransformer<Node> classesTrans = new NodeTransformer<Node>() {
			public Node[] processNode(Node[] n) {
				Node [] res = new Node[1];
				res[0] = n[1];
				return res;
			}
		};
		DistributionAnalyser<Node>  distClasses= new DistributionAnalyser<Node>(stmt,classFilter, classes, classesTrans);
		
		NodeTransformer<String> loggerTrans = new NodeTransformer<String>() {
			private int counter;
			String [] res = {"-"};
			public String[] processNode(Node[] n) {
				counter++;
				if(counter%10000 == 0) VoiD.logMem("lines "+twoDigit.format(counter));
				return res;
			}
		};
		DistributionAnalyser<String> logger = new DistributionAnalyser<String>(distClasses,loggerTrans);
		
		DefaultAnalyser run = logger;
		while(run.hasNext()) {run.next();}
		
		
		String statements = ToVoid.getStmtString(stmt.getStmt());
		
		String classDist = ToVoid.getClassDist(distClasses.getStatsMap());

		
		String datasetContent = ToVoid.getDataSetHeader(_dataID, statements+classDist);
		
		String rdf = ToVoid.getRDF(datasetContent);
		
		out.write(rdf.getBytes());
		out.close();
	}

}