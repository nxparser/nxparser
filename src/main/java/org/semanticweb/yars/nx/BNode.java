package org.semanticweb.yars.nx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.semanticweb.yars.nx.parser.ParseException;


/**
 * A bnode, anonymous resource.
 *
 * @author Andreas Harth
 * @author Tobias Kaefer
 */
public class BNode implements Serializable, Node {

	public static String PREFIX = "_:";
	
	public static boolean PRETTY_PRINT = false;
	
	// the value of the bnode including prefix
    protected String _data;
    
	// version number for serialization
	private static final long serialVersionUID = 6233987125715026425L;

    /**
     * Constructor if we have a bnode with a nodeID.
     * Need to add context of the file to the nodeID later,
     * otherwise there will be clashes.
     */
    public BNode(String nodeid) {
    	this(nodeid, false);
    }

	public BNode(String nodeid, boolean hasPrefix) {
		if (hasPrefix)
			_data = nodeid;
		else if (!nodeid.startsWith(PREFIX))
			_data = PREFIX + nodeid;
		else
			_data = nodeid;
	}

    /**
     * Get URI.
     */
    public String toString() {
    	if(PRETTY_PRINT){
    		try{
    			String[] conb = parseContextualBNode();
    			return conb[1]+"@["+conb[0]+"]";
    		} catch(ParseException pe){
    			return unescapeForBNode(_data.substring(PREFIX.length()));
    		}
    	} else return _data.substring(PREFIX.length());
    }
    
    public int hashCode() {
    	return _data.hashCode();
    }

    public String toN3() {
    	return _data;
    }

    /**
     * Equality check
     */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		else if (o instanceof BNode)
			return _data.equals(((BNode) o)._data);
		else
			return false;
	}
    
    /**
     * Compare.
     */
    public int compareTo(Object o) {
    	if(o==this)
    		return 0;
    	else if (o instanceof BNode) {
    		BNode b = (BNode)o;
    		return _data.compareTo(b._data);
    	} else if (o instanceof Resource) {
    		return Integer.MAX_VALUE/2;
    	} else if (o instanceof Literal) {
    		return Integer.MAX_VALUE;
    	} else if(o instanceof Unbound){
    		return Integer.MIN_VALUE/2;
    	}  else if (o instanceof Variable) {
    		return Integer.MIN_VALUE;
    	}

    	throw new ClassCastException("parameter is not of type BNode but " + o.getClass().getName());
    }
    
    /**
     * Override readObject for backwards compatability and storing hashcode
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    	ois.defaultReadObject();
    	if(!_data.startsWith(PREFIX))
    		_data = PREFIX+_data;
    }
    
    public String[] parseContextualBNode() throws ParseException{
    	String d = _data.substring(PREFIX.length());
    	String[] uri = d.toString().split("xx");
    	if(uri.length!=2){
    		throw new ParseException("This is not a valid context encoded BNode");
    	}
		uri[0] = unescapeForBNode(uri[0]);
		uri[1] = unescapeForBNode(uri[1]);
		return uri;
    }
    
    public static String[] parseContextualBNode(BNode b) throws ParseException{
    	String[] uri = b.toString().split("xx");
    	if(uri.length!=2){
    		throw new ParseException("Not a valid context encoded BNode "+b);
    	}
		uri[0] = unescapeForBNode(uri[0]);
		uri[1] = unescapeForBNode(uri[1]);
		return uri;
    }
    
    public static BNode createBNode(String docURI, String localID){
		String escapedDU = escapeForBNode(docURI);
		String escapedLI = escapeForBNode(localID);
		return new BNode(escapedLI+"xx"+escapedDU);
    }
    
    public static BNode createBNode(String unescaped){
		String escaped = escapeForBNode(unescaped);
		return new BNode(escaped);
    }
    
    public static String escapeForBNode(String unescaped){
    	try {
			return URLEncoder.encode(unescaped, "utf-8").replace("x", "x78").replace("-", "x2D").replace(".", "x2E").replace("_", "x5F").replace("*","x2A").replace('%', 'x');
		} catch (UnsupportedEncodingException e) {
			//never je suppose
			return null;
		}
    }
    
    public static String unescapeForBNode(String escaped){
    	try {
    		return URLDecoder.decode(escaped.replace('x', '%'), "utf-8");
		} catch (UnsupportedEncodingException e) {
			//never je suppose
			return null;
		}
    }
    
    public static void main(String[] args) throws ParseException{
    	String unescaped = "http://asdj.com/-xx42xxx/%20thing/";
    	System.err.println(escapeForBNode(unescaped));
    	System.err.println(unescapeForBNode(escapeForBNode(unescaped)));
    	
    	System.err.println(createBNode(unescaped,"xx78x"));
    	for(String s:parseContextualBNode(createBNode(unescaped,"xx78x"))){
    		System.err.println(s);
    	}
    }
}
