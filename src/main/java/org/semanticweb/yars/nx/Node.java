// (c) 2004 Andreas Harth

package org.semanticweb.yars.nx;

import java.io.Serializable;



/**
 * An RDF node (resource, bnode or literal). Should be immutable.
 *
 * @author Andreas Harth
 */
public interface Node extends /*Comparable<Node>,*/ Serializable { 
    /**
     * N3 representation.
     */
    public String toN3();
    
    /**
     * String representation.
     */
    public String toString();

    /**
     * Equality?
     */
    public boolean equals(Object n);
    
    /**
     * Needed for storing in hashtables.
     */
    public int hashCode();
 }
