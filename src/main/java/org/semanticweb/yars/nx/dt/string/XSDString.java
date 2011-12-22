package org.semanticweb.yars.nx.dt.string;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:string datatype
 * @author aidhog
 *
 */
public class XSDString extends Datatype<String> {
	public static final Resource DT = XSD.STRING;
	private String _ns;
	
//	public static final String REGEX = "[\\x09\\x0A\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-\\x10FFFF]*";
	
	public XSDString(String s) throws DatatypeParseException{
//		if (!Pattern.matches(REGEX, s))
//			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT);

		_ns = s;
	}
	
	public String getCanonicalRepresentation() {
		return _ns;
	}

	public String getValue() {
		return _ns;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDString dec = new XSDString("1234567890\\\n\t\r\b\f``");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}