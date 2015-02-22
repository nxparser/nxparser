package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.rdfxml.RdfXmlParser;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;

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
		
		if (cmd.hasOption("i")) {
			if (cmd.getOptionValue("i").equals("-")) {
				in = System.in;
			} else {
				in = new FileInputStream(cmd.getOptionValue("i"));
			}
		}
		
		Callback cb;
		if (cmd.hasOption('o') && !cmd.getOptionValue("o").equals("-"))
			cb = new CallbackNxBufferedWriter(Files.newBufferedWriter(
					FileSystems.getDefault().getPath(cmd.getOptionValue('o')),
					StandardCharsets.UTF_8), true);
		else
			cb = new CallbackNxBufferedWriter(new BufferedWriter(
					new OutputStreamWriter(System.out)));

		RdfXmlParser rxp = new RdfXmlParser();

		rxp.parse(in,baseuri);
		
		long count = 0;
		
		cb.startDocument();
		while(rxp.hasNext()){
			cb.processStatement(rxp.next());
			count++;
		}

		cb.endDocument();

		System.err.println("Processed  "+count+" statements");
	}
}
