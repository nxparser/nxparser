package org.semanticweb.yars.util;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackCount implements Callback {
	int _i = 0;
	
	public CallbackCount() {
		;
	}

	public int getStmts() {
		return _i;
	}

	public void startDocument() {
		// TODO Auto-generated method stub

	}

	public void endDocument() {
		// TODO Auto-generated method stub

	}
	public void processStatement(Node[] arg0) {
		_i++;
	}
}

