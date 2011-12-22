package org.semanticweb.yars.util;

import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeComparator;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackContextSet implements Callback {

	Set<Node[]> _result;
	Node _context;
	long _time, _time1;
	
	public CallbackContextSet(Node context) {
		_result = new TreeSet<Node[]>(NodeComparator.NC);
		_context = context;
	}
	
	public synchronized void processStatement(Node[] nx) {
		Node[] newnx = new Node[nx.length+1];
		System.arraycopy(nx, 0, newnx, 0, nx.length);
		newnx[nx.length] = _context;
		_result.add(newnx);
	}

	public void startDocument() {
		_time = System.currentTimeMillis();
	}
	
	public void endDocument() {
		_time1 = System.currentTimeMillis();
	}
	
	public Set<Node[]> getSet() {
		return _result;
	}
	
	public String toString() {
		return _result.size() + " tuples in " + (_time1-_time) + " ms";
	}
}