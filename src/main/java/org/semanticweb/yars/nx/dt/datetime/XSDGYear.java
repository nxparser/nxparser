package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:gDay datatype
 * @author aidhog
 *
 */
public class XSDGYear extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.GYEAR;
	private GregorianCalendar _cal;
	
	public XSDGYear(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = ISO8601Parser.parseISO8601Year(s);
		} catch(DatatypeParseException e){
			throw new DatatypeParseException(e.getMessage(), s, DT, e.getError());
		}
	}
	
	public GregorianCalendar getValue(){
		return _cal;
	}

	public String getCanonicalRepresentation() {
		return ISO8601Parser.getCanonicalRepresentation(getValue(), true, false, false, false, true);
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDGYear dt = new XSDGYear("05+05:23");
		System.err.println(dt.getCanonicalRepresentation());
	}
}
