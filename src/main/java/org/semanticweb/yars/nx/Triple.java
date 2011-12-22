// (c) 2004 Andreas Harth, Stefan Decker

package org.semanticweb.yars.nx;

import java.io.Serializable;




/**
 * An RDF statement (triple).
 *
 * @author Andreas Harth, Stefan Decker
 */
public class Triple implements Serializable {
    // predicate should be always resources
    protected Node _pred;
    // subject and object can be any datatype
    protected Node _sub, _obj;
    
	// version number for serialization
	public static final long serialVersionUID = 1l;
    
    /**
     * Constructor.
     * 
     * @param subject
     * @param predicate
     * @param object
     * @param context
     */
    public Triple(Node subject, Node predicate, Node object) {
        _sub = subject;	
        _pred = predicate;
        _obj = object;
    }
    
    /**
     * Constructor.
     */
    public Triple(Triple s) {
        _sub = s.getSubject();	
        _pred = s.getPredicate();
        _obj = s.getObject();
    }
    
    /**
     * From array
     * @return Triple or Quad
     */
    public static Triple fromArray(Node[] na){
    	if(na.length==3)
    		return new Triple(na[0], na[1], na[2]);
    	else if(na.length==4)
    		return Quad.fromArray(na);
    	else
    		return null;
    }
    
    /**
     * To array
     * @return Node[] length 3 containing nodes of Triple
     */
    public Node[] toArray(){
    	Node[] n = new Node[3];
    	n[0] = getSubject();
    	n[1] = getPredicate();
    	n[2] = getObject();
    	return n;
    }
    
    /**
     * @return
     */
    public Node getObject() {
        return _obj;
    }
    
    /**
     * @return
     */
    public Node getPredicate() {
        return _pred;
    }
    
    /**
     * @return
     */
    public Node getSubject() {
        return _sub;
    }
    
    /**
     * @param l
     */
    public void setObject(Node l) {
        _obj = l;
    }
    
    /**
     * @param l
     */
    public void setPredicate(Resource l) {
        _pred = l;
    }
    
    /**
     * @param l
     */
    public void setSubject(Node l) {
        _sub = l;
    }
   
    /**
     * String representation.
     */
    public String toString() {
    	return toN3();
    }
    
    /**
     * Return N3 representation.
     * 
     * @return
     */
    public String toN3() {
        StringBuffer buf = new StringBuffer();
        
        buf.append(_sub.toN3());
        buf.append(" ");
        buf.append(_pred.toN3());
        buf.append(" ");
        buf.append(_obj.toN3());       
        buf.append(" .");
        
        return buf.toString();
    }
    
    /**
     * Equality check
     * 
     * @param
     * @return
     */
    public boolean equals(Object o) {
    	boolean result = (o != null) && (o instanceof Triple);
        if(result) {
            Triple s = (Triple)o;
            result &= s._sub.equals(_sub) 
            			&& s._pred.equals(_pred) 
            			&& s._obj.equals(_obj)
            			;
        }
        return result;
    }
    
    /**
     * Hashcode
     */
    public int hashCode() {
    	return toN3().hashCode();
    }
}
