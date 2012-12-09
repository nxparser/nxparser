package org.semanticweb.yars.util;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;

public class NodesToNodeArrayIterator implements Iterator<Node[]> {
	Iterator<Nodes> in;
	
	public NodesToNodeArrayIterator(Iterator<Nodes> in){
		this.in = in;
	}

	public boolean hasNext() {
		return in.hasNext();
	}

	public Node[] next() {
		return in.next().getNodes();
	}

	public void remove() {
		in.remove();
	}
}
