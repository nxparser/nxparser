package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

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
import org.semanticweb.yars.util.CallbackNxBufferedWriter;

/**
 * CLI utility to split a file according to a modulo/hash function on specified elements.
 * @author Aidan Hogan
 */
public class Split {
	static transient Logger _log = Logger.getLogger(Split.class.getName());
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws org.semanticweb.yars.nx.parser.ParseException 
	 */
	public static void main(String[] args) throws org.semanticweb.yars.nx.parser.ParseException, IOException {
		Options	options = Main.getStandardOptions();
		options.getOption("o").setDescription("output file mask; should contain a '%' which will be replaced by file number; directory should exist");
		
		Option sortOrderO = new Option("e", "elements to hash for split, e.g. 01 to hash on first and second element; default no hashing, random split");
		sortOrderO.setArgs(1);
		sortOrderO.setRequired(false);
		options.addOption(sortOrderO);
		
		Option splitO = new Option("n", "number of output files to split");
		splitO.setArgs(1);
		splitO.setRequired(true);
		options.addOption(splitO);
		
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
		
		int n = Integer.parseInt(cmd.getOptionValue("n"));
		if(n<2){
			System.err.println("***ERROR: n should be an integer >2");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}

		InputStream is = Main.getMainInputStream(cmd);
		NxParser nxp = new NxParser();
		nxp.parse(is);
		Iterator<Node[]> it = nxp ;
		
		
		OutputStream[] oss = new OutputStream[n];
		BufferedWriter[] bw = new BufferedWriter[n];
		Callback[] cbs = new CallbackNxBufferedWriter[n];
		String o = cmd.getOptionValue("o");
		if(!o.contains("%")){
			System.err.println("***ERROR: o should contain at least one '%' character");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}
		
		for(int i=0; i<n; i++){
			String fn = o.replaceAll("%", Integer.toString(i));
			Main.mkdirsForFile(fn);
			oss[i] = new FileOutputStream(fn);
			if(cmd.hasOption("ogz"))
				oss[i] = new GZIPOutputStream(oss[i]);
			bw[i] = new BufferedWriter(new OutputStreamWriter(oss[i]));
			cbs[i] = new CallbackNxBufferedWriter(bw[i]);
		}
		
		int ticks = Main.getTicks(cmd);
		
		int[] els = null; Random r = null;
		if(cmd.hasOption("e")){
			els = Main.getMask(cmd.getOptionValue("e"));
		} else{
			r = new Random();
		}
		
		int[] counts = new int[n];
		int read = 0;
		
		while(it.hasNext()){
			Node[] next = it.next();
			
			read++;
			if(read%ticks==0){
				_log.info("...read "+read);
			}
			
			int fileIndex = -1;
			if(els!=null){
				Node[] key = new Node[els.length];
				for(int i=0; i<els.length; i++){
					key[i] = next[els[i]];
				}
				
				int hash = Arrays.hashCode(key);
				hash = Math.abs(hash);
				fileIndex = hash % n;
			} else{
				fileIndex = r.nextInt(n);
			}
			
			cbs[fileIndex].processStatement(next);
			counts[fileIndex]++;
		}
		
		is.close();
		for(BufferedWriter b:bw)
			b.close();
		
		_log.info("...finished... read "+read+".");
		for(int i=0; i<n; i++){
			_log.info("...file "+i+" written "+counts[i]);
		}
		double aad = absAverageDeviation(counts);
		_log.info("... absolute average deviation from mean "+aad);
	}
	
	public static double absAverageDeviation(int[] cs){
		int sum = 0;
		for(int c:cs)
			sum+=c;
		double avg = sum / (double)cs.length;
		
		double devSum = 0;
		for(int c:cs)
			devSum+= Math.abs(c - avg);
		
		return devSum/cs.length;
	}
	
	
}
