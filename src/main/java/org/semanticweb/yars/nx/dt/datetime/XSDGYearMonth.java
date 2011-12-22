package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:gYearMonth
 * @author aidhog
 *
 */
public class XSDGYearMonth extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.GYEARMONTH;
	private GregorianCalendar _cal;
	
	public XSDGYearMonth(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = ISO8601Parser.parseISO8601YearMonth(s);
		} catch(DatatypeParseException e){
			throw new DatatypeParseException(e.getMessage(), s, DT, e.getError());
		}
	}
	
	public GregorianCalendar getValue(){
		return _cal;
	}

	public String getCanonicalRepresentation() {
		return ISO8601Parser.getCanonicalRepresentation(getValue(), true, true, false, false, true);
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDGYearMonth dt = new XSDGYearMonth("-1234-05");
		System.err.println(dt.getCanonicalRepresentation());
	}
}
