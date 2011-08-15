package org.semanticweb.yars.nx.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeComparator;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.NxParser;

public class CreateQuad {

    /**
     * 
     */
    public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException {
	Options options = new Options();

	Option inputO = new Option("i", "name of file to read, - for stdin");
	inputO.setArgs(1);
	options.addOption(inputO);
	
	Option outputO = new Option("o", "output directory");
	outputO.setArgs(1);
	options.addOption(outputO);
	
	
	Option context = new Option("c", "context");
	context.setArgs(1);
	options.addOption(context);

	Option helpO = new Option("h", "print help");
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

	if (cmd.hasOption("i")) {
	    if (cmd.getOptionValue("i").equals("-")) {
		in = System.in;
	    } else {
		in = new FileInputStream(cmd.getOptionValue("i"));
	    }
	}

	PrintWriter pw = new PrintWriter(System.out);
	if(cmd.hasOption("o")){
	    pw = new PrintWriter(new File(cmd.getOptionValue("c")));
	}
	
	Resource cntx = new Resource(cmd.getOptionValue("c"));
	
	NxParser nxp = new NxParser(in);
	while (nxp.hasNext()) {
	    Node[] nx = nxp.next();
	    pw.println(new Nodes(nx[0],nx[1],nx[2],cntx).toN3());
	}
	in.close();
	pw.close();
    }
}
