package org.semanticweb.yars.stats;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

public interface Analyser extends Iterator<Node[]>{
	public void analyse(Node[] in);
	public void stats();
}
