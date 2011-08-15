package org.semanticweb.yars.nx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.TreeSet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.NxParser;

public class PickLabels {
	
	/**
	 * 
	 */
	public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException {
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);
		
		Option outputO = new Option("o", "name of file to write, - for stdout");
		outputO.setArgs(1);
		
		Option labelsO = new Option("l", "list of label predicates");
		labelsO.setArgs(1);
		labelsO.setRequired(true);

		Option helpO = new Option("h", "print help");
		Options options = new Options();
		options.addOption(inputO);
		options.addOption(outputO);
		options.addOption(labelsO);
		options.addOption(helpO);

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
		
		InputStream in = System.in;
		PrintStream out = System.out;
		
		if (cmd.hasOption("i")) {
			if (cmd.getOptionValue("i").equals("-")) {
				in = System.in;
			} else {
				in = new FileInputStream(cmd.getOptionValue("i"));
			}
		}
		
		if (cmd.hasOption("o")) {
			if (cmd.getOptionValue("o").equals("-")) {
				out = System.out;
			} else {
				out = new PrintStream(new FileOutputStream(cmd.getOptionValue("o")));
			}
		}
		
		TreeSet<Resource> labels = new TreeSet<Resource>();
		
		FileInputStream flin = new FileInputStream(cmd.getOptionValue("l"));
		NxParser nxp = new NxParser(flin);
		while (nxp.hasNext()) {
			labels.add((Resource)nxp.next()[0]);
		}
		flin.close();
		
		nxp = new NxParser(in);
		while (nxp.hasNext()) {
			Node[] line = nxp.next();
			if(labels.contains(line[1]) && line[2] instanceof Literal) {
				for (int i = 0 ; i < line.length; i++) {
					out.print(line[i].toN3() + " ");
				}
				out.println(".");
			}
		}
		
		out.close();
		in.close();
	}
}
