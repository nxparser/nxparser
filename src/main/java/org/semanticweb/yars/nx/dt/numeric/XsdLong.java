package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:long datatype
 * @author aidhog
 *
 */
public class XsdLong extends Datatype<Long> {
	public static final Resource DT = XSD.LONG;
	private long _l;
	
	public static final String REGEX = "[+-]?[0-9]*";
	
	public XsdLong(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
		
		try{
			if(!s.startsWith("+"))
				_l = Long.parseLong(s);
			else _l = Long.parseLong(s.substring(1));
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing long: "+e.getMessage()+".",s,DT,4);
		}
	}
	
	public String getCanonicalRepresentation() {
		return Long.toString(_l);
	}

	public Long getValue() {
		return _l;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdLong dec = new XsdLong("+9223372036854775807");
		System.err.println(dec.getCanonicalRepresentation());
	}
}