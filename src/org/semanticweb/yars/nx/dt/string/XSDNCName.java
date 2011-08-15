package org.semanticweb.yars.nx.dt.string;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.dt.XMLRegex;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:NCName datatype
 * @author aidhog
 *
 */
public class XSDNCName extends Datatype<String> {
	public static final Resource DT = XSD.NCNAME;
	private String _n;
	
	public XSDNCName(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(XMLRegex.NC_NAME, s))
			throw new DatatypeParseException("Lexical value does not correspond to NCName regex.",s,DT,2);
		
		_n = s;
	}
	
	public String getCanonicalRepresentation() {
		return _n;
	}

	public String getValue() {
		return _n;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDNCName dec = new XSDNCName("tag");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}