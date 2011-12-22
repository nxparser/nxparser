package org.semanticweb.yars.util.thread;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;

/**
 * Wraps an iterator around a blocking queue.
 * @author aidhog
 *
 */
public class BlockingQueueIterator implements Iterator<Node[]> {
	BlockingQueue<Node[]> _q;
	Node[] _current = null;
	
	/**
	 * Constructor... Producer for queue should be initialised
	 * before calling constructor as the constructor will wait
	 * on q poll.
	 * @param q
	 */
	public BlockingQueueIterator(BlockingQueue<Node[]> q){
		_q = q;
		_current = _q.poll();
	}
	
	public boolean hasNext() {
		if(_current==null || _current==Nodes.EOM){
			return false;
		}
		return true;
	}

	public Node[] next() {
		Node[] next = _current;
		_current = _q.poll();
		return next;
	}

	public void remove() {
		;
	}
}
