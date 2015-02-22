package org.semanticweb.yars.util;

import java.util.Iterator;
import java.util.Map;

import org.semanticweb.yars.nx.Node;

public class FlyweightNodeIterator implements Iterator<Node[]>{
	
	Map<Node,Node> _flyweight;
	Iterator<Node[]> _in;
	
	public FlyweightNodeIterator(int size, Iterator<Node[]> in){
		this(new LRUMapCache<Node,Node>(size),in);
	}
	
	public FlyweightNodeIterator(Map<Node,Node> flyweightMap, Iterator<Node[]> in){
		_flyweight = flyweightMap;
		_in = in;
	}

	public boolean hasNext() {
		return _in.hasNext();
	}

	public Node[] next() {
		Node[] next = _in.next();
		Node[] fwa = new Node[next.length];
		
		for(int i=0; i<next.length; i++){
			fwa[i] = flyweight(next[i]);
		}
		
		return fwa;
	}
	
	private Node flyweight(Node n){
		Node fw = _flyweight.get(n);
		if(fw==null){
			_flyweight.put(n, n);
			return n;
		}
		return fw;
	}

	public void remove() {
		_in.remove();
	}
	
	
}
