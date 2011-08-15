package org.semanticweb.yars.nx.clean;

import java.io.IOException;
import java.io.Reader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class HTMLTextExtractor extends HTMLEditorKit.ParserCallback {
	StringBuffer _s;

	public HTMLTextExtractor() {}

	public void parse(Reader in) throws IOException {
		_s = new StringBuffer();
		ParserDelegator delegator = new ParserDelegator();
		delegator.parse(in, this, false);
	}

	public void handleText(char[] text, int pos) {
		_s.append(text);
	}

	public String getText() {
		return _s.toString();
	}
}
