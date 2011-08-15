package org.semanticweb.yars.nx.dt;


import org.semanticweb.yars.nx.Resource;

public class UnsupportedDatatypeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedDatatypeException(){
		super();
	}

	public UnsupportedDatatypeException(Exception e){
		super(e);
	}
	
	public UnsupportedDatatypeException(String msg){
		super(msg);
	}
}
