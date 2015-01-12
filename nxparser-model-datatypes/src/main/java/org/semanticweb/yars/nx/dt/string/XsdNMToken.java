package org.semanticweb.yars.nx.dt.string;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.dt.XmlRegex;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:NMTOKEN datatype
 * @author aidhog
 *
 */
public class XsdNMToken extends Datatype<String> {
	public static final Resource DT = XSD.NMTOKEN;
	private String _ns;
	
	public XsdNMToken(String s) throws DatatypeParseException{
		if(s==null)
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(XmlRegex.NM_TOKEN, s))
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
		XsdNMToken dec = new XsdNMToken("-asdafdajskdfaf");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}