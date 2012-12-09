package org.semanticweb.yars.util;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

public class IteratorObjectPair<E> implements Iterator<Node[]> {
	E e;
	Iterator<Node[]> in;
	
	public IteratorObjectPair(Iterator<Node[]> in, E e){
		this.in = in;
		this.e = e;
	}

	public boolean hasNext() {
		return in.hasNext();
	}

	public Node[] next() {
		return in.next();
	}

	public void remove() {
		in.remove();
	}
	
	public E getObject(){
		return e;
	}
}
