package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Variable implements Node,Serializable{
	
	private static final long serialVersionUID = 4927370223302416068L;
	private String _data;
	private boolean _isExistential = false;
	public static final String JOIN_CONST_PREFIX = "y2joinvar:";
	
	public Variable(String data, boolean isN3) {
		if (isN3)
			_data = data;
		else {
			if (data.charAt(0) == '?')
				_data = data;
			else
				_data = '?' + data;
		}
	}
	
	public Variable(String data) {
		this(data, false);
	}

	public String toString() {
		return _data.substring(Math.min(_data.length(), 1),
				Math.min(_data.length() - 1, 0));
	}

	/**
	 * if parameter is of type Variable, compare the _data representations
	 * else a variable is always equals to a Resource, Blanknode, or Literal
	 * @param o - Object 
	 */
	public int compareTo(Object o) {
		if(o instanceof Variable){
			return _data.compareTo(o.toString());
		}
		else
			return -1;
	}
	
    /**
     * Equality check.
     * 
     */
    public boolean equals(Object o) {
    	return 
    		   (o != null)
    		&& (o instanceof Variable)
    		&& ((Variable)o)._data.equals(_data)
    		;
    }

	public String toN3() {
		return _data;
	}
	
	public void setExistential(boolean ex){
		_isExistential = ex;
	}
	
	public boolean isExistential(){
		return _isExistential;
	}
	
    /**
     * Needed for storing in hashtables.
     */
    public int hashCode() {
    	return _data.hashCode();
    }
    
    public Literal toJoinLiteral(){
    	return new Literal(JOIN_CONST_PREFIX+toN3());
    }
    
    public static Variable fromJoinLiteral(Literal l){
    	if(isJoinLiteral(l)){
    		return new Variable(l.toString().substring(JOIN_CONST_PREFIX.length()+1));
    	}
    	return null;
    }
    
    public static boolean isJoinLiteral(Literal l){
    	return l.toString().startsWith(JOIN_CONST_PREFIX);
    }
    
    public static boolean isJoinLiteral(Node n){
    	return n instanceof Literal && ((Literal)n).toString().startsWith(JOIN_CONST_PREFIX);
    }
}