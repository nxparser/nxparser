package org.semanticweb.yars.nx.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.semanticweb.yars.nx.Node;

/**
 * An NxParser that collects the exceptions occurred during the parsing. The
 * exceptions cannot be thrown because of the iterator interface.
 * 
 * @author Tobias Käfer
 *
 */
public class ExceptionCollectingNxParser implements Iterator<Node[]>,
		Iterable<Node[]> {

	Queue<Exception> _exceptions = new LinkedList<Exception>();

	BufferedReader _br;
	boolean _nextIsFresh = false;
	Node[] _next = null;

	public ExceptionCollectingNxParser() {
		;
	}

	public void parse(InputStream is, Charset cs) {
		parse(new BufferedReader(new InputStreamReader(is, cs)));
	}

	public void parse(BufferedReader br) {
		_br = br;
		_next = null;
		_nextIsFresh = false;
		_exceptions.clear();
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (_nextIsFresh && _next != null)
			return true;
		else if (!_nextIsFresh) {
			if (_br == null)
				return false;
			loadNext();
			if (_next != null)
				return true;
			else
				return false;
		} else
			return false;

	}

	private void loadNext() {
		_next = null;
		if (_br == null)
			return;
		String line = null;
		try {
			line = _br.readLine();
			if (line != null)
				do {
					try {
						_next = NxParser.parseNodes(line);
					} catch (ParseException e) {
						_exceptions.add(e);
						_next = null;
					}
				} while (_next == null && ((line = _br.readLine()) != null));
		} catch (IOException e1) {
			_exceptions.add(e1);
		}
		_nextIsFresh = true;
	}

	@Override
	public Node[] next() {
		if (_nextIsFresh) {
			_nextIsFresh = false;
			return _next;
		} else {
			if (hasNext())
				return _next;
			else
				throw new NoSuchElementException();
		}

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the exceptions encountered during since the last {@link #parse} call.
	 * 
	 * @return A the exceptions in order of their occurrence
	 */
	public Queue<Exception> getExceptions() {
		return _exceptions;
	}

}