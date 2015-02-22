package org.semanticweb.yars.nx.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.util.CallbackNxBufferedWriter;

/**
 * Reorder some statements
 * @author aidhog
 *
 */
public class Patch {
	static transient Logger _log = Logger.getLogger(Patch.class.getName());
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws org.semanticweb.yars.nx.parser.ParseException 
	 */
	public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException {
		Options options = Main.getStandardOptions();

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
		
		NxParser nxp = new NxParser();
		nxp.parse(is);
		Iterator<Node[]> it = nxp;
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		Callback cb = new CallbackNxBufferedWriter(bw);
				
		int t = 0;
		int s = 0;
		int o = 0;
		boolean trip = false;
		int a = 0;
		
		while(it.hasNext()){
			a++;
			Node[] next = it.next();
			Node[] copy = new Node[next.length]; 
			System.arraycopy(next, 0, copy, 0, next.length);
			trip = false;
			
			if(next[0].toString().startsWith("<node")){
				trip = true;
				s++;
				copy[0] = BNode.createBNode(next[0].toString(), next[3].toString());
				// According to the naming of the variables, the next line would
				// have been correct, but it is used otherwise in other code.
				// copy[0] = BNode.createBNode(next[3].toString(),
				// next[0].toString());
				// copy[2] = new BNode(next[2].toString()); // No bnode renaming
			}

			if (next[2].toString().startsWith("<node")) {
				trip = true;
				o++;
				copy[2] = BNode.createBNode(next[2].toString(),	next[3].toString());
				// According to the naming of the variables, the next line would
				// have been correct, but it is used otherwise in other code.
				// copy[0] = BNode.createBNode(next[3].toString(),
				// next[0].toString());
				// copy[2] = new BNode(next[2].toString()); // No bnode renaming
			}
			cb.processStatement(copy);
			
			if(trip){
				_log.info("Rewriting "+Nodes.toString(next)+" to "+Nodes.toString(copy));
				t++;
			}
		}
		
		_log.info("Finished patch. Read "+a+" statements. Rewrote "+s+" subjects, "+o+" objects, "+t+" statements.");
		
		is.close();
		bw.close();
	}
}
