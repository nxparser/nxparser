package org.semanticweb.yars.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wraps a resetable iterator around an in-memory collection.
 * 
 * @author aidhog
 *
 * @param <E>
 */
public class ResetableCollectionIterator<E> implements ResetableIterator<E> {
	
	Collection<E> _coll = null;
	Iterator<E> _iter = null;
	
	public ResetableCollectionIterator(Collection<E> coll){
		_coll = coll;
		reset();
	}

	public boolean hasNext() {
		return _iter!=null && _iter.hasNext();
	}

	public E next() {
		if(!hasNext()){
			throw new NoSuchElementException();
		}
		return _iter.next();
	}

	public void remove() {
		_iter.remove();
	}

	public void reset() {
		if(_coll!=null)
			_iter = _coll.iterator();
	}
}
