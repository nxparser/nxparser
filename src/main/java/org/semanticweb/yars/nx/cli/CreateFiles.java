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
import org.semanticweb.yars.nx.parser.NxParser;

public class CreateFiles {
	
	/**
	 * 
	 */
	public static void main(String[] args) throws IOException, org.semanticweb.yars.nx.parser.ParseException {
		Option inputO = new Option("i", "name of file to read, - for stdin");
		inputO.setArgs(1);

		Option outputO = new Option("o", "output directory");
		outputO.setArgs(1);
		outputO.setRequired(true);
		
		Option appendO = new Option("a", "append, not overwrite");
		appendO.setArgs(0);
		
		Option sourcesO = new Option("s", "only use specified sources");
		sourcesO.setArgs(1);

		Option dirO = new Option("c", "create directory structure");
		dirO.setArgs(0);
		
		Option helpO = new Option("h", "print help");
		Options options = new Options();
		options.addOption(inputO);
		options.addOption(outputO);
		options.addOption(dirO);
		options.addOption(appendO);
		options.addOption(sourcesO);
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
		
		File dir = new File(cmd.getOptionValue("o"));
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		boolean dirstr = false;
		if (cmd.hasOption("c")) {
			dirstr = true;
		}

		
		Set<Node> sources = null;
		
		if (cmd.hasOption("s")) {
			sources = new HashSet<Node>();
			NxParser nxp = new NxParser(new FileInputStream(cmd.getOptionValue("s")));
			while (nxp.hasNext()) {
				Node[] nx = nxp.next();
				sources.add(nx[0]);
			}
		}
		
		boolean append = false;
		if (cmd.hasOption("a")) {
			append = true;
		}
		
		Map<Node, Set<Node[]>> data = new HashMap<Node, Set<Node[]>>();
		
		NxParser nxp = new NxParser(in);
		while (nxp.hasNext()) {
			Node[] nx = nxp.next();
			
			if (sources == null || (sources != null && sources.contains(nx[3]))) {			
				Set<Node[]> content;

				if (data.containsKey(nx[3])) {
					content = data.get(nx[3]);
				} else {
					content = new TreeSet<Node[]>(NodeComparator.NC);
					data.put(nx[3], content);
				}

				content.add(nx);
			}
		}
		in.close();
				
		for (Node k : data.keySet()) {
			try {
				if (dirstr) {
					URL u = new URL(k.toString());
					String host = u.getHost();
					String path = u.getPath();
					path = path.substring(0, path.lastIndexOf('/'));
					String fname = u.getFile();
					fname = fname.substring(fname.lastIndexOf('/')+1);
					
					String mdir = dir + "/" + host + "/" + path;
					File m = new File(mdir);
					m.mkdirs();
					writeFile(m, fname, data.get(k), append);
				} else {
					writeFile(dir, URLEncoder.encode(k.toString(), "utf-8"), data.get(k), append);
				}
			} catch (UnsupportedEncodingException ue) {
				ue.printStackTrace();
			} catch (FileNotFoundException fe) {
				fe.printStackTrace();
			}
		}
	}
	
	static void writeFile(File dir, String fname, Set<Node[]> list, boolean append) throws FileNotFoundException {
		File p = new File(dir.getAbsoluteFile() + "/" + fname);
		
		PrintWriter pw = new PrintWriter(new FileOutputStream(p, append));
		for (Node[] ns:list) {
			pw.println(ns[0].toN3() + " " + ns[1].toN3() + " " + ns[2].toN3() + " .");
		}
		pw.close();
	}
}
