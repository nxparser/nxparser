package org.semanticweb.yars.nx.dt.binary;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;
/**
 * Represents the xsd:base64Binary datatype
 * @author aidhog
 *
 */
		
public class XSDBase64Binary extends Datatype<String> {
	public static final Resource DT = XSD.BASE64BINARY;
	private String _h;
	
	public static final String REGEX = "((([A-Za-z0-9+/] ?){4})*(([A-Za-z0-9+/] ?){3}[A-Za-z0-9+/]|([A-Za-z0-9+/] ?){2}[AEIMQUYcgkosw048] ?=|[A-Za-z0-9+/] ?[AQgw] ?= ?=))?";
	
	public XSDBase64Binary(String s) throws DatatypeParseException{
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
		_h = s;
	}

	public String getValue() {
		return _h;
	}

	public String getCanonicalRepresentation() {
		return _h;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDBase64Binary hex = new XSDBase64Binary("098abzxcbvks/ajg+r--cDF087123D");
		System.err.println(hex.getCanonicalRepresentation());
	}
}
