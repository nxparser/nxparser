// (c) 2004 Andreas Harth, Stefan Decker

package org.semanticweb.yars.nx;

import java.io.Serializable;


/**
 * A quad is an RDF triple with context.
 *
 * @author Andreas Harth, Stefan Decker
 */
public class Quad extends Triple implements Serializable {

    private Node _context;
    
    // context predicate
    public final static Resource CONTEXT = new Resource("http://sw.deri.org/2004/06/yars#context");
  
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
    public Quad(Node subject, Node predicate, Node object, Node context) {
        super(subject, predicate, object);
        _context = context;
    }

    /**
     * Constructor.
     * 
     * @param s
     * @param context
     */
    public Quad(Triple t, Node context) {
    	super(t);
    	_context = context;
    }
    
    /**
     * Constructor.
     */
    public Quad(Quad s) {
    	super(s);
    	_context = s.getContext();
    }
    
    /**
     * From array
     * @return Quad
     */
    public static Quad fromArray(Node[] na){
    	if(na.length!=4){
    		return null;
    	} else return new Quad(na[0], na[1], na[2], na[3]);
    }
    
    /**
     * To array
     * @return Node[] length 4 containing nodes of Quad
     */
    public Node[] toArray(){
    	Node[] n = new Node[4];
    	n[0] = getSubject();
    	n[1] = getPredicate();
    	n[2] = getObject();
    	n[3] = getContext();
    	return n;
    }
   
    /**
     * @return
     */
    public Node getContext() {
        return _context;
    }

    /**
     * @param context
     */
    public void setContext(Resource context) {
        _context = context;
    }
   
    /**
     * To String method.
     * 
     * @return
     */
    public String toString() {
        return toN3();
    }
    
    /**
     * Serialize a quad to N3.
     * 
     * @return
     */
    public String toN3() {
        if (_context == null) {
        	return super.toN3();
        }

        StringBuffer buf = new StringBuffer();

        buf.append(_sub.toN3());
        buf.append(" ");
        buf.append(_pred.toN3());
        buf.append(" ");
        buf.append(_obj.toN3());       
        buf.append(" ");
        buf.append(_context.toN3());
        buf.append(" ");
        buf.append(".");
        
        return buf.toString();
    }
    
    /**
     * Equality check
     * 
     * @param o
     * @return 
     */
    public boolean equals(Object o) {
    	boolean result = (o != null) && (o instanceof Quad);
        if(result) {
            Quad s = (Quad)o;
            result &= s._sub.equals(_sub) 
						&& s._pred.equals(_pred) 
						&& s._obj.equals(_obj)
            			&& s._context.equals(_context)
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
