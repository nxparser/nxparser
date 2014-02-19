package org.semanticweb.yars.util;

import java.io.IOException;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackNxAppender implements Callback {

	static Logger _log = Logger
			.getLogger(CallbackNxAppender.class.getName());

	Appendable _app;

	public final static String DOTNEWLINE = "."
			+ System.getProperty("line.separator");

	public CallbackNxAppender(Appendable app) {
		_app = app;

	}

	public void startDocument() {
	}

	public void endDocument() {
	}

	public synchronized void processStatement(Node[] nx) {
		try {
			for (Node n : nx) {
				_app.append(n.toN3());
				_app.append(' ');
			}
			_app.append(DOTNEWLINE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
