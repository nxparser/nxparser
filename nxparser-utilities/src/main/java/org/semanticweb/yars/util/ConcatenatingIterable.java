package org.semanticweb.yars.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ConcatenatingIterable<T> implements Iterable<T> {

	final LinkedList<Iterable<T>> _iterables;

	@SafeVarargs
	public ConcatenatingIterable(Iterable<T>... iterables) {
		_iterables = new LinkedList<Iterable<T>>();
		_iterables.addAll(Arrays.asList(iterables));
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			Iterator<T> _currentIt = null;

			@Override
			public boolean hasNext() {
				if (_currentIt == null)
					try {
						do {
							_currentIt = _iterables.pop().iterator();
						} while (!_currentIt.hasNext());
					} catch (NoSuchElementException e) {
						return false;
					}
				while (!_currentIt.hasNext())
					try {
						_currentIt = _iterables.pop().iterator();
					} catch (NoSuchElementException e) {
						return false;
					}
				return _currentIt.hasNext();
			}

			@Override
			public T next() {
				if (hasNext())
					return _currentIt.next();
				else
					throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				if (_currentIt == null)
					try {
						_currentIt = _iterables.pop().iterator();
					} catch (NoSuchElementException e) {
						return;
					}
				_currentIt.remove();
			}

		};
	}

}
