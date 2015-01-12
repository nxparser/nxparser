package org.semanticweb.yars.nx.parser;

import org.semanticweb.yars.nx.Node;

public interface Callback {
	public void startDocument();
	public void endDocument();
	public void processStatement(Node[] nx);
}
