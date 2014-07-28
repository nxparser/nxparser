package org.semanticweb.yars.rdfxml;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * Just for debugging
 * @author aidhog
 *
 */
public class PrintCallBack implements Callback{

	public void endDocument() {
		;
	}

	public void processStatement(Node[] ns) {
		System.err.println(Nodes.toN3(ns));
	}

	public void startDocument() {
		// TODO Auto-generated method stub
		
	}
	
}
