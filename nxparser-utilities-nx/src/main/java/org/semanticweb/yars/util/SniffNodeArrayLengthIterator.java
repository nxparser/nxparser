package org.semanticweb.yars.util;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

/**
 * Sniff out an iterators Node[] length
 * @author aidhog
 *
 */
public class SniffNodeArrayLengthIterator implements Iterator<Node[]>{
	Iterator<Node[]> _in;
	short _nxlength = 0;
	Node[] _current;
	
	public SniffNodeArrayLengthIterator(Iterator<Node[]> in){
		_in = in;
		loadNext();
		if(_current!=null)
			_nxlength = (short)_current.length;
	}
	
	private void loadNext(){
		_current = null;
		if(_in.hasNext()){
			_current = _in.next();
		}
	}
	
	public short nxLength() {
		return _nxlength;
	}

	public boolean hasNext() {
		return _current != null;
	}

	public Node[] next() {
		Node[] next = _current;
		loadNext();
		return next;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
