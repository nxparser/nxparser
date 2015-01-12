package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:unsignedShort datatype
 * @author aidhog
 *
 */
public class XsdUnsignedShort extends Datatype<Integer> {
	public static final Resource DT = XSD.UNSIGNEDSHORT;
	private Integer _us;
	
	private static final int MAX_INCLUSIVE = 65535;
	private static final int MIN_INCLUSIVE = 0;
	
	public static final String REGEX = "(\\+|-)?[0-9]+";
	
	public XsdUnsignedShort(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(s.startsWith("+"))
				s = s.substring(1, s.length());
			_us = Integer.parseInt(s);
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing int (unsigned short): "+e.getMessage()+".",s,DT,40);
		}
		
		if(_us.compareTo(MAX_INCLUSIVE)>0){
			throw new DatatypeParseException("Value of unsigned short above max_inclusive value "+MAX_INCLUSIVE+".",s,DT,41);
		} else if(_us.compareTo(MIN_INCLUSIVE)<0){
			throw new DatatypeParseException("Value of unsigned short below min_inclusive value "+MIN_INCLUSIVE+".",s,DT,42);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _us.toString();
	}

	public Integer getValue() {
		return _us;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdUnsignedShort dec = new XsdUnsignedShort("23425");
		System.err.println(dec.getCanonicalRepresentation());
	}
}