package org.semanticweb.yars.rdfxml;

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
import org.semanticweb.yars.nx.util.ParseException;
import org.xml.sax.SAXException;

/**
 * RdfXmlParser... for... you guessed it... parsing RDF/XML
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
public class RdfXmlParser implements Iterator<Node[]>, Iterable<Node[]> {
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
	public Iterator<Node[]> parse(InputStream in, String baseURI) throws ParseException, IOException {
		return parse(in, false, true, baseURI, DEFAULT_BUFFER);
	}
	
	/**
	 * Short default constructor.
	 * 
	 */
	public Iterator<Node[]> parse(InputStream in, boolean strict, boolean skolemise, String baseURI) throws ParseException, IOException {
		return parse(in, strict, skolemise, baseURI, DEFAULT_BUFFER);
	}
	
	/**
	 * Default constructor. Creates a BlockingCallBack instance, whose buffer is filled
	 * by a parser thread and consumed by this instance using the iterator model.
	 */
	public Iterator<Node[]> parse(InputStream in, boolean strict, boolean skolemise, String baseURI, int buffer) throws ParseException, IOException {
//		SAXParserFactory factory = SAXParserFactory.newInstance();
//		factory.setNamespaceAware(true);
//		factory.setValidating(strict);
		try {
//			SAXParser saxParser = factory.newSAXParser();
			_q = new ArrayBlockingQueue<Node[]>(buffer);
			CallbackBlockingQueue bcb = new CallbackBlockingQueue(_q);
			_pt = new ParserThread(_parser, in, new RdfXmlParserBase(baseURI, bcb, skolemise),_q);
			_pt.start();
		} catch (Exception err) {
			throw new ParseException(err);
//			err.printStackTrace ();
		}
		
		return this;
	}
	
	/**
	 * Constructor given Callback. 
	 * Interaction outside of iterator model but analagous to NxParser :(.
	 * 
	 * @@@ need always context for relative URIs?
	 */
	@Deprecated
	public void parse(InputStream in, boolean strict, boolean skolemise, String baseURI, Callback c) throws ParseException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(strict);
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new RdfXmlParserBase(baseURI, c, skolemise));
		} catch (Exception err) {
			throw new ParseException(err);
//			err.printStackTrace ();
		}
	}
	
	/**
	 * Constructor given Callback. 
	 * Interaction outside of iterator model but analagous to NxParser :(.
	 * Produces quadruples using the provided context.
	 * 
	 */
	public void parse(InputStream in, boolean strict, boolean skolemise, String baseURI, Callback c, Resource con) throws ParseException, IOException {
		_con = con;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(strict);
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new RdfXmlParserBase(baseURI, c, skolemise, con));
		} catch (Exception err) {
			throw new ParseException(err);
//			err.printStackTrace ();
		}
	}
	
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

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}
}
