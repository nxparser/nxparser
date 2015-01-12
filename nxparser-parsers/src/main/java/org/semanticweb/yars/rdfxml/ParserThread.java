package org.semanticweb.yars.rdfxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;

import org.semanticweb.yars.nx.Node;
import org.xml.sax.SAXException;

public class ParserThread extends Thread {
	private static Logger _log = Logger.getLogger(ParserThread.class.getName());

	private SAXParser _parser;
	private InputStream _in;
	private RdfXmlParserBase _rxpb;
	private Exception _e = null;
	private final BlockingQueue<Node[]>	_q;
	
	public ParserThread(final SAXParser parser, final InputStream in, final RdfXmlParserBase rxpb, final BlockingQueue<Node[]> bq){
		super();
		_q = bq;
		_parser = parser;
		_in = in;
		_rxpb = rxpb;
	}
	
	public void run(){
		try {
			_parser.parse(_in, _rxpb);
		} catch (SAXException e) {
			_log.severe(e.getMessage());
			_e = e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			_log.severe(e.getMessage());
			_e = e;
		}finally {
			try {
				_q.put(new Node[0]);
			} catch (InterruptedException e) {
                // "THIS HAPPENS NEVER EVER!! - HOPEFULLY"
				_log.severe(e.getMessage());
			}
		}
	}
	
	public Exception getException(){
		return _e;
	}
}
