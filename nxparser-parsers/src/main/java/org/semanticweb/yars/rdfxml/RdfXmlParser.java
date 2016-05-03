package org.semanticweb.yars.rdfxml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ErrorHandler;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.semanticweb.yars.nx.parser.InternalParserError;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * RdfXmlParser... for... you guessed it... parsing RDF/XML Based on SAXParser.
 * 
 * Implements the basic push RdfParser interface with a callback.
 * 
 * Does not create its own thread any more.
 * 
 * @author aidhog, aharth
 *
 */
public class RdfXmlParser implements RdfParser {

	private static final SAXParserFactory factory = SAXParserFactory.newInstance();

	static {
		factory.setNamespaceAware(true);
		factory.setValidating(false);
	}

	private final InputSource _is;
	private final URI _base;
	private ErrorHandler _eh;

	public RdfXmlParser(InputStream is, URI base) {
		_is = new InputSource(is);

		// InputSource's Javadoc recommends this, but seems to have no effect:
		_is.setSystemId(base.toString());

		_base = base;
	}

	public RdfXmlParser(Reader r, URI base) {
		_is = new InputSource(r);

		// InputSource's Javadoc recommends this, but seems to have no effect:
		_is.setSystemId(base.toString());

		_base = base;
	}

	public RdfXmlParser(InputStream is, Charset cs, URI base) {
		_is = new InputSource(is);

		_is.setEncoding(cs.name());

		// InputSource's Javadoc recommends this, but seems to have no effect:
		_is.setSystemId(base.toString());

		_base = base;
	}

	@Override
	public void setErrorHandler(ErrorHandler eh) {
		_eh = eh;
	}

	@Override
	public void parse(Callback cb) throws InterruptedException, InternalParserError, ParseException, IOException {

		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(_is, new RdfXmlParserBase(_base.toString(), cb));
		} catch (ParserConfigurationException e) {
			throw new InternalParserError(e);
		} catch (SAXException e) {
			throw new ParseException(e);
		}
	}
}