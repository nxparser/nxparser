package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:gMonthDay datatype
 * @author aidhog
 *
 */
public class XsdGMonthDay extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.GMONTHDAY;
	private GregorianCalendar _cal;
	
	public XsdGMonthDay(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = Iso8601Parser.parseISO8601MonthDay(s);
		} catch(DatatypeParseException e){
			throw new DatatypeParseException(e.getMessage(), s, DT, e.getError());
		}
	}
	
	public GregorianCalendar getValue(){
		return _cal;
	}

	public String getCanonicalRepresentation() {
		return Iso8601Parser.getCanonicalRepresentation(getValue(), false, true, true, false, true);
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdGMonthDay dt = new XsdGMonthDay("--05-31Z");
		System.err.println(dt.getCanonicalRepresentation());
	}
}
