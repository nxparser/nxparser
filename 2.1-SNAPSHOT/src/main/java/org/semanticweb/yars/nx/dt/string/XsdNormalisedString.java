package org.semanticweb.yars.nx.dt.string;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:normalizedString datatype
 * @author aidhog
 *
 */
public class XsdNormalisedString extends Datatype<String> {
	public static final Resource DT = XSD.NORMALIZEDSTRING;
	private String _ns;
	
	public static final String REGEX = "[^\\t^\\r^\\n]*";
	
	public XsdNormalisedString(String s) throws DatatypeParseException{
		if(s==null)
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
		_ns = s;
	}
	
	public String getCanonicalRepresentation() {
		return _ns;
	}

	public String getValue() {
		return _ns.replace("\n", " ");
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdNormalisedString dec = new XsdNormalisedString("");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}