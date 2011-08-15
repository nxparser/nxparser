package org.semanticweb.yars2.rdfxml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.util.CallbackBlockingQueue;
import org.semanticweb.yars.util.CallbackNxOutputStream;
import org.xml.sax.SAXException;

/**
 * RDFXMLParser... for... you guessed it... parsing RDF/XML
 * Based on SAXParser. Default behaviour creates a parsing thread
 * which fills a BlockingQueue and is consumed externally through the 
 * iterator model.
 * 
 * Can use custom CallBack which will not use a seperate thread for 
 * parsing and which does not use iterator methods.
 * 
 * @author aidhog
 *
 */
public class RDFXMLParser implements Iterator<Node[]> {
	private BlockingQueue<Node[]> _q = null;
	private boolean _done = false;
	private Exception _e = null;
	private ParserThread _pt = null;
	private Node[] _current = null;
	private Resource _con = null;
	
	public static final int DEFAULT_BUFFER = 1000;
	public static final int TIME_OUT = 1000; //1 sec
	
//	private static final Header[] _headers = {
//		new Header("Accept", "application/rdf+xml"),
//		new Header("User-Agent", "nxparser/java"),	
//	};
	
	private SAXParser _parser;
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		try {
			_parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Short default constructor.
	 * 
	 */
	public RDFXMLParser(InputStream in, String baseURI) throws ParseException, IOException {
		this(in, false, true, baseURI, DEFAULT_BUFFER);
	}
	
	/**
	 * Short default constructor.
	 * 
	 */
	public RDFXMLParser(InputStream in, boolean strict, boolean skolemise, String baseURI) throws ParseException, IOException {
		this(in, strict, skolemise, baseURI, DEFAULT_BUFFER);
	}
	
	/**
	 * Default constructor. Creates a BlockingCallBack instance, whose buffer is filled
	 * by a parser thread and consumed by this instance using the iterator model.
	 */
	public RDFXMLParser(InputStream in, boolean strict, boolean skolemise, String baseURI, int buffer) throws ParseException, IOException {
//		SAXParserFactory factory = SAXParserFactory.newInstance();
//		factory.setNamespaceAware(true);
//		factory.setValidating(strict);
		try {
//			SAXParser saxParser = factory.newSAXParser();
			_q = new ArrayBlockingQueue<Node[]>(buffer);
			CallbackBlockingQueue bcb = new CallbackBlockingQueue(_q);
			_pt = new ParserThread(_parser, in, new RDFXMLParserBase(baseURI, bcb, skolemise),_q);
			_pt.start();
		} catch (Throwable err) {
			err.printStackTrace ();
		}
	}
	
	/**
	 * Constructor given Callback. 
	 * Interaction outside of iterator model but analagous to NxParser :(.
	 * 
	 */
	public RDFXMLParser(InputStream in, boolean strict, boolean skolemise, String baseURI, Callback c) throws ParseException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(strict);
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new RDFXMLParserBase(baseURI, c, skolemise));
		} catch (Throwable err) {
			err.printStackTrace ();
		}
	}
	
	/**
	 * Constructor given Callback. 
	 * Interaction outside of iterator model but analagous to NxParser :(.
	 * 
	 */
	public RDFXMLParser(InputStream in, boolean strict, boolean skolemise, String baseURI, Callback c, Resource con) throws ParseException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(strict);
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new RDFXMLParserBase(baseURI, c, skolemise, con));
		} catch (Throwable err) {
			err.printStackTrace ();
		}
	}
	
	/**
	 * Constructor given Callback and URL. 
	 * Interaction outside of iterator model but analogous to NxParser :(.
	 * 
	 */
//	public RDFXMLParser(URL u, boolean strict, boolean skolemise, Callback c) throws ParseException, IOException {
//		this(u, strict, skolemise,u.toString(), c, false, TIME_OUT);
//	}
//
//	/**
//	* Constructor given Callback and URL. 
//	* Interaction outside of iterator model but analogous to NxParser :(.
//	* 
//	*/
//	public RDFXMLParser(URL u, boolean strict, boolean skolemise, String baseURI, Callback c, boolean writeQuads) throws ParseException, IOException {
//		this(u, strict, skolemise,u.toString(), c, writeQuads, TIME_OUT);
//	}
	
//	/**
//	 * Constructor given Callback and URL if baseURI different from URL. 
//	 * Interaction outside of iterator model but analogous to NxParser :(.
//	 * @throws SAXException 
//	 * @throws ParserConfigurationException 
//	 * 
//	 * @TODO doesn't parse my bloody file, UTF error
//	 */
//	public RDFXMLParser(URL u, boolean strict, boolean skolemise, String baseURI, Callback c, boolean writeQuads, int timeOut) throws ParseException, IOException {
//		this(u, strict, skolemise, baseURI, c, writeQuads, timeOut, Long.MAX_VALUE);
//	}
	
