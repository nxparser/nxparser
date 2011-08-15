package org.semanticweb.yars.nx.dt.numeric;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:positiveInteger datatype
 * @author aidhog
 *
 */
public class XSDPositiveInteger extends Datatype<BigInteger> {
	public static final Resource DT = XSD.POSITIVEINTEGER;
	private BigInteger _bi;
	
	public static final String REGEX = "[+]?[0-9]*";
	
	public XSDPositiveInteger(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(!s.startsWith("+"))
				_bi = new BigInteger(s);
			else _bi = new BigInteger(s.substring(1));
			
			if(_bi.compareTo(BigInteger.ZERO)==0){
				throw new DatatypeParseException("Error parsing positiveInteger: zero value.",s,DT,21);
			}
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing positiveInteger: "+e.getMessage()+".",s,DT,21);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _bi.toString();
	}

	public BigInteger getValue() {
		return _bi;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDPositiveInteger dec = new XSDPositiveInteger("+0");
		System.err.println(dec.getCanonicalRepresentation());
	}
}