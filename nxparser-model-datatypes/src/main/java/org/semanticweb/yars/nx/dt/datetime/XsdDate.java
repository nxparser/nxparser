package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:date datatype
 * @author aidhog
 *
 */
		
public class XsdDate extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.DATE;
	private GregorianCalendar _cal;
	
	public XsdDate(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = Iso8601Parser.parseISO8601Date(s);
		} catch(DatatypeParseException e){
			throw new DatatypeParseException(e.getMessage(), s, DT, e.getError());
		}
	}
	
	public GregorianCalendar getValue(){
		return _cal;
	}

	public String getCanonicalRepresentation() {
		return Iso8601Parser.getCanonicalRepresentation(getValue(), true, true, true, false, true);
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdDate dt = new XsdDate("-1543-12-12-05:41");
		System.err.println(dt.getCanonicalRepresentation());
	}
}
