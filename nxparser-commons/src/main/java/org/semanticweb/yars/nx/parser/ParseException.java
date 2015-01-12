package org.semanticweb.yars.nx.parser;


public class ParseException extends Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public ParseException() {
    super();
  }

  public ParseException(String message) {
    super(message);
  }
  
  public ParseException(Exception ex) {
	  super(ex);
  }
}
