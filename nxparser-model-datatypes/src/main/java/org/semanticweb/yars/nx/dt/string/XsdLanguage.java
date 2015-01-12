package org.semanticweb.yars.nx.dt.string;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:language datatype
 * @author aidhog
 *
 */
public class XsdLanguage extends Datatype<Locale> {
	public static final Resource DT = XSD.LANGUAGE;
	
	public static final String REGEX = "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*";
	private Locale _l;
	private String _s;
	
	
	public XsdLanguage(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if (!Pattern.matches(REGEX, s))
			throw new DatatypeParseException("Lexical value does not correspond to regex "+REGEX+".",s,DT,20);
		
		StringTokenizer tok = new StringTokenizer(s, "-");
		if(tok.countTokens()==0){
			throw new DatatypeParseException("Cannot parse according to '-'.",s, DT,21);
		}
		String lang = tok.nextToken();
		
		if(lang.length()!=2){
			throw new DatatypeParseException("Expecting first token to be of length 2: '"+lang+"'.",s, DT,22);
		}
		
		if(tok.hasMoreTokens()){
			String country = tok.nextToken();
			if(tok.hasMoreTokens()){
				_l = new Locale(lang, country, tok.nextToken());
			} else _l = new Locale(lang, country);
		} else {
			_l = new Locale(lang);
		}
		_s = s;
	}
	
	public String getCanonicalRepresentation() {
		return _s;
	}

	public Locale getValue() {
		return _l;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdLanguage dec = new XsdLanguage("en-US-asdjf");
		System.err.println(dec.getCanonicalRepresentation());
		System.err.println(dec.getValue());
	}
}