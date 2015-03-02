package org.semanticweb.yars.nx.dt;

import java.util.TreeSet;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * Datatype abstract class
 * @author aidhog
 *
 * @param <E>
 */
public abstract class Datatype<E> {
	public static final Resource[] SUPPORTED_STD_DTS = {
		XSD.STRING, XSD.BOOLEAN, XSD.INTEGER, XSD.DATETIME, XSD.DATE, XSD.DATETIMESTAMP,
		XSD.TIME, XSD.INT, XSD.LONG, XSD.SHORT, XSD.BYTE, XSD.DECIMAL,
		XSD.FLOAT, XSD.DOUBLE, XSD.GYEARMONTH, XSD.GYEAR, XSD.GMONTHDAY,
		XSD.GMONTH, XSD.GDAY, XSD.HEXBINARY, XSD.BASE64BINARY, XSD.TOKEN,
		XSD.NMTOKEN, XSD.NAME, XSD.NCNAME, XSD.NONNEGATIVEINTEGER, 
		XSD.POSITIVEINTEGER, XSD.NONPOSITIVEINTEGER, XSD.NEGATIVEINTEGER,
		XSD.NORMALIZEDSTRING, XSD.ANYURI, XSD.LANGUAGE, XSD.UNSIGNEDLONG,
		XSD.UNSIGNEDINT, XSD.UNSIGNEDSHORT, XSD.UNSIGNEDBYTE, RDF.XMLLITERAL
	};
	
	public static final TreeSet<Resource> SUPPORTED_STD_DTS_TS = new TreeSet<Resource>();
	static {
		for(Resource r: SUPPORTED_STD_DTS)
			SUPPORTED_STD_DTS_TS.add(r);
	};
	
	/**
	 * Get a Java object representing the value of the Datatype
	 * @return
	 */
	public abstract E getValue();
	
	/**
	 * Get the canonicalised form of the lexical value.
	 * @return
	 */
	public abstract String getCanonicalRepresentation();
	
	public static boolean isSupportedStandardDatatype(Resource dt){
		if(SUPPORTED_STD_DTS_TS.contains(dt)){
			return true;
		}
		return false;
	}
	
	public static boolean isSupportedStandardDatatype(Node dt){
		if(dt instanceof Resource){
			return isSupportedStandardDatatype((Resource)dt);
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Datatype))
			return false;
		else
			return getCanonicalRepresentation().equals(
					((Datatype) other).getCanonicalRepresentation());
	}
	
	@Override
	public int hashCode() {
		return getCanonicalRepresentation().hashCode();
	}
}