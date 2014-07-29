package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Unbound implements Node,Serializable{
	public static final long serialVersionUID = 1l;
	
	public static final String TO_STRING = "UNBOUND";
	
	public Unbound() {
		;
	}
	
	@Override
	public String toString() {
		return TO_STRING;
	}
	
    /**
     * Equality check
     * 
     */
	@Override
    public boolean equals(Object o) {
    	return (o instanceof Unbound);
    }

    /**
     * Needed for storing in hashtables.
     */
	@Override
    public int hashCode() {
    	return toString().hashCode();
    }
}