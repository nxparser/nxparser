package org.semanticweb.yars.nx.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Main {
	static transient Logger _log = Logger.getLogger(Main.class.getName());
	
	private static final String USAGE = "usage: org.semanticweb.yars.nx.cli.Main <utility> [options...]";
	private static final String PREFIX = "org.semanticweb.yars.nx.cli.";

	public static void main(String[] args) {		
		try {
			if (args.length < 1) {			
				StringBuffer sb = new StringBuffer();
				sb.append("where <utility> one of");
				sb.append("\n\tParse             Parse nx file");
				sb.append("\n\tClean             Normalise URIs and clean HTML from input");
				sb.append("\n\tClean             Normalise URIs and clean HTML from input");
				sb.append("\n\tCleanXML          Removes invalid character references in XML");
				sb.append("\n\tGetURIs           Get all URIs in an NX file");
				sb.append("\n\tBenchmark         Run benchmarks");
				sb.append("\n\tPickLabels        Pick unique labels");
				sb.append("\n\tParseRDFXML       Parse RDF/XML file");
				sb.append("\n\tReorder           Reorder some data");
				sb.append("\n\tSample            Random sampling");
				sb.append("\n\tStats             Analyse the NQ file and print some nice stats");
				sb.append("\n\tSort              Sort some data");
				sb.append("\n\tMergeSort         Merge-sort pre-sorted files");
				
				_log.severe(USAGE);
				_log.severe(sb.toString());
				System.exit(-1);
			}
			
			Class<?> cls = Class.forName(PREFIX + args[0]);
			
			Method mainMethod = cls.getMethod("main", new Class[] { String[].class });

			String[] mainArgs = new String[args.length - 1];
			System.arraycopy(args, 1, mainArgs, 0, mainArgs.length);
			
			long time = System.currentTimeMillis();
			
			mainMethod.invoke(null, new Object[] { mainArgs });
			
			long time1 = System.currentTimeMillis();
			
			_log.info("time elapsed " + (time1-time) + " ms");
		} catch (Throwable e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			cause.printStackTrace();
			_log.severe(USAGE);
			_log.severe(e.toString());
			System.exit(-1);
		}
	}
	
	public static int TICKS = 10000000;
	
	public static final String DEFAULT_TMP_DIR = "./nxtmp/";
//	public static final String DEFAULT_TEMP_DIR = System.getProperty("java.io.tmpdir");
	static{
		File f = new File(DEFAULT_TMP_DIR);
		if(f.mkdirs())
			f.deleteOnExit();;
	}
	
	private static String TMP_DIR = DEFAULT_TMP_DIR;
	
	public static String getRootTempDir(){
		return TMP_DIR;
	}
	
	public static void setRootTempDir(String tmpDir){
		TMP_DIR = tmpDir;
	}
	
	public static final String TEMP_PREFIX = "tmp";
	
	public static String getTempSubDir(){
		return getTempSubDir(TMP_DIR);
	}
	
	public static String getTempSubDir(String s){
		String dir = s+"/"+TEMP_PREFIX+(new Random()).nextInt()+"/";
		File f = new File(dir);
		if(f.mkdirs())
			f.deleteOnExit();
		return dir;
	}

	public static InputStream[] getMainInputStreams(CommandLine cl) throws IOException{
		return getInputStreams(cl, "i");
	}
	
	public static InputStream[] getInputStreams(CommandLine cl, String inputPrefix) throws IOException{
		ArrayList<InputStream> iss = new ArrayList<InputStream>();
		
		String[] inputs = cl.getOptionValues(inputPrefix);
		
		boolean gz = false;
		if(cl.hasOption(inputPrefix+"gz")){
			gz = true;
			_log.info("All '"+inputPrefix+"' inputs are gzipped");
		}
		
		for(String input:inputs){
			File f = new File(input);
			if(!f.exists()){
				throw new IOException("Cannot find file "+input);
			}
			if(f.isFile()){
				InputStream is = new FileInputStream(f);
				if(gz){
					is = new GZIPInputStream(is);
				}
				_log.info("Adding input "+f.getName());
				iss.add(is);
			} else if(f.isDirectory()){
				addInputStreams(f, iss, gz);
			} else{
				throw new IOException("Not a file or directory: "+input);
			}
		}
		
		InputStream[] isa = new InputStream[iss.size()];
		iss.toArray(isa);
		return isa;
	}
	
	private static void addInputStreams(File dir, Collection<InputStream> iss, boolean gz) throws IOException{
		for(File f:dir.listFiles()){
			if(f.isFile()){
				InputStream is = new FileInputStream(f);
				if(gz){
					is = new GZIPInputStream(is);
				}
				_log.info("Adding input "+f.getName());
				iss.add(is);
			}
		}
	}
	
	public static InputStream getMainInputStream(CommandLine cl) throws IOException{
		return getInputStream(cl, "i");
	}
	
	public static InputStream getInputStream(CommandLine cl, String inputPrefix) throws IOException{
		InputStream is = null;
		
		String input = cl.getOptionValue(inputPrefix);
		
		if(input==null || input.equals("-")){
			is = System.in;
			_log.info("Input '"+inputPrefix+"' is stdin");
		} else {
			is = new FileInputStream(input);
			_log.info("Input '"+inputPrefix+"' is "+input);
		}
		
		if(cl.hasOption(inputPrefix+"gz")){
			_log.info("Input '"+inputPrefix+"' is gzipped");
			is = new GZIPInputStream(is);
		}
		
		return is;
	}
	
	public static int getTicks(CommandLine cl) throws IOException{
		if(cl.hasOption("ticks")){
			int t = Integer.parseInt(cl.getOptionValue("ticks"));
			_log.info("Ticks set to "+t);
			return t;
		}
		return 0;
	}
	
	public static OutputStream getOutputStream(CommandLine cl, String outputPrefix) throws IOException{
		OutputStream os = null;
		
		String output = cl.getOptionValue(outputPrefix);
		
		if(output==null || output.equals("-")){
			os = System.out;
			_log.info("Output '"+outputPrefix+"' is stdout");
		} else {
			os = new FileOutputStream(output);
			_log.info("Output '"+outputPrefix+"' is "+output);
		}
		
		if(cl.hasOption(outputPrefix+"gz")){
			os = new GZIPOutputStream(os);
			_log.info("Output '"+outputPrefix+"' is gzipped");
		}
		
		return os;
	}
	
	public static OutputStream getMainOutputStream(CommandLine cl) throws IOException{
		return getOutputStream(cl, "o");
	}
	
	public static void addOutputOption(Options o, String argPrefix, String name){
		Option outputO = new Option(argPrefix, (name+" output file, - for stdout").trim());
		outputO.setArgs(1);
		
		Option outfO = new Option(argPrefix+"gz", ("gzip output "+name).trim());
		outfO.setArgs(0);
		
		o.addOption(outputO);
		o.addOption(outfO);
	}
	
	public static void addInputOption(Options o, String argPrefix, String name){
		Option inputO = new Option(argPrefix, (name+" input files (all files in all dirs will be considered)").trim());
		inputO.setArgs(1);
		
		Option inputfO = new Option(argPrefix+"gz", ("gzipped input "+name).trim());
		inputfO.setArgs(0);
		
		o.addOption(inputO);
		o.addOption(inputfO);
	}
	
	public static void addInputsOption(Options o, String argPrefix, String name){
		Option inputO = new Option(argPrefix, (name+" input file, - for stdin").trim());
		inputO.setArgs(Option.UNLIMITED_VALUES);
		
		Option inputfO = new Option(argPrefix+"gz", ("*all* gzipped input "+name).trim());
		inputfO.setArgs(0);
		
		o.addOption(inputO);
		o.addOption(inputfO);
	}
	
	public static void addTicksOption(Options os){
		Option ticksO = new Option("ticks", "ticks for logging, positive int value");
		ticksO.setArgs(1);
		os.addOption(ticksO);
	}
	
	public static void addHelpOption(Options os){
		Option helpO = new Option("h", "help");
		helpO.setArgs(0);
		os.addOption(helpO);
	}
	
	public static Options getStandardOptions(){
		Options os = new Options();
		
		addInputOption(os, "i", "");
	
		addOutputOption(os, "o", "");
		
		addTicksOption(os);
		
		addHelpOption(os);
		
		return os;
	}
	
	
}

