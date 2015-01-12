package org.semanticweb.yars.nx.dt.binary;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;
/**
 * Represents the xsd:hexBinary datatype
 * @author aidhog
 *
 */
		
public class XsdHexBinary extends Datatype<String> {
	public static final Resource DT = XSD.HEXBINARY;
	private String _h;
	
	public static final String REGEX = "([0-9a-fA-F]{2})*";
	
	public XsdHexBinary(String s) throws DatatypeParseException{
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
		_h = s;
	}

	public String getValue() {
		return _h;
	}

	public String getCanonicalRepresentation() {
		return _h.toUpperCase();
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdHexBinary hex = new XsdHexBinary("098acbcDF087123D");
		System.err.println(hex.getCanonicalRepresentation());
	}
}
