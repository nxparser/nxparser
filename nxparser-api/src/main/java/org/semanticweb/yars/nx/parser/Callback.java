package org.semanticweb.yars.nx.parser;

import org.semanticweb.yars.nx.Node;

public abstract class Callback {

	enum State {
		DOCUMENT_STARTED, DOCUMENT_ENDED
	};

	State _state = State.DOCUMENT_ENDED;

	public void startDocument() {
		if (_state != State.DOCUMENT_ENDED)
			throw new IllegalStateException("Document already started!");
		else {
			startDocumentInternal();
			_state = State.DOCUMENT_STARTED;
		}
	}
	
	public void endDocument() {
		if (_state != State.DOCUMENT_STARTED)
			throw new IllegalStateException("Document already ended!");
		else {
			endDocumentInternal();
			_state = State.DOCUMENT_ENDED;
		}
	}
	
	public void processStatement(Node[] nx) {
		if (_state != State.DOCUMENT_STARTED)
			throw new IllegalStateException("Document already ended!");
		else
			processStatementInternal(nx);
	}
	

	abstract protected void startDocumentInternal();

	abstract protected void endDocumentInternal();

	abstract protected void processStatementInternal(Node[] nx);
}
