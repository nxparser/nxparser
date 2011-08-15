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
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class CreateSQL {
	public static void main (String[] args) throws URISyntaxException, IOException{
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);
		
		Option outputO = new Option("o", "name of file to write, - for stdout");
		outputO.setArgs(1);
		
		Option tabO = new Option("t", "create tab-delimited file instead of sql input statements");
		tabO.setArgs(0);

		Option helpO = new Option("h", "print help");
		
		Options options = new Options();
		options.addOption(inputO);
		options.addOption(outputO);
		options.addOption(tabO);
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
		
		boolean tab = false;
		if (cmd.hasOption("t")) {
			tab = true;
		}

		NxParser nxp = new NxParser(in,false);

		while (nxp.hasNext()) {
			Node[] nx = nxp.next();
			
			StringBuffer sb = new StringBuffer();

			if (!tab) {
				sb.append("INSERT INTO n");
				sb.append(nx.length);
				sb.append(" VALUES ('");
			}
			
			for (int i = 0; i < nx.length; i++) {
				String str = nx[i].toN3();
				if (str.length() > 2000) {
					sb = null;
					break;
				}
				if (tab) {
					sb.append(str.replace("\t", "\\t"));
				} else {
					sb.append(str.replace("'", "\'"));
				}
				
				if (i < nx.length-1) {
					if (tab) {
						sb.append("\t");
					} else {
						sb.append("','");
					}
				}
			}
			
			if (sb != null) {
				if (!tab) {
					sb.append("');");
				}
				out.println(sb);
			}
		}
	}
}
