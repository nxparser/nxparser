package org.semanticweb.yars.nx.dt.datetime;

import java.util.GregorianCalendar;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:dateTimeStamp datatype
 * @author aidhog
 *
 */
public class XsdDateTimeStamp extends Datatype<GregorianCalendar>{
	public static final Resource DT = XSD.DATETIME;
	private GregorianCalendar _cal;
	
	public XsdDateTimeStamp(String s) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		try{
			_cal = Iso8601Parser.parseISO8601DateTime(s, true);
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
