package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Unbound implements Node,Serializable{
	
	public static final long serialVersionUID = 1l;
	public static final String TO_STRING = "UNBOUND";
	
	public Unbound() {
		;
	}
	
	public String toString() {
		return TO_STRING;
	}

	/**
	 * if parameter is of type Unbound, return true, else return 1
	 * @param o - Object 
	 */
	public int compareTo(Object o) {
		if(o instanceof Unbound){
			return 0;
		}
		else
			return 1;
	}
	
    /**
     * Equality check
     * 
     */
    public boolean equals(Object o) {
    	return (o instanceof Unbound);
    }

	public String toN3() {
		return toString();
	}
	
    /**
     * Needed for storing in hashtables.
     */
    public int hashCode() {
    	return toString().hashCode();
    }
}