package org.semanticweb.yars.turtle;

import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.rdfxml.CallbackBlockingQueue;

public class CallbackIterator extends Callback implements Iterable<Node[]>, Iterator<Node[]> {
	final BlockingDeque<Node[]> _dq;
	final CallbackBlockingQueue _cb;

	public CallbackIterator() {
		_dq = new LinkedBlockingDeque<Node[]>();
		_cb = new CallbackBlockingQueue(_dq) {
			protected void endDocumentInternal() {
				;
			}
		};
	}

	@Override
	public boolean hasNext() {
		return !_dq.isEmpty() || _cb.documentIsStarted();
	}

	@Override
	public Node[] next() {
		return _dq.pop();
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void startDocumentInternal() {
		;
	}

	@Override
	protected void endDocumentInternal() {
		;
	}

	@Override
	protected void processStatementInternal(Node[] nx) {
		_dq.add(nx);
	}
}