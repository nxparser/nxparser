package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:time datetype
 * @author aidhog
 *
 */
public class XsdTime extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.TIME;
	private GregorianCalendar _cal;
	
	public XsdTime(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = Iso8601Parser.parseISO8601Time(s);
		} catch(DatatypeParseException e){
			throw new DatatypeParseException(e.getMessage(), s, DT, e.getError());
		}
	}
	
	public GregorianCalendar getValue(){
		return _cal;
	}

	public String getCanonicalRepresentation() {
		return Iso8601Parser.getCanonicalRepresentation(getValue(), false, false, false, true, true);
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdTime dt = new XsdTime("23:34:45+05:00");
		System.err.println(dt.getCanonicalRepresentation());
	}
	
	/**
	 * Remove trailing zeros
	 * @return
	 */
	public static String removeTrailingZeros(String s){
		while(s.endsWith("0"))
			s = s.substring(0, s.length()-1);
		return s;
	}
}
