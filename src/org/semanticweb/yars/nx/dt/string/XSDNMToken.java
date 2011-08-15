package org.semanticweb.yars.nx.dt.string;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.dt.XMLRegex;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:NMTOKEN datatype
 * @author aidhog
 *
 */
public class XSDNMToken extends Datatype<String> {
	public static final Resource DT = XSD.NMTOKEN;
	private String _ns;
	
	public XSDNMToken(String s) throws DatatypeParseException{
		if(s==null)
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(XMLRegex.NM_TOKEN, s))
			throw new DatatypeParseException("Lexical value does not correspond to NMTOKEN regex.",s,DT,2);
		
		_ns = s;
	}
	
	public String getCanonicalRepresentation() {
		return _ns;
	}

	public String getValue() {
		return _ns;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDNMToken dec = new XSDNMToken("-asdafdajskdfaf");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}