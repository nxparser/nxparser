package org.semanticweb.yars.turtle;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

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

	public TurtleParser(InputStream is, String encoding, URI base) {
		_tpi = new TurtleParserInternal(is, encoding);
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
	public void parse(Callback cb) throws InterruptedException {
		try {
			_tpi.parse(cb, _base);
		} catch (TurtleParseException e) {
			_eh.fatalError(e);
		} catch (ParseException e) {
			_eh.fatalError(e);
		}
	}
}