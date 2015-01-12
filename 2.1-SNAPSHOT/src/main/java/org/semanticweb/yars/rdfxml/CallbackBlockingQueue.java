package org.semanticweb.yars.rdfxml;

import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackBlockingQueue implements Callback{
	BlockingQueue<Node[]> _q;
	InterruptedException _e;
	
	public CallbackBlockingQueue(BlockingQueue<Node[]> q){
		_q = q;
	}

	public void endDocument() {
		processStatement(Nodes.EOM);
	}
	
	public boolean successful(){
		return _e == null;
	}

	public Exception getException(){
		return _e;
	}

	public void processStatement(Node[] nx) {
		try{
			_q.put(nx);
		} catch(InterruptedException e){
			_e = e;
		}
	}

	public void startDocument() {
		;
	}
	
	

}