//	/**
//	 * Constructor given Callback and URL if baseURI different from URL. 
//	 * Interaction outside of iterator model but analogous to NxParser :(.
//	 * @throws SAXException 
//	 * @throws ParserConfigurationException 
//	 * 
//	 * @TODO doesn't parse my bloody file, UTF error
//	 */
//	public RDFXMLParser(URL u, boolean strict, boolean skolemise, String baseURI, Callback c, boolean writeQuads, int timeOut, long maxLength) throws ParseException, IOException {
//		//took Andreas' code from NxParser
//		//could use saxParer.parse(String url, DefaultHandler blah)
//		
//		_con = new Resource(u.toString());
//		HttpClient http = new HttpClient();
//
//		if (System.getProperty("http.proxyHost") != null && System.getProperty("http.proxyPort") != null) {
//			HostConfiguration hc = http.getHostConfiguration();
//			hc.setProxy(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")));
//		}
//		
//		http.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
//		http.getHttpConnectionManager().getParams().setSoTimeout(timeOut);
//
//		GetMethod gm = new GetMethod(u.toString());
//		InputStream is = null;
//		int status = -1;
//		boolean rdfxml = false;
//		
//		for (Header h: _headers) {
//			gm.setRequestHeader(h);
//		}
//		
////		try {
//		http.executeMethod(gm);
//
//		String furi = gm.getURI().toString();
//		if(baseURI==null)
//			baseURI = furi;
//		_con = new Resource(gm.getURI().toString());
//
//		status = gm.getStatusCode();
//
//		Header[] head = gm.getResponseHeaders("Content-Type");
//		for (Header h : head) {
//			String tmp = h.getValue();
//			if (tmp.contains("application/rdf+xml")) {
//				rdfxml = true;
//			}
//		}
//		
//		Header[] length = gm.getResponseHeaders("Content-Length");
//		if(length!=null) for (Header h : length) {
//			String tmp = h.getValue();
//			try{
//				long len = Long.parseLong(tmp);
//				if(len>maxLength){
//					throw new IOException("Content-Length "+tmp+" exceeds internal max specified length of "+maxLength+".");
//				}
//			}catch(NumberFormatException nfe){
//				;
//			}
//		}
//
//		if (status == 200 && rdfxml) {
//			is = gm.getResponseBodyAsStream();
//			SAXParserFactory factory = SAXParserFactory.newInstance();
//			factory.setNamespaceAware(true);
//			factory.setValidating(strict);
//
//			try {
//				SAXParser saxParser = factory.newSAXParser();
//				saxParser.parse(is, new RDFXMLParserBase(baseURI, c, skolemise, strict, _con));
//			} catch (ParserConfigurationException e1) {
//				throw new RuntimeException(e1.getLocalizedMessage());
//			} catch (SAXParseException e1) {
//				throw new ParseException(e1.getMessage()+" Line "+e1.getLineNumber()+" column "+e1.getColumnNumber()+".");
//			} catch (SAXException e1) {
//				throw new ParseException(e1);
//			}
//		} else{ 
//			gm.releaseConnection();
//			if(status!=200)
//				throw new IOException(u+" returned response code " + status + " " + gm.getStatusLine());
//			else if(!rdfxml)
//				throw new IOException(u+" did not return Content-Type application/rdf+xml");
//		}
//			
//			
////		} 
////		catch (Exception ex) {
////			e = ex;
////			ex.printStackTrace ();
////		} finally {
////			try {
////				if (is != null) {
////					is.close();
////				}
////			} catch (IOException ex) {
////				ex.printStackTrace();
////			}
////
////			if (status != 200 && status != 204) {
////				gm.releaseConnection();
////				throw new IOException("returned response code " + status + " " + gm.getStatusLine());
////			}
////			
////			if (!rdfxml) {
////				gm.releaseConnection();
////				throw new IOException("not rdfxml");				
////			}
////			
////			if(strict)
////			
////			gm.releaseConnection();
////		}
//		
//	}
	
	public Resource getContext(){
		return _con;
	}
	
	public boolean hasNext() {
		if(_q==null){
			return false;
		} else if(_done){
			return false;
		} else if(_current!=null){
			//if(NodeComparator.NC.equals(_current,BlockingCallBack.POISON_TOKEN)){
			//faster hack :( :)
			if(_current.length==0){
				_done = true;
				_e = _pt.getException();
				return false;
			}
			else return true;
		} else if(_q.size()>0){
			_current = _q.poll();
			return hasNext();
		} else{
			try {
				_current = _q.poll(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				_done = true;
				return false;
			}
			return hasNext();
		}
	}

	public Node[] next() {
		if(_current==null){
			if(!hasNext()){
				throw new NoSuchElementException();
			}
		}
		
		Node[] result = new Node[_current.length];
		System.arraycopy(_current, 0, result, 0, _current.length);
		_current = null;
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public boolean isSuccess(){
		return _e == null;
	}
	
	public Exception getException(){
		return _e;
	}
	
//	public static void main(String args[]) throws FileNotFoundException, ParseException, IOException{
//		String in = "../saor0.0.1/test/data/lubm/University0_0.owl";
//		String baseURI = "http://swat.cse.lehigh.edu/projects/lubm/University0_0.owl";
//		String out = "../saor0.0.1/test/data/lubm/University0_0.nt";
//		
//		FileInputStream fis = new FileInputStream(in);
//		FileOutputStream fos = new FileOutputStream(out);
//		CallbackNxOutputStream cb = new CallbackNxOutputStream(fos);
//		
//		new RDFXMLParser(fis, false, false, baseURI, cb);
//		
//		fis.close();
//		fos.close();
//	}
	
	public static void main(String args[]) throws FileNotFoundException, ParseException, IOException{
		String in = "test-data/rdfxmlthis.rdf";
		String baseURI = "http://prefix.cc/popular/all.file.vann";
//		String out = "../saor0.0.1/test/data/lubm/University0_0.nt";
		
		FileInputStream fis = new FileInputStream(in);
		CallbackNxOutputStream cb = new CallbackNxOutputStream(System.out);
		
		new RDFXMLParser(fis, false, false, baseURI, cb);
		
		fis.close();
		System.out.flush();
	}
}
