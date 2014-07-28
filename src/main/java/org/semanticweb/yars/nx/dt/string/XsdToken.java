package org.semanticweb.yars.nx.dt.string;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:token datatype
 * @author aidhog
 *
 */
public class XsdToken extends Datatype<String> {
	public static final Resource DT = XSD.TOKEN;
	private String _ns;
	
	public static final String REGEX = "[^\\n\\t\\r ]?|[^\\n\\t\\r ][^\\n\\t\\r ]|[^ \\n\\t\\r][^\\n\\t\\r]*[^ \\n\\t\\r]";
	
	public XsdToken(String s) throws DatatypeParseException{
		if (!s.isEmpty() && !Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		else if(s.contains("  ")){
			throw new DatatypeParseException("Lexical value should not contain a double-space.",s,DT,21);
		}

		_ns = s;
	}
	
	public String getCanonicalRepresentation() {
		return _ns;
	}

	public String getValue() {
		return _ns;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdToken dec = new XsdToken("a b");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}