package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:int datatype
 * @author aidhog
 *
 */
public class XsdInt extends Datatype<Integer> {
	public static final Resource DT = XSD.INT;
	private int _i;
	
	public static final String REGEX = "[+-]?[0-9]*";
	
	public XsdInt(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
		
		try{
			if(!s.startsWith("+"))
				_i = Integer.parseInt(s);
			else _i = Integer.parseInt(s.substring(1));
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing int: "+e.getMessage()+".",s,DT,4);
		}
	}
	
	public String getCanonicalRepresentation() {
		return Integer.toString(_i);
	}

	public Integer getValue() {
		return _i;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdInt dec = new XsdInt("-0");
		System.err.println(dec.getCanonicalRepresentation());
	}
}