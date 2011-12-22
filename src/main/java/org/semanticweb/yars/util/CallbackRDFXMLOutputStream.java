package org.semanticweb.yars.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackRDFXMLOutputStream implements Callback {
	private static Logger _log = Logger.getLogger(CallbackRDFXMLOutputStream.class.getName());
	
	OutputStream _out;

	long _cnt = 0;
	long _time, _time1;
	
	public final static String NEWLINE = System.getProperty("line.separator");
	
	public CallbackRDFXMLOutputStream(OutputStream out) {
		_out = out;
	}

	public void startDocument() {
		_time = System.currentTimeMillis();
		
		try {
			_out.write("<?xml version='1.0' encoding='utf-8'?>\n\n".getBytes("utf-8"));
			_out.write("<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>\n".getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public synchronized void processStatement(Node[] nx) {
		if (nx.length < 3) {
			_log.warning("nx length has to be at least 3!, dropping");
			return;
		}
		if (!(nx[1] instanceof Resource)) {
			_log.warning("predicate must be resource, is " + nx[1] + ", dropping");
			return;
		}
		
		String r = nx[1].toString();
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
			_log.warning("couldn't separate namespace and localname " + r);
			return;
		}

		StringBuffer sb = new StringBuffer();

		sb.append("<rdf:Description ");

		if (nx[0] instanceof Resource) {
			sb.append(" rdf:about='" + nx[0].toString() + "'>");
		} else if (nx[0] instanceof BNode) {
			sb.append(" rdf:nodeID='" + nx[0].toString() + "'>\n");			
		}
		sb.append("\t<" + localname + " xmlns='" + namespace + "'");

		if (nx[2] instanceof BNode) {
			sb.append(" rdf:nodeID='" + nx[2].toString() + "'/>\n");
		} else if (nx[2] instanceof Resource) {
			sb.append(" rdf:resource='" + escape(nx[2].toString()) + "'/>\n");				
		} else if (nx[2] instanceof Literal) {
			Literal l = (Literal)nx[2];
			if (l.getLanguageTag() != null) {
				sb.append(" xml:lang='" + l.getLanguageTag() + "'");
			} else if (l.getDatatype() != null) {
				sb.append(" rdf:datatype='" + l.getDatatype().toString() + "'");					
			}
			sb.append(">" + escape(l.toString()) + "</" + localname + ">\n");
		}
			
		sb.append("</rdf:Description>\n");
		try {
			_out.write(sb.toString().getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		_cnt++;
	}

	public void endDocument() {
		try {
			_out.write("</rdf:RDF>\n".getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		try {
			_out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		_time1 = System.currentTimeMillis();
	}
	
	public String toString() {
		return _cnt + " tuples in " + (_time1-_time) + " ms";
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
