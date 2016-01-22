package org.semanticweb.yars.rdfxml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ErrorHandler;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.xml.sax.SAXException;

/**
 * RdfXmlParser... for... you guessed it... parsing RDF/XML
 * Based on SAXParser.
 * 
 * Implements the basic push RdfParser interface with a callback.
 * 
 * Does not create its own thread any more.
 * 
 * @author aidhog, aharth
 *
 */
public class RdfXmlParser implements RdfParser {
	private final InputStream _is;
	private final URI _base;
	private ErrorHandler _eh;

	public RdfXmlParser(InputStream is, URI base) {
		_is = is;
		_base = base;
	}

	@Override
	public void setErrorHandler(ErrorHandler eh) {
		_eh = eh;
	}

	@Override
	public void parse(Callback cb) throws InterruptedException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(_is, new RdfXmlParserBase(_base.toString(), cb));
		} catch (ParserConfigurationException e) {
			if (_eh != null) {
				_eh.fatalError(e);
			}
		} catch (SAXException e) {
			if (_eh != null) {
				_eh.fatalError(e);
			}
		} catch (IOException e) {
			if (_eh != null) {
				_eh.fatalError(e);
			}
		}
	}
}