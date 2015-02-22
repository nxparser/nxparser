package org.semanticweb.yars.nx.clean;

import java.io.PrintStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.datetime.Iso8601Parser;
import org.semanticweb.yars.nx.dt.datetime.XsdDateTime;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.nx.util.NxUtil;

public class Cleaner {
	static SimpleDateFormat _iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	static SimpleDateFormat[] _formats = {
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
		new SimpleDateFormat("dd-MMM-yy"),
		new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss"),
		new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z"),
		new SimpleDateFormat("MM/dd/yy")
	};

	public static void clean(Iterator<Node[]> in, PrintStream out, int length, boolean datatype) {
		int drop = 0;
		long linecount = 0;
		
		while(in.hasNext()){
			boolean write = true;
			Node[] line = in.next();
			linecount++;
			if (length != -1 && length != line.length) {
				System.err.println(linecount + ": doesn't have " + length + " elements but " + line.length);
				drop++;
				continue;
			}
			Node[] clean = new Node[line.length];

			for(int i=0; i<line.length; i++){
				try {
					clean[i] = clean(line[i], datatype);
				} catch (Exception e) {
					drop++;
					write = false;
					System.err.println(linecount + ": cannot parse entry "+line[i] +  " " + e.getMessage());
				}
			}
			if(write) {
				for (Node n:clean) {
					out.print(n.toString()+" ");
				}
				out.println(".");
			}
		}
		
		System.err.println("Processed  "+linecount+" statements");
		
		System.err.println("Dropped "+drop+" statements");
	}
	
	public static Node clean(Node raw, boolean datatype) throws URISyntaxException, MalformedURLException {
		if(raw instanceof Resource) {
			return new Resource(normaliseURI(raw.toString()));
		} else if(raw instanceof Literal){
			Literal l = (Literal) raw;

			String data = removeControlChars(l.toString());
			data = stripHTML(data);
			data = data.trim();

			// dropping empty literals
			if (data.length() <= 0) {
				throw new URISyntaxException("", "dropping emtpy literals");
			}

			data = NxUtil.escapeLiteral(data);

			if(l.getDatatype()==null && l.getLanguageTag()==null)
				return normaliseLiteral(new Literal(data), datatype);
			else if(l.getDatatype()!=null && l.getLanguageTag()!=null)
				return new Literal(data, l.getLanguageTag(), l.getDatatype());
			else if(l.getDatatype()!=null)
				return new Literal(data, l.getDatatype());
			else
				return new Literal(data, l.getLanguageTag());

		} else {
			return raw;
		}
	}

	public static Literal normaliseLiteral(Literal l, boolean datatype) throws URISyntaxException {
		// we know language or datatype already? -> skip
		if (l.getLanguageTag() != null || l.getDatatype() != null) {
			String data = l.getLabel().trim();
			
			Literal n = null;
			
			if (l.getLanguageTag() != null) {
				n = new Literal(data, l.getLanguageTag());
			} else {
				n = new Literal(data, l.getDatatype());
			}

			return n;
		}

		// no datatype normalisation
		if (datatype == false) {
			String data = l.getLabel().trim();
			
			Literal n = null;
			
			if (l.getLanguageTag() != null) {
				n = new Literal(data, l.getLanguageTag());
			} else {
				n = new Literal(data, l.getDatatype());
			}

			return n;
		}
		
		Literal n = null;
		String data = l.getLabel().trim();
		
		Pattern numex = Pattern.compile("^[-+]?[0-9]*.?[0-9]+$");
	
		// we have a number
		/*
		if (numex.matcher(data).matches() == true) {
			NumberFormat nf = NumberFormat.getInstance();
			try {
				Number num = nf.parse(data);
				if (num instanceof Integer || num instanceof BigInteger ||
						num instanceof Short || num instanceof Long ||
						num instanceof Byte) {
					n = new Literal(num.toString(), Literal.DECIMAL);
				} else if (num instanceof Float) {
					n = new Literal(num.toString(), Literal.FLOAT);				
				} else if (num instanceof Double) {
					n = new Literal(num.toString(), Literal.DOUBLE);				
				} else {
					throw new URISyntaxException("", "dropping bogus number literal");
				}
			} catch (java.text.ParseException e) {
				;
			} catch (NumberFormatException nfe) {
				;
			}
		} else {
		*/
			// try date
			Date date = null;
			
			try {
				XsdDateTime xdt = new XsdDateTime(data);
				if (xdt.getValue() != null) {
					return new Literal(data,XsdDateTime.DT);
				}
			} catch (ParseException nfe) {
				;
			} catch (Throwable e) {
				;
			}
			
			for (SimpleDateFormat f:_formats) {
				try {
					date = (Date)f.parse(data);
					XsdDateTime xdt = new XsdDateTime(_iso.format(date));
					if (xdt.getValue() != null) {
						break;
					}
				} catch (java.text.ParseException pe) {
					;
				} catch (Throwable e) {
					;
				}
			}
//		}
		
        if (n != null) {
        	return n;
        }
        return l;
	}
	

