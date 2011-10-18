package org.semanticweb.yars.nx.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.reorder.ReorderIterator;
import org.semanticweb.yars.util.CallbackNxOutputStream;

/**
 * Reorder some statements
 * @author aidhog
 *
 */
public class Reorder {
	static transient Logger _log = Logger.getLogger(Reorder.class.getName());
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws org.semanticweb.yars.nx.parser.ParseException 
	 */
	public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException {
		
		Option maskO = new Option("m", "where 0123 is an integer mask, out[0] = in[2], out[1] = in[3], out[2] = in[0], out[3] = in[1]\n" +
		"example: spoc => pocs, pocs => ocsp, ocsp => cspo, cspo => spoc 1230\n" +
		"example: pocs => spoc, spoc => cspo, cspo => ocsp, ocsp => pocs 3012\n" +
		"example: spoc => sopc 0213\n" +
		"example: spoc => cpso 3102");
		maskO.setArgs(1);
		maskO.setRequired(true);
		
		Options options = Main.getStandardOptions();
		options.addOption(maskO);

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
		
		int[] mask = getMask(cmd.getOptionValue("m"));

		InputStream is = Main.getMainInputStream(cmd);
		OutputStream os = Main.getMainOutputStream(cmd);
		int ticks = Main.getTicks(cmd);
		
		Iterator<Node[]> it = new NxParser(is);
		Callback cb = new CallbackNxOutputStream(os, true);
		
		ReorderIterator ri = new ReorderIterator(it, mask, ticks);
		
		while(ri.hasNext()){
			cb.processStatement(ri.next());
		}
		
		_log.info("Finished reorder. Reordered "+ri.count()+" statements.");
		
		is.close();
		cb.endDocument();
	}

	static int[] getMask(String arg){
		int[] reorder = new int[arg.length()];
		
		for(int i=0; i<reorder.length; i++){
			reorder[i] = Integer.parseInt(Character.toString(arg.charAt(i)));
		}
		
		return reorder;
	}
}
