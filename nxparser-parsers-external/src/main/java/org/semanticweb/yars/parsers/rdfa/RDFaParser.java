package org.semanticweb.yars.parsers.rdfa;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
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
 */
public class RDFaParser implements Iterator<Node[]>, Iterable<Node[]> {

	StreamProcessor sp;

	BlockingDeque<Node[]> _deque = new LinkedBlockingDeque<Node[]>();

	boolean _streamOpen = true;

	public Iterator<Node[]> parse(Reader r, String baseURI)
			throws SAXException, ParseException {
		return parse(new BufferedReader(r), baseURI);
	}

	public Iterator<Node[]> parse(InputStream is, final String baseURI)
			throws SAXException, ParseException {
		return parse(new InputStreamReader(is), baseURI);
	}

	public Iterator<Node[]> parse(InputStream is, Charset cs,
			final String baseURI) throws ParseException, SAXException {
		return parse(new InputStreamReader(is, cs), baseURI);
	}

	public Iterator<Node[]> parse(BufferedReader br, String baseURI)
			throws SAXException, ParseException {
		StreamProcessor sp = createStreamProcessor(baseURI);
		sp.process(br, baseURI);
		return this;
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return _streamOpen || !_deque.isEmpty();
	}

	@Override
	public Node[] next() {
		try {
			return _deque.takeFirst();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return _deque.pop();
		}
	}

	private static Node createBnodeOrResource(String node, String context) {
		return node.startsWith("_:") ? BNode.createBNode(context,
				node.substring(3)) : new Resource(node);
	}

	private StreamProcessor createStreamProcessor(final String baseUri)
			throws SAXException {
		sp = new StreamProcessor(RdfaParser.connect(new QuadSink() {

			Resource _context = new Resource(baseUri);
			String _contextString = baseUri;

			@Override
			public void addNonLiteral(String arg0, String arg1, String arg2) {
				if (_contextString == null)
					throw new IllegalStateException("need context to work.");
				_deque.add(new Node[] {
						createBnodeOrResource(arg0, _contextString),
						new Resource(arg1),
						createBnodeOrResource(arg2, _contextString), _context });

			}

			@Override
			public void addPlainLiteral(String arg0, String arg1, String arg2,
					String arg3) {
				if (_contextString == null)
					throw new IllegalStateException("need context to work.");
				_deque.add(new Node[] {
						createBnodeOrResource(arg0, _contextString),
						new Resource(arg1), new Literal(arg2, arg3), _context });

			}

			@Override
			public void addTypedLiteral(String arg0, String arg1, String arg2,
					String arg3) {

				if (_contextString == null)
					throw new IllegalStateException("need context to work.");
				_deque.add(new Node[] {
						createBnodeOrResource(arg0, _contextString),
						new Resource(arg1),
						new Literal(arg2, new Resource(arg3)), _context });

			}

			@Override
			public void endStream() throws ParseException {
				_streamOpen = false;
			}

			@Override
			public void setBaseUri(String arg0) {
				_contextString = arg0;
				_context = new Resource(arg0);
			}

			@Override
			public boolean setProperty(String arg0, Object arg1) {
				return false;
			}

			@Override
			public void startStream() throws ParseException {
				_streamOpen = true;
			}

			@Override
			public void addNonLiteral(String arg0, String arg1, String arg2,
					String arg3) {
				_deque.add(new Node[] { createBnodeOrResource(arg0, arg3),
						new Resource(arg1), createBnodeOrResource(arg2, arg3),
						new Resource(arg3) });

			}

			@Override
			public void addPlainLiteral(String arg0, String arg1, String arg2,
					String arg3, String arg4) {
				_deque.add(new Node[] { createBnodeOrResource(arg0, arg4),
						new Resource(arg1), new Literal(arg2, arg3),
						new Resource(arg4) });

			}

			@Override
			public void addTypedLiteral(String arg0, String arg1, String arg2,
					String arg3, String arg4) {
				_deque.add(new Node[] { createBnodeOrResource(arg0, arg4),
						new Resource(arg1),
						new Literal(arg2, new Resource(arg3)),
						new Resource(arg4) });
			}
		}));

		XMLReader reader = SAXParserImpl.newInstance(null).getXMLReader();
		sp.setProperty(StreamProcessor.XML_READER_PROPERTY, reader);
		return sp;
	}

}
