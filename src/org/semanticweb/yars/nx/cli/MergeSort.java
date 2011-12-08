package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.semanticweb.yars.nx.NodeComparator;
import org.semanticweb.yars.nx.NodeComparator.NodeComparatorArgs;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.sort.MergeSortIterator;
import org.semanticweb.yars.nx.sort.MergeSortIterator.MergeSortArgs;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;
import org.semanticweb.yars.util.CallbackNxOutputStream;
import org.semanticweb.yars.util.CheckSortedIterator;

public class MergeSort {
	static transient Logger _log = Logger.getLogger(MergeSort.class.getName());
	
	public final static String DIR = ".";
	/**
	 * @param args
	 * @throws IOException 
	 * @throws org.semanticweb.yars.nx.parser.ParseException 
	 */
	public static void main(String[] args) throws org.semanticweb.yars.nx.parser.ParseException, IOException {
		Options	options = new Options();
		
		Main.addHelpOption(options);
		Main.addOutputOption(options, "o", "");
		Main.addTicksOption(options);
		Main.addInputsOption(options, "i", "");
		
		Option sortOrderO = new Option("so", "sort order: e.g. 0123 for SPOC 3012 for CSPO (written order preserved)");
		sortOrderO.setArgs(1);
		sortOrderO.setRequired(true);
		options.addOption(sortOrderO);
		
		Option numericOrderO = new Option("no", "numeric order: e.g. 2 for objects of order SPOC/0123, 21 for objects and predicates (independent of sort order)");
		numericOrderO.setArgs(1);
		numericOrderO.setRequired(false);
		options.addOption(numericOrderO);
		
		Option reverseOrderO = new Option("ro", "reverse order: e.g. 2 for objects of order SPOC/0123, 21 for objects and predicates (independent of sort order)");
		reverseOrderO.setArgs(1);
		reverseOrderO.setRequired(false);
		options.addOption(reverseOrderO);
		
		Option adO = new Option("ad", "allow duplicates");
		adO.setArgs(0);
		adO.setRequired(false);
		options.addOption(adO);
		
		Option verifyO = new Option("v", "verify ordered");
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

		int ticks = Main.getTicks(cmd);
		
		InputStream[] iss = Main.getMainInputStreams(cmd);
		Iterator<Node[]>[] iters = new Iterator[iss.length];
		for (int i=0; i< iss.length; i++) {
			iters[i] = new NxParser(iss[i]);
		}
		_log.info("Opened "+iters.length+" files for merging");
		
		OutputStream os = Main.getMainOutputStream(cmd);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		Callback cb = new CallbackNxBufferedWriter(bw);
		
		
		NodeComparatorArgs nca = new NodeComparatorArgs();
		if(cmd.hasOption("so")){
			nca.setOrder(NodeComparatorArgs.getIntegerMask(cmd.getOptionValue("so")));
		}
		
		if(cmd.hasOption("no")){
			nca.setNumeric(NodeComparatorArgs.getBooleanMask(cmd.getOptionValue("no")));
		}
		
		if(cmd.hasOption("ro")){
			nca.setReverse(NodeComparatorArgs.getBooleanMask(cmd.getOptionValue("ro")));
		}
		
		if(cmd.hasOption("ad")){
			nca.setNoEquals(true);
			nca.setNoZero(true);
		}
		
		MergeSortArgs msa = new MergeSortArgs(iters);
		NodeComparator nc = new NodeComparator(nca);
		msa.setComparator(nc);
		msa.setTicks(ticks);
		
		MergeSortIterator msi = new MergeSortIterator(msa);
		
		boolean verify = cmd.hasOption("v");
		
		CheckSortedIterator csi = null;
		Iterator<Node[]> in = msi;
		
		if(verify){
			csi = new CheckSortedIterator(msi, nc);
			in = csi;
		}
		
		while(in.hasNext()){
			cb.processStatement(in.next());
			if(verify){
				if(!csi.isOkay()){
					throw new RuntimeException(csi.getException());
				}
			}
		}
		
		_log.info("Finished sort. Sorted "+msi.count()+" with "+msi.duplicates()+" duplicates.");
		
		for(InputStream is:iss){
			is.close();
		}
		bw.close();
	}
	
	static int[] getMask(String arg){
		int[] reorder = new int[arg.length()];
		
		for(int i=0; i<reorder.length; i++){
			reorder[i] = Integer.parseInt(Character.toString(arg.charAt(i)));
		}
		
		return reorder;
	}
}
