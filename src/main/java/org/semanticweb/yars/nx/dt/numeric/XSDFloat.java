package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:float datatype
 * @author aidhog
 *
 */
public class XSDFloat extends Datatype<Float> {
	public static final Resource DT = XSD.FLOAT;
	private float _f;
	private static final String REGEX = "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN";
	
	private static final String POSITIVE_NUMBER = ".*[1-9].*";
	
	public XSDFloat(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			if (!Pattern.matches(REGEX, s))
				throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,2);
			if(s.equals("INF")){
				_f = Float.POSITIVE_INFINITY;
			} else if(s.equals("-INF")){
				_f = Float.NEGATIVE_INFINITY;
			} else{
				_f = Float.parseFloat(s);
				if(_f == Float.POSITIVE_INFINITY){
					throw new DatatypeParseException("Error parsing float: exceeds POSITIVE MAX of "+Float.MAX_VALUE+".",s,DT,2);
				} else if(_f == Float.NEGATIVE_INFINITY){
					throw new DatatypeParseException("Error parsing float: exceeds NEGATIVE MAX of -"+Float.MAX_VALUE+".",s,DT,2);
				} else if(_f == 0 && s.matches(POSITIVE_NUMBER)){
					throw new DatatypeParseException("Error parsing float: exceeds MIN of "+Float.MIN_VALUE+".",s,DT,2);
				}
			}
			_f = Float.parseFloat(s);
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing float: "+e.getMessage()+".",s,DT,2);
		}
	}
	
	public String getCanonicalRepresentation() {
		if(_f == Float.POSITIVE_INFINITY)
			return "INF";
		else if(_f == Float.NEGATIVE_INFINITY)
			return "-INF";
		else{
			return Float.toString(_f);
		}
	}

	public Float getValue() {
		return _f;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDFloat dec = new XSDFloat("12.78e-500");
		System.err.println(dec.getCanonicalRepresentation());
	}
}