package org.semanticweb.yars.turtle;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.rdfxml.CallbackBlockingQueue;

public class TurtleParser implements Iterable<Node[]>, Iterator<Node[]> {

	public TurtleParser() {
		;
	}

	TurtleParserInternal _tpi;

	BlockingDeque<Node[]> _dq;
	CallbackBlockingQueue _cb;

	public void parse(InputStream is, Charset cs, URI baseURI)
			throws TurtleParseException, ParseException {
		_tpi = new TurtleParserInternal(is, cs.name());
		init(baseURI);
	}

	public void parse(Reader r, URI baseURI)
			throws TurtleParseException, ParseException {
		_tpi = new TurtleParserInternal(r);
		init(baseURI);
	}

	private void init(URI baseURI) throws TurtleParseException, ParseException {
		_dq = new LinkedBlockingDeque<Node[]>();
		_cb = new CallbackBlockingQueue(_dq) {
			protected void endDocumentInternal() {
				;
			}
		};
		_tpi.parse(_cb, baseURI);
	}

	@Override
	public boolean hasNext() {
		return !_dq.isEmpty()
				|| _cb.documentIsStarted();
	}

	@Override
	public Node[] next() {
		return _dq.pop();
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
