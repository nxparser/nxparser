package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:unsignedInt datatype
 * @author aidhog
 *
 */
public class XSDUnsignedInt extends Datatype<Long> {
	public static final Resource DT = XSD.UNSIGNEDINT;
	private Long _ui;
	
	private static final long MAX_INCLUSIVE = Long.parseLong("4294967295");
	private static final long MIN_INCLUSIVE = 0;
	
	public static final String REGEX = "(\\+|-)?[0-9]+";
	
	public XSDUnsignedInt(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(s.startsWith("+"))
				s = s.substring(1, s.length());
			_ui = Long.parseLong(s);
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing long (unsigned int): "+e.getMessage()+".",s,DT,40);
		}
		
		if(_ui>MAX_INCLUSIVE){
			throw new DatatypeParseException("Value of unsigned int above max_inclusive value "+MAX_INCLUSIVE+".",s,DT,41);
		} else if(_ui<MIN_INCLUSIVE){
			throw new DatatypeParseException("Value of unsigned int below min_inclusive value "+MIN_INCLUSIVE+".",s,DT,42);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _ui.toString();
	}

	public Long getValue() {
		return _ui;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDUnsignedInt dec = new XSDUnsignedInt("2334523445");
		System.err.println(dec.getCanonicalRepresentation());
	}
}