package org.semanticweb.yars.nx.cli;

import java.io.InputStream;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeComparator;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.NxParser;

public class CheckSorted {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Options	options = new Options();
		Main.addInputOption(options, "i", "sorted input");
		Main.addTicksOption(options);
		Main.addHelpOption(options);
		
		Option sortOrderO = new Option("so", "sort order: e.g. 0123 for SPOC 3012 for CSPO (written order preserved)");
		sortOrderO.setArgs(1);
		sortOrderO.setRequired(false);
		options.addOption(sortOrderO);
		
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

		InputStream is = Main.getMainInputStream(cmd);
		int ticks = Main.getTicks(cmd);
		
		Iterator<Node[]> it = new NxParser(is);
		
		Comparator<Node[]> nc = NodeComparator.NC;
		if(cmd.hasOption("so")){
			nc = new NodeComparator(Sort.getMask(cmd.getOptionValue("so")));
		}
		
		int count = 1;
		boolean fine = true;
		if(it.hasNext()){
			Node[] nxold = it.next();
			while (it.hasNext()) {
				count++;
				if(ticks>0 && count%ticks==0){
					System.err.println("Read "+count);
				}
				Node[] nx = it.next();
				if(nc.compare(nxold,nx)>0){
					System.err.println(Nodes.toN3(nxold)+" before "+Nodes.toN3(nx));
					fine = false;
				}
				nxold = nx;
			}
		}
		
		System.err.println("Read "+count);

		if(fine){
			System.err.println("Passed");
		}
	}
}