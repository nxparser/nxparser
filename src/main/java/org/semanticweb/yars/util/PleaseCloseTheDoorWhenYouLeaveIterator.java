package org.semanticweb.yars.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 * Iterator which closes the door when it leaves...
 * @author aidhog
 * @author Tobias Kaefer
 * @param <T>
 *
 */
public class PleaseCloseTheDoorWhenYouLeaveIterator<T> implements Iterator<T> {
	boolean _closed;
	Iterator<T> _it;
	Closeable _cl;

	public PleaseCloseTheDoorWhenYouLeaveIterator(Iterator<T> it, Closeable cl) {
		_closed = false;
		_it = it;
		_cl = cl;
	}

	public boolean hasNext() {
		if (!_it.hasNext() && _closed == false) {
			try {
				_cl.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			_closed = true;
		}
		return _it.hasNext();
	}

	public T next() {
		return _it.next();
	}

	public void remove() {
		_it.remove();
	}
}
