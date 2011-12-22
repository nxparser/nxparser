package org.semanticweb.yars.util;

import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackBlockingQueue implements Callback{
	private BlockingQueue<Node[]> _q;
	
	//do not change
	public static Node[] POISON_TOKEN = new Node[0];
	
	public CallbackBlockingQueue(BlockingQueue<Node[]> q){
		_q = q;
	}

	public void endDocument() {
		try {
			_q.put(POISON_TOKEN);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void processStatement(Node[] ns) {
		try {
			_q.put(ns);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void startDocument() {
		;
	}
	
	
}
