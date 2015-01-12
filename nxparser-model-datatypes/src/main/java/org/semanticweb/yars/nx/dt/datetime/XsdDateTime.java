package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:dateTime datatype
 * @author aidhog
 *
 */
public class XsdDateTime extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.DATETIME;
	private GregorianCalendar _cal;
	
	public XsdDateTime(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = Iso8601Parser.parseISO8601DateTime(s);
		} catch(DatatypeParseException e){
			throw new DatatypeParseException(e.getMessage(), s, DT, e.getError());
		}
	}
	
	public GregorianCalendar getValue(){
		return _cal;
	}

	public String getCanonicalRepresentation() {
		return Iso8601Parser.getCanonicalRepresentation(getValue(), true, true, true, true, true);
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XsdDateTime dt = new XsdDateTime("1600-02-29T07:17:55Z");
		System.err.println(dt.getCanonicalRepresentation());
	}
	
	/**
	 * Add leading zeros
	 * @return
	 */
	public static String leadZeros(int v, int l){
		String val = Integer.toString(v);
		while(val.length()<l)
			val = "0"+val;
		
		return val;
	}
}
