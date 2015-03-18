package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Unbound implements Node, Serializable{
	
	public static final long serialVersionUID = 1l;
	public static final String TO_STRING = "UNBOUND";
	
	public Unbound() {
		;
	}
	
	public String toString() {
		return TO_STRING;
	}
	
	public String getLabel() {
		return toString();
	}

	/**
	 * if parameter is of type Unbound, return true, else return 1
	 * @param o - Object 
	 */
	public int compareTo(Node o) {
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

    /**
     * Needed for storing in hashtables.
     */
    public int hashCode() {
    	return toString().hashCode();
    }
}