	/**
	 * Uses some heurisitics to clean URIs
	 * e.g. http://en.wikipedia.org/wiki/URL_normalization
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException 
	 * @throws MalformedURLException 
	 */
	public static String normaliseURI(String uri) throws URISyntaxException, MalformedURLException {
		URI raw = new URI(uri.replaceAll(" ", "%20"));
		
		raw = raw.normalize();
		
		//raw.toURL();

		if (!raw.isOpaque()) {
			String scheme = raw.getScheme();
			if (scheme != null) {
				scheme = scheme.toLowerCase();
			} else {
				throw new URISyntaxException(uri, "dropping uris without scheme");
			}
			
			if (scheme.startsWith("http")) {
				raw.toURL();
			}
			
			String path = raw.getPath();
			if (path != null) {
				// fucks up foaf uris
				//path = path.toLowerCase();
				
				if (path.equals("")) {
					path = "/";
				} else if (path.endsWith("/index.html")
						|| path.endsWith("/index.htm")
						|| path.endsWith("/index.asp")
						|| path.toLowerCase().endsWith("/default.asp")
						|| path.toLowerCase().endsWith("/default.aspx")
						|| path.endsWith("/index.jsp")
						|| path.endsWith("/index.php") ) {
					path = path.substring(0, path.lastIndexOf('/')+1);
				}
			}

			String host = raw.getHost();
			if (host != null) {
				host = host.toLowerCase();
				//if (host.startsWith("www.")) {
				//	host = host.substring(4, host.length());
				//}
			}

			int port = raw.getPort();
			if(port==80) {
				port = -1;
			}

			URI u = new URI(scheme, raw.getUserInfo(), host, port, path, raw.getQuery(), raw.getFragment());

			return u.toString();
		}
		
		// if there's no : (and hence no scheme), skip
		String scheme = raw.getScheme();
		//System.out.println("schem" + scheme);
		if (scheme == null) {
			/*
			scheme = scheme.toLowerCase();
		}
		if (uri.indexOf(':') == -1 || uri.indexOf(':') > 7) {
		*/
			throw new URISyntaxException("", "dropping uris without scheme");
		}

		return uri;
	}

	private static String removeControlChars(String lit) {
		StringBuffer result = new StringBuffer();
		
		for (int i = 0; i < lit.length(); i++) {
			char c = lit.charAt(i);
			int cInt = (int)c;
			if (cInt >= 0 && cInt <= 31) {
				result.append(" ");
			} else {
				result.append(c);
			}
		}
		
		return result.toString();
	}
	
	private static String stripHTML(String literal){
		return literal.replaceAll("\\<.*?\\>", "");
		/*
		if (literal.indexOf(">") > 0) {
			StringBean sb = new StringBean();
			Parser parser = Parser.createParser(literal, "UTF-8");
			String result = literal;
			try {
				parser.visitAllNodesWith (sb);
				result = sb.getStrings ();
			} catch(EncodingChangeException eEx){
				String msg = eEx.getMessage();
				String enc = msg.substring(msg.indexOf(" to ")+4);
				enc = enc.substring(0, enc.indexOf(" "));
				System.err.println("Switch to "+enc+" encoding");
				try{
					parser = Parser.createParser(literal, enc);
					sb = new StringBean ();
					parser.visitAllNodesWith(sb);
					result = sb.getStrings();
				} catch(Exception ex){
					return result;
				}
			}catch (ParserException e) {
				e.printStackTrace(System.err);
			}
			return result;
		}
		return literal;
		*/
//		HTMLTextExtractor parser = new HTMLTextExtractor();
//		try {
//			parser.parse(new StringReader(literal));
//		} catch (IOException e) {
//			e.printStackTrace();
//			return literal;
//		} catch (NumberFormatException e) {
//			return literal;
//		}
//		return literal;
//		
	}
}
