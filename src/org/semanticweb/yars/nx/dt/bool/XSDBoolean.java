package org.semanticweb.yars.nx.dt.bool;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;
/**
 * Represents the xsd:boolean datatype
 * @author aidhog
 *
 */
		
public class XSDBoolean extends Datatype<Boolean> {
	public static final Resource DT = XSD.BOOLEAN;
	private boolean _b;
	
	public XSDBoolean(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		if(s.equals("true") || s.equals("1")){
			_b = true;
		} else if(s.equals("false") || s.equals("0")){
			_b = false;
		} else{
			throw new DatatypeParseException("Lexical value is not 0|1|true|false.",s,DT,2);
		}
	}

	public Boolean getValue() {
		return _b;
	}

	public String getCanonicalRepresentation() {
		if(_b)
			return "true";
		return "false";
	}
}
