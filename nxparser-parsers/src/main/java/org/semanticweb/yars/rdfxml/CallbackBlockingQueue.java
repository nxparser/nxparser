package org.semanticweb.yars.rdfxml;

import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackBlockingQueue extends Callback{
	BlockingQueue<Node[]> _q;
	InterruptedException _e;
	
	public CallbackBlockingQueue(BlockingQueue<Node[]> q){
		_q = q;
	}

	public boolean successful(){
		return _e == null;
	}

	public Exception getException(){
		return _e;
	}

	protected void processStatementInternal(Node[] nx) {
		try{
			_q.put(nx);
		} catch(InterruptedException e){
			_e = e;
		}
	}

	protected void startDocumentInternal() {
		;
	}
	
	protected void endDocumentInternal() {
		processStatement(Nodes.EOM);
	}

}
