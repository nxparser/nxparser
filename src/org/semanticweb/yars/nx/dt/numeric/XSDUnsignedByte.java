package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:unsignedByte datatype
 * @author aidhog
 *
 */
public class XSDUnsignedByte extends Datatype<Short> {
	public static final Resource DT = XSD.UNSIGNEDBYTE;
	private Short _ub;
	
	private static final short MAX_INCLUSIVE = 255;
	private static final short MIN_INCLUSIVE = 0;
	
	public static final String REGEX = "(\\+|-)?[0-9]+";
	
	public XSDUnsignedByte(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(s.startsWith("+"))
				s = s.substring(1, s.length());
			_ub = Short.parseShort(s);
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing short (unsigned byte): "+e.getMessage()+".",s,DT,40);
		}
		
		if(_ub>MAX_INCLUSIVE){
			throw new DatatypeParseException("Value of unsigned byte above max_inclusive value "+MAX_INCLUSIVE+".",s,DT,41);
		} else if(_ub<MIN_INCLUSIVE){
			throw new DatatypeParseException("Value of unsigned byte below min_inclusive value "+MIN_INCLUSIVE+".",s,DT,42);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _ub.toString();
	}

	public Short getValue() {
		return _ub;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDUnsignedByte dec = new XSDUnsignedByte("+42");
		System.err.println(dec.getCanonicalRepresentation());
	}
}