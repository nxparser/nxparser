package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.util.NxUtil;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;

/**
 * Code for patching original BTC 12 data after any23 mix-up in bnode handling
 * @author aidhog
 *
 */

public class FixBNodes2 {
	static Logger _log = Logger.getLogger(FixBNodes2.class.getName());
	
	public static void main (String[] args) throws URISyntaxException, IOException{
		Options	options = Main.getStandardOptions();
		
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
		NxParser nxp = new NxParser(is);
		
		OutputStream os = Main.getMainOutputStream(cmd);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		Callback cb = new CallbackNxBufferedWriter(bw);
		
		int ticks = Main.getTicks(cmd);
		
		int c = 0;
		
		while (nxp.hasNext()) {
			Node[] nx = nxp.next();
			c++;
			
			if(c % ticks==0)
				_log.info("Read "+c+" lines");
			
			for (int i = 0; i<nx.length; i++) {
				if (nx[i] instanceof Resource) {
					String nxs = nx[i].toN3();					
					if (nxs.startsWith("<node") && !nxs.contains(":") && nxs.length()>6) {
						nx[i] = BNode.createBNode(nx[3].toString(), nx[i].toN3().substring(5,nxs.length()-1));
						_log.info("Fixing bnode "+nxs+" to "+nx[i].toN3()+" in doc "+nx[3]);
					} else {
						nx[i] = new Resource(NxUtil.escapeForNx(nx[i].toString()));
						if(!nxs.equals(nx[i].toN3())){
							_log.info("Fixing resource "+nxs+" to "+nx[i].toN3()+" in doc "+nx[3]);
						}
					}
				}
			}
			cb.processStatement(nx);
		}
		
		_log.info("Finished. Read "+c+" lines");
		
		bw.close();
	}
}
