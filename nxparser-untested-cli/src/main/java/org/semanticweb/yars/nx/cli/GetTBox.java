package org.semanticweb.yars.nx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.OWL;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.RDFS;
import org.semanticweb.yars.nx.parser.NxParser;

public class GetTBox {
	public static void main (String[] args) throws URISyntaxException, IOException{
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);
		
		Option outputO = new Option("o", "name of file to write, - for stdout");
		outputO.setArgs(1);

		Option helpO = new Option("h", "print help");
		
		Options options = new Options();
		options.addOption(inputO);
		options.addOption(outputO);
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
		
		// from BufferTBoxIterator)
		Map<Resource, Integer> tboxTypes = new Hashtable<Resource, Integer>();
		Map<Resource, Integer> tboxPreds = new Hashtable<Resource, Integer>();

		tboxTypes.put(OWL.FUNCTIONALPROPERTY , 0);
		tboxTypes.put(OWL.INVERSEFUNCTIONALPROPERTY , 0);
		tboxTypes.put(OWL.SYMMETRICPROPERTY , 0);
		tboxTypes.put(OWL.TRANSITIVEPROPERTY , 0);
		
		tboxPreds = new Hashtable<Resource, Integer>();
		tboxPreds.put(RDFS.DOMAIN , 0);
		tboxPreds.put(RDFS.RANGE , 0);
		tboxPreds.put(RDFS.SUBCLASSOF , 0);
		tboxPreds.put(RDFS.SUBPROPERTYOF , 0);
		
		tboxPreds.put(RDF.FIRST , 0);
		tboxPreds.put(RDF.REST , 0);
		
		tboxPreds.put(OWL.ALLVALUESFROM , 0);
		tboxPreds.put(OWL.CARDINALITY , 0);
		tboxPreds.put(OWL.MAXCARDINALITY , 0);
		tboxPreds.put(OWL.MINCARDINALITY , 0);
		
		tboxPreds.put(OWL.EQUIVALENTCLASS , 0);
		tboxPreds.put(OWL.EQUIVALENTPROPERTY , 0);
		tboxPreds.put(OWL.HASVALUE , 0);
		tboxPreds.put(OWL.INTERSECTIONOF , 0);
		tboxPreds.put(OWL.INVERSEOF , 0);
		
		tboxPreds.put(OWL.ONEOF , 0);
		tboxPreds.put(OWL.ONPROPERTY , 0);
		tboxPreds.put(OWL.SOMEVALUESFROM , 0);
		tboxPreds.put(OWL.UNIONOF , 0);
		
		NxParser nqp = new NxParser();
		nqp.parse(in);
		boolean write = false;

		while (nqp.hasNext()) {
			Node[] line = nqp.next();

			if(line[1].equals(RDF.TYPE)){
				Integer c = tboxTypes.get(line[2]);
				if(c!=null){
					write = true;
					tboxTypes.put((Resource)line[2], c+1);
				}
			} else{
				Integer c = tboxPreds.get(line[1]);
				if(c!=null){
					write = true;
					tboxPreds.put((Resource)line[1], c+1);
				}
			}
			if (write) {
				out.println(Nodes.toString(line));
				write = false;
			}
		}
	}
}