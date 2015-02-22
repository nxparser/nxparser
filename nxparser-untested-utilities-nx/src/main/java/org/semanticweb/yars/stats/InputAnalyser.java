package org.semanticweb.yars.stats;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

public class InputAnalyser implements Analyser {
	private final Iterator<Node[]> _in;
	
	public InputAnalyser(Iterator<Node[]> in){
		_in = in;
	}
	
	public boolean hasNext() {
		return _in.hasNext();
	}

	public Node[] next() {
		return _in.next();
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public void analyse(Node[] in) {
		;
	}

	public void stats() {
		;
	}
}
