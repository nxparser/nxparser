// (c) 2004 Andreas Harth

package org.semanticweb.yars.nx;

import java.io.Serializable;

/**
 * An RDF node (resource, bnode or literal). Should be immutable.
 * 
 * We assume that we get the data in Unicode (as in RDF 1.1), so no
 * escaping is necessary.
 *
 * @author Andreas Harth
 */
public interface Node extends Comparable<Node>, Serializable { 
    
    /**
     * Nx representation.
     */
    public String toString();

    /**
     * Return "string-only" representation of the node.
     * 
     * For literals, only the part inside the quotation marks.
     * For URIs, only the URI as string without <>.
     * For blank nodes, only the blank node label without _:.
     */
    public String getLabel();

    /**
     * Equality.
     */
    public boolean equals(Object n);
    
    /**
     * Needed for storing in hashtables.
     */
    public int hashCode();
 }
	