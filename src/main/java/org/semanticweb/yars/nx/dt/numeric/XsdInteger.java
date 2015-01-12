package org.semanticweb.yars.nx.dt.numeric;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:integer datatype
 * @author aidhog
 *
 */
public class XsdInteger extends Datatype<BigInteger> {
	public static final Resource DT = XSD.INTEGER;
	private BigInteger _bi;
	
	public static final String REGEX = "[+-]?[0-9]*";
	
	public XsdInteger(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(!s.startsWith("+"))
				_bi = new BigInteger(s);
			else _bi = new BigInteger(s.substring(1));
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing BigInteger: "+e.getMessage()+".",s,DT,21);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _bi.toString();
	}

	public BigInteger getValue() {
		return _bi;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdInteger dec = new XsdInteger("-1876");
		System.err.println(dec.getCanonicalRepresentation());
	}
}