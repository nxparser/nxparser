package org.semanticweb.yars.nx.parser;

/**
 * An exception that in an HTTP context would mean a 500 response should be
 * returned.
 * 
 * @author Tobias Käfer
 *
 */
public class InternalParserError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1550575035176623876L;

	public InternalParserError() {
		super();
	}

	public InternalParserError(String message) {
		super(message);
	}

	public InternalParserError(Exception ex) {
		super(ex);
	}

}
