package org.semanticweb.yars.rdfxml;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * Just for debugging
 * @author aidhog
 *
 */
public class StdErrPrintCallBack extends Callback {

	public void endDocumentInternal() {
		;
	}

	public void processStatementInternal(Node[] ns) {
		System.err.println(new Nodes(ns));
	}

	public void startDocumentInternal() {
		// TODO Auto-generated method stub
		
	}
	
}
