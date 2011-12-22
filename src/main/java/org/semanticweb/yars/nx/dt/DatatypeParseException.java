package org.semanticweb.yars.nx.dt;


import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.ParseException;

public class DatatypeParseException extends ParseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _error = -1;

	public DatatypeParseException(){
		super();
	}

	public DatatypeParseException(Exception e){
		super(e);
	}
	
	public DatatypeParseException(String msg, int error){
		super(msg);
		_error = error;
	}
	
//	public DatatypeParseException(String msg, String val, Resource dt){
//		super(msg+"\nValue '"+val+"' not in the lexical space of datatype "+dt.toString());
//	}
	
	public DatatypeParseException(String msg, String val, Resource dt, int error){
		super(msg+"\nValue '"+val+"' not in the lexical space of datatype "+dt.toString()+". Error "+error+".");
		_error = error;
	}
	
	public int getError(){
		return _error;
	}
	
//	public DatatypeParseException(String val, Resource dt){
//		super("Value '"+val+"' not in the lexical space of datatype "+dt.toString());
//	}
}
