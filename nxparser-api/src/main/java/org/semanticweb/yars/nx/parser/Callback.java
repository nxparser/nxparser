package org.semanticweb.yars.nx.parser;

import java.util.concurrent.atomic.AtomicInteger;

import org.semanticweb.yars.nx.Node;

public abstract class Callback {

	protected AtomicInteger _openDocuments = new AtomicInteger(0);

	public void startDocument() {
		startDocumentInternal();

		_openDocuments.incrementAndGet();
	}

	public void endDocument() {
		endDocumentInternal();

		int i = _openDocuments.decrementAndGet();
		if (i<0)
			throw new IllegalStateException("I don't have a document to end.");
	}

	public void processStatement(Node[] nx) {
		if (_openDocuments.get()<1)
			throw new IllegalStateException("I don't have a document to write to.");
		else
			processStatementInternal(nx);
	}

	abstract protected void startDocumentInternal();

	abstract protected void endDocumentInternal();

	abstract protected void processStatementInternal(Node[] nx);
}
