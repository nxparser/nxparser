package org.semanticweb.yars.nx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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

public class CreateRDFXML {
	
	/**
	 * Create RDF/XML from NTriples files (sorted by subject)
	 */
	public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException {
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
		
		printHeaderRDFXML(out);
		
		Node oldsubj = null;
		Node subj = null;
		List<Node[]> list = new ArrayList<Node[]>();

		NxParser nxp = new NxParser();
		nxp.parse(in);
		while (nxp.hasNext()) {
			Node[] nx = nxp.next();
			
			subj = nx[0];
			
			// new subject encountered
			if (oldsubj != null && !subj.equals(oldsubj)) {
				printRDFXML(list, out);
				list = new ArrayList<Node[]>();
			}
			
			list.add(nx);

			oldsubj = subj;
		}
		
		printRDFXML(list, out);

		printFooterRDFXML(out);

		in.close();
		out.close();
	}
	
	static void printHeaderRDFXML(PrintStream out) {
		out.println("<?xml version='1.0'?>");
		out.println("<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>");
	}
	
	static void printFooterRDFXML(PrintStream out) {
		out.println("</rdf:RDF>");
	}
	
	static void printRDFXML(List<Node[]> list, PrintStream out) {
		if (list.isEmpty()) {
			return;
		}
		
		for (Node[] nx : list) {
			if (nx[2].toString().equals("0.0")) {
				return;
			}
		}
		
		Node subj = list.get(0)[0];
		out.print("<rdf:Description");
		
		if (subj instanceof Resource) {
			out.println(" rdf:about='" + escape(subj.toString()) + "'>");
		} else if (subj instanceof BNode) {
			out.println(" rdf:nodeID='" + subj.toString() + "'>");
		}
		
		for (Node[] ns: list) {
			String r = ns[1].toString();
			String namespace = null, localname = null;
			int i = r.indexOf('#');

			if (i > 0) {
				namespace = r.substring(0, i+1);
				localname = r.substring(i+1, r.length());
			} else {
				i = r.lastIndexOf('/');
				if (i > 0) {
					namespace = r.substring(0, i+1);
					localname = r.substring(i+1, r.length());
				}
			}
			if (namespace == null && localname == null) {
				System.err.println("couldn't separate namespace and localname");
				break;
			}
			
			out.print("\t<" + localname + " xmlns='" + namespace + "'");
			if (ns[2] instanceof BNode) {
				out.println(" rdf:nodeID='" + ns[2].toString() + "'/>");
			} else if (ns[2] instanceof Resource) {
				out.println(" rdf:resource='" + escape(ns[2].toString()) + "'/>");				
			} else if (ns[2] instanceof Literal) {
				Literal l = (Literal)ns[2];
				if (l.getLanguageTag() != null) {
					out.print(" xml:lang='" + l.getLanguageTag() + "'");
				} else if (l.getDatatype() != null) {
					out.print(" rdf:datatype='" + l.getDatatype().toString() + "'");					
				}
				out.println(">" + escape(ns[2].toString()) + "</" + localname + ">");
			}
		}
		
		out.println("</rdf:Description>");
	}
	
	private static String escape(String s){
		String e;
		e = s.replaceAll("&", "&amp;");
		e = e.replaceAll("<", "&lt;");
		e = e.replaceAll(">", "&gt;");
		e = e.replaceAll("\"","&quot;");
		e = e.replaceAll("'","&apos;");
		return e;
	}
}
