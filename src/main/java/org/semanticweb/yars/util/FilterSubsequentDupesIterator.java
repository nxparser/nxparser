package org.semanticweb.yars.util;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeComparator;

public class FilterSubsequentDupesIterator implements Iterator<Node[]>{
	Iterator<Node[]> in;
	Node[] current = null;
	
	public FilterSubsequentDupesIterator(Iterator<Node[]> in){
		this.in = in;
		loadNext();
	}

	public boolean hasNext() {
		return current!=null;
	}

	public Node[] next() {
		Node[] next = current;
		loadNext();
		return next;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	private void loadNext(){
		Node[] last = current;
		current = null;

		while(in.hasNext() && current==null){
			Node[] next = in.next();
			if(last==null || !NodeComparator.NC.equals(next,last)){
				current = next;
			}
		} 
	}
}
