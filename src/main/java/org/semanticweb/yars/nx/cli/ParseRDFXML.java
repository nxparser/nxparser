package org.semanticweb.yars.nx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars2.rdfxml.RDFXMLParser;

public class ParseRDFXML {
	public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException{
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);
		
		Option outputO = new Option("o", "name of file to write, - for stdout");
		outputO.setArgs(1);
		
		Option baseuriO = new Option("b", "base uri");
		baseuriO.setArgs(1);
		baseuriO.setRequired(true);

		Option helpO = new Option("h", "print help");

		Options options = new Options();
		options.addOption(inputO);
		options.addOption(outputO);
		options.addOption(baseuriO);
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

		String baseuri = cmd.getOptionValue("b");
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
		
		RDFXMLParser rxp = new RDFXMLParser(in,baseuri);

		long count = 0;
		
		while(rxp.hasNext()){
			Node[] nx = rxp.next();
			count++;

			for(Node n:nx) {
				out.print(n.toN3()+" ");
			}
			out.println(".");
		}

		in.close();

		System.err.println("Processed  "+count+" statements");
	}
}
