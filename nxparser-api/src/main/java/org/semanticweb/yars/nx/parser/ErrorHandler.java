package org.semanticweb.yars.nx.parser;

/**
 * The interface for error handling, similar to SAX.
 * 
 * @author aharth, Tobias Kaefer
 */
public interface ErrorHandler {
//	/** recoverable error */ - we don't have that 
//	public void error(Exception e);
	/** non-recoverable error */
	public void fatalError(Exception e);
	/** conditions that are not errors, that is, information to the user, warnings */
	public void warning(Exception e);
}