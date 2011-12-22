package org.semanticweb.yars.util;

import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeComparator;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackSet implements Callback {
	Set<Node[]> _result;
	long _time, _time1;
	
	public CallbackSet() {
		_result = new TreeSet<Node[]>(NodeComparator.NC);
	}
	
	public synchronized void processStatement(Node[] nx) {
		_result.add(nx);
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