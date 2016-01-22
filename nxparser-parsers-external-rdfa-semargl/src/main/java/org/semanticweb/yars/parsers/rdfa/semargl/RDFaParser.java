package org.semanticweb.yars.parsers.rdfa.semargl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ErrorHandler;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.rdfa.RdfaParser;
import org.semarglproject.sink.QuadSink;
import org.semarglproject.source.StreamProcessor;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Extracts RDFa from input streams / readers using semargl's RDFa parser to
 * NxParser's data model. Processes the input using TagSoup first in order to
 * deal with dirty HTML.
 *
 * @author Tobias Käfer
 * @author Leonard Lausen
 */
public class RDFaParser implements RdfParser {

	private static enum Contents {
		reader, inputstreamAndCharset, inputstream
	};

	final Object[] _inputData;
	final Contents _inputDataContents;
	final URI _baseURI;
	final String _baseURIstring;

	ErrorHandler _eh;

	public RDFaParser(InputStream is, URI baseURI) {
		_inputData = new Object[] { is };
		_inputDataContents = Contents.inputstream;
		_baseURI = baseURI;
		_baseURIstring = baseURI.toString();
	}

	public RDFaParser(InputStream is, Charset cs, URI baseURI) {
		_inputData = new Object[] { is, cs };
		_inputDataContents = Contents.inputstreamAndCharset;
		_baseURI = baseURI;
		_baseURIstring = baseURI.toString();
	}

	public RDFaParser(Reader r, URI baseURI) {
		_inputData = new Object[] { r };
		_inputDataContents = Contents.reader;
		_baseURI = baseURI;
		_baseURIstring = baseURI.toString();
	}

	public void parse(final Callback cb) {
		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(new QuadSink() {

			@Override
			public void addNonLiteral(String arg0, String arg1, String arg2) {
				cb.processStatement(new Node[] { createBnodeOrResource(arg0, _baseURIstring), new Resource(arg1),
						createBnodeOrResource(arg2, _baseURIstring), });
			}

			@Override
			public void addPlainLiteral(String arg0, String arg1, String arg2, String arg3) {
				cb.processStatement(new Node[] { createBnodeOrResource(arg0, _baseURIstring), new Resource(arg1),
						new Literal(arg2, arg3) });

			}

			@Override
			public void addTypedLiteral(String arg0, String arg1, String arg2, String arg3) {
				cb.processStatement(new Node[] { createBnodeOrResource(arg0, _baseURIstring), new Resource(arg1),
						new Literal(arg2, new Resource(arg3)) });
			}

			@Override
			public void endStream() throws ParseException {
				cb.endDocument();
			}

			@Override
			public void setBaseUri(String arg0) {
				return;
			}

			@Override
			public boolean setProperty(String arg0, Object arg1) {
				return false;
			}

			@Override
			public void startStream() throws ParseException {
				cb.startDocument();
			}

			@Override
			public void addNonLiteral(String arg0, String arg1, String arg2, String arg3) {
				cb.processStatement(new Node[] { createBnodeOrResource(arg0, arg3), new Resource(arg1),
						createBnodeOrResource(arg2, arg3), new Resource(arg3) });

			}

			@Override
			public void addPlainLiteral(String arg0, String arg1, String arg2, String arg3, String arg4) {
				cb.processStatement(new Node[] { createBnodeOrResource(arg0, arg4), new Resource(arg1),
						new Literal(arg2, arg3), new Resource(arg4) });

			}

			@Override
			public void addTypedLiteral(String arg0, String arg1, String arg2, String arg3, String arg4) {
				cb.processStatement(new Node[] { createBnodeOrResource(arg0, arg4), new Resource(arg1),
						new Literal(arg2, new Resource(arg3)), new Resource(arg4) });
			}
		}));

		// for cleaning the HTML
		XMLReader reader = null;
		try {
			reader = SAXParserImpl.newInstance(null).getXMLReader();
		} catch (SAXException e) {
			if (_eh != null) {
				_eh.fatalError(e);
				return;
			}
		}
		sp.setProperty(StreamProcessor.XML_READER_PROPERTY, reader);

		try {
			switch (_inputDataContents) {
			case reader:
				sp.process((Reader) _inputData[0], _baseURIstring);
				break;
			case inputstream:
				sp.process((InputStream) _inputData[0], _baseURIstring);
				break;
			case inputstreamAndCharset:
				sp.process(new InputStreamReader((InputStream) _inputData[0], (Charset) _inputData[1]), _baseURIstring);
				break;
			}
		} catch (ParseException e) {
			if (_eh != null) {
				_eh.warning(e);
			}
		}

	}

	private static Node createBnodeOrResource(String node, String context) {
		return node.startsWith("_:") ? BNode.createBNode(context, node.substring(3)) : new Resource(node);
	}

	@Override
	public void setErrorHandler(ErrorHandler eh) {
		_eh = eh;
	}

}
