package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:short datatype
 * @author aidhog
 *
 */
public class XsdShort extends Datatype<Short> {
	public static final Resource DT = XSD.SHORT;
	private short _s;
	
	public static final String REGEX = "[+-]?[0-9]*";
	
	public XsdShort(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
		
		try{
			if(!s.startsWith("+"))
				_s = Short.parseShort(s);
			else _s = Short.parseShort(s.substring(1));
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing short: "+e.getMessage()+".",s,DT,4);
		}
	}
	
	public String getCanonicalRepresentation() {
		return Short.toString(_s);
	}

	public Short getValue() {
		return _s;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdShort dec = new XsdShort("-32767");
		System.err.println(dec.getCanonicalRepresentation());
	}
}