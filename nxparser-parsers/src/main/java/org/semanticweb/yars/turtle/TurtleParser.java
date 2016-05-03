package org.semanticweb.yars.turtle;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;

import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ErrorHandler;
import org.semanticweb.yars.nx.parser.RdfParser;

public class TurtleParser implements RdfParser {
	TurtleParserInternal _tpi;
	URI _base;
	ErrorHandler _eh;

	public TurtleParser(InputStream is, URI base) {
		_tpi = new TurtleParserInternal(is);
		_base = base;
	}

	public TurtleParser(InputStream is, Charset charset, URI base) {
		_tpi = new TurtleParserInternal(is, charset.name());
		_base = base;
	}

	public TurtleParser(Reader r, URI base) {
		_tpi = new TurtleParserInternal(r);
		_base = base;
	}

	@Override
	public void setErrorHandler(ErrorHandler eh) {
		_eh = eh;
	}

	@Override
	public void parse(Callback cb) throws InterruptedException, org.semanticweb.yars.nx.parser.ParseException {
		try {
			_tpi.parse(cb, _base);
		} catch (ParseException e) {
			throw new org.semanticweb.yars.nx.parser.ParseException(e);
		}
	}
}