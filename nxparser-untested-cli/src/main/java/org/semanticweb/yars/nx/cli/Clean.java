package org.semanticweb.yars.nx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.clean.Cleaner;
import org.semanticweb.yars.nx.parser.NxParser;

public class Clean {
	public static void main (String[] args) throws URISyntaxException, IOException{
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);
		
		Option outputO = new Option("o", "name of file to write, - for stdout");
		outputO.setArgs(1);

		Option helpO = new Option("h", "print help");
		
		Option elementO = new Option("e", "check that all entries have x elements, will skip other entries");
		elementO.setArgs(1);
		
		Option datatypeO = new Option("d", "perform datatype normalisation (eperimental!)");

		Options options = new Options();
		options.addOption(inputO);
		options.addOption(outputO);
		options.addOption(helpO);
		options.addOption(elementO);
		options.addOption(datatypeO);

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
		int elements = -1;
		
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
		
		if (cmd.hasOption("e")) {
			elements = Integer.parseInt(cmd.getOptionValue("e"));
		}
		
		boolean datatype = false;
		if (cmd.hasOption("d")) {
			datatype = true;
		}
		
		NxParser nqp = new NxParser();
		nqp.parse(in);

		Cleaner.clean(nqp, out, elements, datatype);
	}
}
