package org.semanticweb.yars.util;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

/**
 * Append static array of nodes to an iterator stream.
 * 
 * @author aidhog
 *
 */
public class AppendIterator implements Iterator<Node[]> {

	public Iterator<Node[]> _in;
	public Node[] _append;
	
	public AppendIterator(Iterator<Node[]> in, Node... n){
		_in = in;
		_append = n;
	}
	
	public boolean hasNext() {
		return _in.hasNext();
	}

	public Node[] next() {
		Node[] next = _in.next();
		Node[] nextAppend = new Node[next.length+_append.length];
		System.arraycopy(next, 0, nextAppend, 0, next.length);
		System.arraycopy(_append, 0, nextAppend, next.length, _append.length);
		return nextAppend;
	}

	public void remove() {
		_in.remove();		
	}
}
