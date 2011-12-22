package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:double datatype
 * @author aidhog
 *
 */
public class XSDDouble extends Datatype<Double> {
	public static final Resource DT = XSD.DOUBLE;
	private double _d;
	private static final String REGEX = "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN";
	
	private static final String POSITIVE_NUMBER = ".*[1-9].*";
	
	public XSDDouble(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			if (!Pattern.matches(REGEX, s))
				throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
			if(s.equals("INF")){
				_d = Double.POSITIVE_INFINITY;
			} else if(s.equals("-INF")){
				_d = Double.NEGATIVE_INFINITY;
			} else{
				_d = Double.parseDouble(s);
				if(_d == Double.POSITIVE_INFINITY){
					throw new DatatypeParseException("Error parsing double: exceeds POSITIVE MAX of "+Double.MAX_VALUE+".",s,DT,2);
				} else if(_d == Double.NEGATIVE_INFINITY){
					throw new DatatypeParseException("Error parsing double: exceeds NEGATIVE MAX of -"+Double.MAX_VALUE+".",s,DT,2);
				} else if(_d == 0 && s.matches(POSITIVE_NUMBER)){
					throw new DatatypeParseException("Error parsing double: exceeds MIN of "+Double.MIN_VALUE+".",s,DT,2);
				}
			}
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing double: "+e.getMessage()+".",s,DT,2);
		}
	}
	
	public String getCanonicalRepresentation() {
		if(_d == Double.POSITIVE_INFINITY)
			return "INF";
		else if(_d == Double.NEGATIVE_INFINITY)
			return "-INF";
		else{
			return Double.toString(_d);
		}
	}

	public Double getValue() {
		return _d;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDDouble dec = new XSDDouble("1420e-12");
		System.err.println(dec.getCanonicalRepresentation());
	}
}