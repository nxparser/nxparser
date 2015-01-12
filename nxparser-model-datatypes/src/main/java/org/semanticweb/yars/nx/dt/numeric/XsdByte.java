package org.semanticweb.yars.nx.dt.numeric;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:byte datatype
 * @author aidhog
 *
 */
public class XsdByte extends Datatype<Byte> {
	public static final Resource DT = XSD.BYTE;
	private byte _b;
	
	public static final String REGEX = "[+-]?[0-9]*";
	
	public XsdByte(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			if(!s.startsWith("+"))
				_b = Byte.parseByte(s);
			else _b = Byte.parseByte(s.substring(1));
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing byte: "+e.getMessage()+".",s,DT,21);
		}
	}
	
	public String getCanonicalRepresentation() {
		return Byte.toString(_b);
	}

	public Byte getValue() {
		return _b;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdByte dec = new XsdByte("-67");
		System.err.println(dec.getCanonicalRepresentation());
	}
}