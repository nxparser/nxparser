package org.semanticweb.yars.util;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

public class Callbacks implements Callback {
	Callback[] _cbs;
	
	public Callbacks(Callback... cbs) {
		_cbs = cbs;
	}

	public void processStatement(Node[] nx) {
		for (Callback cb : _cbs) {
			cb.processStatement(nx);
		}
	}

	
	public void startDocument() {
		for (Callback cb : _cbs) {
			cb.startDocument();
		}
	}
	
	public void endDocument() {
		for (Callback cb : _cbs) {
			cb.endDocument();
		}		
	}

}
