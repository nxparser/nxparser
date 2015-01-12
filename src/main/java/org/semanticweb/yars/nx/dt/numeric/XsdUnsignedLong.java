package org.semanticweb.yars.nx.dt.numeric;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:unsignedLong datatype
 * @author aidhog
 *
 */
public class XsdUnsignedLong extends Datatype<BigInteger> {
	public static final Resource DT = XSD.UNSIGNEDLONG;
	private BigInteger _ul;
	
	private static final BigInteger MAX_INCLUSIVE = new BigInteger("18446744073709551615");
	private static final BigInteger MIN_INCLUSIVE = new BigInteger("0");
	
	public static final String REGEX = "(\\+|-)?[0-9]+";
	
	public XsdUnsignedLong(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(s.startsWith("+"))
				s = s.substring(1, s.length());
			_ul = new BigInteger(s);
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing BigInteger (unsigned long): "+e.getMessage()+".",s,DT,21);
		}
		
		if(_ul.compareTo(MAX_INCLUSIVE)>0){
			throw new DatatypeParseException("Value of unsigned long above max_inclusive value "+MAX_INCLUSIVE+".",s,DT,40);
		} else if(_ul.compareTo(MIN_INCLUSIVE)<0){
			throw new DatatypeParseException("Value of unsigned long below min_inclusive value "+MIN_INCLUSIVE+".",s,DT,42);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _ul.toString();
	}

	public BigInteger getValue() {
		return _ul;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdUnsignedLong dec = new XsdUnsignedLong("+2345276324514325326532514321");
		System.err.println(dec.getCanonicalRepresentation());
	}
}