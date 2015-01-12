package org.semanticweb.yars.nx.dt.numeric;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:decimal datatype
 * @author aidhog
 *
 */
public class XsdDecimal extends Datatype<BigDecimal> {
	public static final Resource DT = XSD.DECIMAL;
	private BigDecimal _bd;
	
	public static final String REGEX = "[+-]?[0-9]*[.]?[0-9]*";
	
	public XsdDecimal(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		try{
			_bd = new BigDecimal(s);
		}catch(NumberFormatException e){
			throw new DatatypeParseException("Error parsing BigDecimal: "+e.getMessage()+".",s,DT,21);
		}
	}
	
	public String getCanonicalRepresentation() {
		if(_bd.scale()==0)
			return _bd.toPlainString()+".0";
		return removeTrailingZeros(_bd.toPlainString());
	}
	
	public static String removeTrailingZeros(String numericDecimal){
		String noTrailingZeros = numericDecimal;
		while(noTrailingZeros.endsWith("0") && !noTrailingZeros.isEmpty()
				&& noTrailingZeros.charAt(noTrailingZeros.length()-2)!='.'){
			noTrailingZeros = noTrailingZeros.substring(0, noTrailingZeros.length()-1);
		}
		return noTrailingZeros;
	}

	public BigDecimal getValue() {
		return _bd;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdDecimal dec = new XsdDecimal("-2345236524533245234523532235645345627.2340");
		System.err.println(dec.getCanonicalRepresentation());
	}
}