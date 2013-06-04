package org.semanticweb.yars.nx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.util.NxUtil;

public class Parse {
	public static void main(String[] args) throws IOException{
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);
		
		Option inputGzO = new Option("igz", "input is gzipped");
		inputGzO.setArgs(0);
		
		Option outputO = new Option("o", "name of file to write, - for stdout");
		outputO.setArgs(1);
		
		Option outputGzO = new Option("ogz", "output is gzipped");
		outputGzO.setArgs(0);

		Option encodeO = new Option("e", "N-Triples encoding of strings");
		encodeO.setArgs(0);
		
		Option strictO = new Option("s", "strict mode, will end program with parse exception");

		Option helpO = new Option("h", "print help");

		Options options = new Options();
		options.addOption(inputO);
		options.addOption(inputGzO);
		options.addOption(outputO);
		options.addOption(outputGzO);
		options.addOption(strictO);
		options.addOption(helpO);
		options.addOption(encodeO);

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
		boolean strict = false;
		
		if (cmd.hasOption("i")) {
			if (cmd.getOptionValue("i").equals("-")) {
				in = System.in;
			} else {
				in = new FileInputStream(cmd.getOptionValue("i"));
			}
		}
		
		if(cmd.hasOption("igz")){
			in = new GZIPInputStream(in);
		}
		
		if (cmd.hasOption("o")) {
			if (cmd.getOptionValue("o").equals("-")) {
				out = System.out;
			} else {
				if(cmd.hasOption("ogz"))
					out = new PrintStream(new GZIPOutputStream(new FileOutputStream(cmd.getOptionValue("o"))));
				else out = new PrintStream(new FileOutputStream(cmd.getOptionValue("o")));
			}
		}
		
		if (cmd.hasOption("strict")) {
			strict = true;
		}
		
		NxParser nqp = new NxParser(in,strict);

		long count = 0;
		
		while(nqp.hasNext()){
			Node[] nx = nqp.next();
			count++;

			for(Node n:nx) {
				if(cmd.hasOption("e")){
					String str = n.toString();
					if(n instanceof Resource){
						n = new Resource(NxUtil.escapeForNx(str));
					} else if(n instanceof Literal){
						n = new Literal(NxUtil.escapeForNx(str),((Literal) n).getLanguageTag(),((Literal) n).getDatatype());
					} else if(n instanceof BNode){
						n = new BNode(NxUtil.escapeForNx(str));
					} else{
						throw new RuntimeException("Unknown node "+n.toN3());
					}
				}
				out.print(n.toN3()+" ");
			}
			out.println(".");
		}

		in.close();

		System.err.println("Processed  "+count+" statements");
	}
}
