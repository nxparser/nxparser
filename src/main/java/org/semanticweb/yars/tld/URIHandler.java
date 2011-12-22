/**
 * 
 */
package org.semanticweb.yars.tld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * @author juergen
 *
 */
public class URIHandler implements Comparator<String>{
	final private static String NO_HOST = "no_host";
	final private static Runtime runtime = Runtime.getRuntime();
	private final static NumberFormat FMT = NumberFormat.getInstance();
	static {
		FMT.setMaximumFractionDigits(2);
		FMT.setMinimumFractionDigits(2);
	}
//	private static final String tldFile = ;

	private static Set<String> noTLDSet = new HashSet<String>();
	private static Set<String> tldSet = new HashSet<String>();
	private static Set<String> wildcardTLDSet = new HashSet<String>();
	private static TldManager _tldManager;
	static {
		try {
			parseTLD();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * @param string
	 */
	public static void logMem(String msg) {
		long mem = runtime.maxMemory() - runtime.totalMemory();
		float KBytes = mem / (float) 1024;
		float MBytes = KBytes / 1024;
		float GBytes = MBytes / 1024;
		if (GBytes >= 1)
			System.err.println("[MEMLOG] ("+msg+") FreeMemory: " + FMT.format(GBytes) + " GB");
		else if (MBytes >= 1) {
			System.err.println("[MEMLOG] ("+msg+") FreeMemory: " + FMT.format(MBytes) + " MB");
		} else
			System.err.println("[MEMLOG] ("+msg+") FreeMemory: " + FMT.format(KBytes) + " KB");
	
	}
	/**
	 * @return
	 * @throws IOException 
	 */
	private static void parseTLD() throws IOException {
		_tldManager = new TldManager();
		
//		if(!new File(tldFile).exists())
//			throw new FileNotFoundException("WARNING:::TLD file is missing!!");
//		_tldManager.readList(URIHandler.class.getResourceAsStream("tld.dat"));
	}
	/**
	 * @param node
	 * @return
	 */
	public static String getPLD(String uri) {
		URI tmp = null;
		try {
			tmp = new URI(uri);
		} catch (Exception e) {
			System.err.println("[ERROR] malformed url "+e.getMessage());
			return NO_HOST;
		}
		return _tldManager.getPLD(tmp);
	}
	
	public static String getPLD(URI uri) {
		String pld = _tldManager.getPLD(uri);
		if(pld==null) return NO_HOST;
		return pld;
	}
	/**
	 * @param uri
	 * @return
	 */
	public static String[] getPathTokens(String uri) {
		List<String> result = new ArrayList<String>();
		List<String> tokens = new ArrayList<String>();
		String path = "";
		try {
			URI tmp = new URI(uri.toString());
			path = tmp.getPath();
		} catch (URISyntaxException e) {
			URL url;
			try {
				url = new URL(uri.toString());
				path = url.getPath();
			} catch (MalformedURLException eURL) {
			;
			}
		}
		if(path == null) {path = "";}
		
		String s;
		tokens = Arrays.asList(path.split("/"));
		for(String token: tokens) {
			s = token.trim();
			if(s.length()!=0) {
				s = s.replaceAll("\\{","").replaceAll("\\}", "")
				.replaceAll("\"", "").replaceAll(",", "")
				.replaceAll(" ", "_").replaceAll("'", "").replaceAll("%", "")
				.replaceAll("\n", "\\\n");
				
				if(path.contains(token+"/")) {
					s ="/"+s+"/";
				}
				else { 
					if(s.contains(".")){
						s = "/"+s.substring(0,s.lastIndexOf("."));}
					else
						s ="/"+s;
				}
				if(!result.contains(s))
					result.add(s);
			}
		}

		if(path.endsWith("/") || path.trim().length()==0) {
//			result.add("/NO_FNAME");
		}
		String [] tmp = new String[result.size()];
		
		return result.toArray(tmp);
	}
	
	public static String[] getPathTokens(URI uri) {
		List<String> result = new ArrayList<String>();
		String path = uri.getPath();
		if(path == null) {path = "";}
		String s;
		String [] tokens = path.split("/");
		for(String token: tokens) {
			s = token.trim();
			if(s.length()!=0) {
				s = s.replaceAll("\\{","").replaceAll("\\}", "")
				.replaceAll("\"", "").replaceAll(",", "")
				.replaceAll(" ", "_").replaceAll("'", "").replaceAll("%", "")
				.replaceAll("\n", "\\\n");
				
				if(path.contains(token+"/")) {
					s ="/"+s+"/";
				}
				else { 
					if(s.contains(".")){
						s = "/"+s.substring(0,s.lastIndexOf("."));}
					else
						s ="/"+s;
				}
				if(!result.contains(s))
					result.add(s);
			}
		}

//		if(path.endsWith("/") || path.trim().length()==0) {
////			result.add("/NO_FNAME");
//		}
		String [] tmp = new String[result.size()];
		return result.toArray(tmp);
	}
	
	/**
	 * @param uri
	 * @return
	 */
	public static String getFileName(String uri) {
		String path = "";
		try {
			URI tmp = new URI(uri.toString());
			path = tmp.getPath();
		} catch (URISyntaxException e) {
			URL url;
			try {
				url = new URL(uri.toString());
				path = url.getPath();
			} catch (MalformedURLException eURL) {
				;
			}
		}
//		if(path == null) return "/NO_FNAME";
		if(path == null) return "/NO_FNAME";
		File file = new File(path);
		String fName = file.getName();
		if(fName.endsWith("/")) return "/NO_FNAME";
		if(fName.contains(".")) {
			fName = fName.substring(0, fName.lastIndexOf("."));
		}
		fName = fName.replaceAll("\\{","").replaceAll("\\}", "")
			.replaceAll("\"", "").replaceAll(",", "")
			.replaceAll(" ", "_").replaceAll("'", "").replaceAll("%", "");
		if(fName.trim().length()==0) {
			return "/NO_FNAME";
//			return "";
		}
		return "/"+fName;
	}
	/**
	 * @param uri
	 * @return
	 */
	public static String getFileExtension(String uri) {
		String ext = "";
		String path = "";
		try {
			URI tmp = new URI(uri.toString());
			path = tmp.getPath();
		} catch (URISyntaxException e) {
			URL url;
			try {
				url = new URL(uri.toString());
				path = url.getPath();
			} catch (MalformedURLException eURL) {
				;
			}
		}
//		if(path == null) return ".NOEXT";
		if(path == null) return ".NOEXT";
		File file = new File(path);
		if(file.getName().endsWith("/"))return ".NOEXT";
		int extIndex = file.getName().lastIndexOf(".");
		if(extIndex>-1){
			ext  = file.getName().substring(extIndex).trim();
		}
		if(ext.length() == 0 
		   || ext.contains(",") || ext.contains("}")
		   || ext.contains("/") || ext.contains("{")
		   || ext.contains("=") || ext.contains("'")
		   || ext.contains("(") || ext.contains(")") 
		   || ext.contains(" ") )
//			return ".NOEXT";
			return ".NOEXT";
		return ext.trim().toLowerCase();
	}
	
	public static String getFileExtension(URI uri) {
		String ext = "";
		String path  = uri.getPath();
		if(path == null) return ".NOEXT";
		if(path.endsWith("/"))return ".NOEXT";
		int extIndex = path.lastIndexOf(".");
		if(extIndex>-1){
			ext  = path.substring(extIndex).trim();
		}
//		System.err.println(ext.matches("[,/=(){} ']"));
		if(ext.length() == 0 || ext.length() > 4
//		   || ext.contains(",") || ext.contains("}")
//		   || ext.contains("/") || ext.contains("{")
//		   || ext.contains("=") || ext.contains("'")
//		   || ext.contains("(") || ext.contains(")") 
//		   || ext.contains(" ") 
				)
			return ".NOEXT";
		return ext.trim().toLowerCase();
	}
	
	public static void main(String[] args) throws IOException {
		URIHandler handler = new URIHandler();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while((line=br.readLine())!=null) {
			System.out.println(handler.getPLD(line.trim()));
		}
		
	}
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String o1, String o2) {
		System.out.println("compare("+o1+","+o2+")");
		return o1.compareTo(o2);
	}
	/**
	 * @return
	 */
	public static long getFreeMem() {
		return runtime.maxMemory() - runtime.totalMemory();
	}
	/**
	 * @param line
	 * @return
	 */
	public String getDomain(String uri) {
		URL url;
		try {
			URI tmp = new URI(uri.toString());
			url = tmp.toURL();
		} catch (Exception e) {
			try {
				url = new URL(uri.toString());
			} catch (MalformedURLException eURL) {
				System.err.println("[ERROR] malformed url "+eURL.getMessage());
				return "NO_HOST";
			}
		}
		return url.getHost();
	}
}