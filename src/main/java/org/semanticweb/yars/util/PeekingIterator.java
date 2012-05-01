package org.semanticweb.yars.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * 
 * An iterator that allows to peek.
 * 
 * @author Tobias Käfer
 *
 * @param <T>
 */
public class PeekingIterator<T> implements Iterator<T> {
	T _peek;
	T _next;
	Iterator<T> _it;
	
	Status _stat;
	
	private enum Status {
		VIRGINAL, RUNNING, ITERATOR_FINISHED, FINISHED
	}
	
	public PeekingIterator(Iterator<T> it) {
		_it = it;
		_stat = Status.VIRGINAL;
		_next = null;
	}
	

	public boolean hasNext() {
		switch (_stat) {
		case VIRGINAL:
			return _it.hasNext();
		default:
			return (_stat != Status.FINISHED);
		}
	}

	public T next() {
		switch (_stat) {
		case VIRGINAL:
			_stat = Status.RUNNING;
			if (_it.hasNext()) {
				T sav = _it.next();
				if (!_it.hasNext())
					_stat = Status.FINISHED;
				else { 
					_peek = _it.next();
					if (!_it.hasNext()) {
						_stat = Status.ITERATOR_FINISHED;
					} else {
						_next = _it.next();
					}
				}
				return sav;
			} else
				_stat = Status.FINISHED;
			// and proceed to FINISHED
		case FINISHED:
			throw new NoSuchElementException();
		case ITERATOR_FINISHED:
			_stat = Status.FINISHED;
			return _peek;
		default:
			T sav = _peek;
			if (!_it.hasNext())
				_stat = Status.FINISHED;
			else
				_peek = _next;
			if (!_it.hasNext()) {
				_stat = Status.ITERATOR_FINISHED;
				_peek = _next;
				_next = null;
			} else {
				_peek = _next;
				_next = _it.next();
			}
			return sav;
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public T peek() {
		switch (_stat) {
		case VIRGINAL:
			if (!_it.hasNext()) {
				_stat = Status.FINISHED;
				return null;
			}
			else {
				_stat = Status.RUNNING;
				_peek = _it.next();
				if (!_it.hasNext())
					_stat = Status.ITERATOR_FINISHED;
				else {
					_next = _it.next();
				}
				return _peek;
			}
		case FINISHED:
			return null;
		default:
			return _peek;
		}

	}
	
	public static void main (String [] args) {
		List<Integer> l = new LinkedList<Integer>();
//		l = Collections.EMPTY_LIST;
		
		for (int i = 0; i < 1; ++i)
			l.add(i);
//			;
		
		PeekingIterator<Integer> it = new PeekingIterator<Integer>(l.iterator());
		
		while (it.hasNext()) {
			if (Math.random() > 0.5)
				System.out.println("peek " + it.peek());
			else
				System.out.println("next " + it.next());
		}
		System.out.println("peek " + it.peek());		

		
	}
}