package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.tld.TldManager;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;

/**
 * Extract PLDs from data.
 * @author aidhog
 *
 */
public class GetPlds {
	static transient Logger _log = Logger.getLogger(GetPlds.class.getName());
	
	static TldManager TLDM = null;
	static { 
		try {
			TLDM = new TldManager();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws org.semanticweb.yars.nx.parser.ParseException 
	 */
	public static void main(String[] args) throws org.semanticweb.yars.nx.parser.ParseException, IOException {
		Options	options = Main.getStandardOptions();
		
		Option posO = new Option("p", "positions to extract PLDs from: e.g. 3 for context 012 for triple (default all)");
		posO.setArgs(1);
		posO.setRequired(false);
		options.addOption(posO);
		
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
		OutputStream os = Main.getMainOutputStream(cmd);
		int ticks = Main.getTicks(cmd);
		int[] pos = null;
		if(cmd.hasOption("p"))
			pos = Main.getIntegerMask(cmd.getOptionValue("p"));
		
		NxParser it = new NxParser();
		it.parse(is);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		Callback cb = new CallbackNxBufferedWriter(bw);
		
		TreeSet<Node> plds = new TreeSet<Node>();
		
		int read = 0;
		while(it.hasNext()){
			Node[] ns = it.next();
			
			read++;
			if(ticks>0 && read%ticks==0){
				_log.info("Read "+read);
			}
			
			if(pos==null){
				for(Node n:ns){
					Resource r = extractPld(n);
					if(r!=null) plds.add(r);
				}
			} else for(int p:pos){
				Node n = ns[p];
				Resource r = extractPld(n);
				if(r!=null) plds.add(r);
			}
		}
		
		for(Node p:plds){
			cb.processStatement(new Node[]{p});
		}
		
		_log.info("Finished. Read "+read+". Found "+plds.size()+" PLDs.");
		
		is.close();
		bw.close();
	}
	
	static int[] getMask(String arg){
		int[] reorder = new int[arg.length()];
		
		for(int i=0; i<reorder.length; i++){
			reorder[i] = Integer.parseInt(Character.toString(arg.charAt(i)));
		}
		
		return reorder;
	}
	
	static Resource extractPld(Node n){
		if(!(n instanceof Resource)) return null;
		try{
			URI u = new URI(n.toString());
			String pld = TLDM.getPLD(u);
			if(pld!=null){
				pld = "http://"+pld.toLowerCase()+"/";
				return new Resource(pld);
			}
			return null;
		} catch(Exception e){
			return null;
		}
	}
}
