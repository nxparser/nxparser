package org.semanticweb.yars.nx.dt.string;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.dt.XmlRegex;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:Name datatype
 * @author aidhog
 *
 */
public class XsdName extends Datatype<String> {
	public static final Resource DT = XSD.NAME;
	private String _n;
	
	public XsdName(String s) throws DatatypeParseException{
		if(s==null)
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(XmlRegex.NAME, s))
			throw new DatatypeParseException("Lexical value does not correspond to NAME regex.",s,DT,2);
		
		_n = s;
	}
	
	public String getCanonicalRepresentation() {
		return _n;
	}

	public String getValue() {
		return _n;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdName dec = new XsdName("ns1:tag1");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}