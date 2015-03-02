package org.semanticweb.yars.nx.dt;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.binary.XsdBase64Binary;
import org.semanticweb.yars.nx.dt.binary.XsdHexBinary;
import org.semanticweb.yars.nx.dt.bool.XsdBoolean;
import org.semanticweb.yars.nx.dt.datetime.XsdDate;
import org.semanticweb.yars.nx.dt.datetime.XsdDateTime;
import org.semanticweb.yars.nx.dt.datetime.XsdDateTimeStamp;
import org.semanticweb.yars.nx.dt.datetime.XsdGDay;
import org.semanticweb.yars.nx.dt.datetime.XsdGMonth;
import org.semanticweb.yars.nx.dt.datetime.XsdGMonthDay;
import org.semanticweb.yars.nx.dt.datetime.XsdGYear;
import org.semanticweb.yars.nx.dt.datetime.XsdGYearMonth;
import org.semanticweb.yars.nx.dt.datetime.XsdTime;
import org.semanticweb.yars.nx.dt.numeric.XsdByte;
import org.semanticweb.yars.nx.dt.numeric.XsdDecimal;
import org.semanticweb.yars.nx.dt.numeric.XsdDouble;
import org.semanticweb.yars.nx.dt.numeric.XsdFloat;
import org.semanticweb.yars.nx.dt.numeric.XsdInt;
import org.semanticweb.yars.nx.dt.numeric.XsdInteger;
import org.semanticweb.yars.nx.dt.numeric.XsdLong;
import org.semanticweb.yars.nx.dt.numeric.XsdNegativeInteger;
import org.semanticweb.yars.nx.dt.numeric.XsdNonNegativeInteger;
import org.semanticweb.yars.nx.dt.numeric.XsdNonPositiveInteger;
import org.semanticweb.yars.nx.dt.numeric.XsdPositiveInteger;
import org.semanticweb.yars.nx.dt.numeric.XsdShort;
import org.semanticweb.yars.nx.dt.numeric.XsdUnsignedByte;
import org.semanticweb.yars.nx.dt.numeric.XsdUnsignedInt;
import org.semanticweb.yars.nx.dt.numeric.XsdUnsignedLong;
import org.semanticweb.yars.nx.dt.numeric.XsdUnsignedShort;
import org.semanticweb.yars.nx.dt.string.XsdLanguage;
import org.semanticweb.yars.nx.dt.string.XsdNCName;
import org.semanticweb.yars.nx.dt.string.XsdNMToken;
import org.semanticweb.yars.nx.dt.string.XsdName;
import org.semanticweb.yars.nx.dt.string.XsdNormalisedString;
import org.semanticweb.yars.nx.dt.string.XsdString;
import org.semanticweb.yars.nx.dt.string.XsdToken;
import org.semanticweb.yars.nx.dt.uri.XsdAnyURI;
import org.semanticweb.yars.nx.dt.xml.RdfXmlLiteral;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.XSD;
import org.semanticweb.yars.nx.parser.ParseException;

/**
 * XSD Datatype factory
 * @author aidhog
 *
 */

public class DatatypeFactory {
	
	/**
	 * Get object representing datatype-value of literal.
	 *
	 * @return datatype value or null if (i) unsupported datatype; (ii) plain literal (w/wo/ lang tag)
	 * @throws DatatypeParseException if supported datatype with bad syntax
	 */
	public static Datatype<? extends Object> getDatatype(Literal l) throws DatatypeParseException, ParseException {
		return getDatatype(l.getLabel(), l.getDatatype());
	}

	/**
	 * Get object representing datatype-value of lexical string/datatype URI pair.
	 *
	 * @return datatype value or null if (i) unsupported datatype; (ii) lex is null or dt is null
	 * @throws DatatypeParseException if supported datatype with bad syntax
	 */
	public static Datatype<? extends Object> getDatatype(String lex, Resource dt) throws DatatypeParseException{
		if(dt==null || lex==null)
			return null;
		else if(dt.toString().startsWith(XSD.NS)){
			if(dt.equals(XSD.STRING)){
				return new XsdString(lex);
			} else if(dt.equals(XSD.BOOLEAN)){
				return new XsdBoolean(lex);
			} else if(dt.equals(XSD.INTEGER)){
				return new XsdInteger(lex);
			} else if(dt.equals(XSD.DATETIME)){
				return new XsdDateTime(lex);
			} else if(dt.equals(XSD.DATETIMESTAMP)){
				return new XsdDateTimeStamp(lex);
			} else if(dt.equals(XSD.DATE)){
				return new XsdDate(lex);
			} else if(dt.equals(XSD.TIME)){
				return new XsdTime(lex);
			} else if(dt.equals(XSD.INT)){
				return new XsdInt(lex);
			} else if(dt.equals(XSD.LONG)){
				return new XsdLong(lex);
			} else if(dt.equals(XSD.SHORT)){
				return new XsdShort(lex);
			} else if(dt.equals(XSD.BYTE)){
				return new XsdByte(lex);
			} else if(dt.equals(XSD.DECIMAL)){
				return new XsdDecimal(lex);
			} else if(dt.equals(XSD.FLOAT)){
				return new XsdFloat(lex);
			} else if(dt.equals(XSD.DOUBLE)){
				return new XsdDouble(lex);
			} else if(dt.equals(XSD.GYEARMONTH)){
				return new XsdGYearMonth(lex);
			} else if(dt.equals(XSD.GYEAR)){
				return new XsdGYear(lex);
			} else if(dt.equals(XSD.GMONTHDAY)){
				return new XsdGMonthDay(lex);
			} else if(dt.equals(XSD.GMONTH)){
				return new XsdGMonth(lex);
			} else if(dt.equals(XSD.GDAY)){
				return new XsdGDay(lex);
			} else if(dt.equals(XSD.HEXBINARY)){
				return new XsdHexBinary(lex);
			} else if(dt.equals(XSD.BASE64BINARY)){
				return new XsdBase64Binary(lex);
			} else if(dt.equals(XSD.TOKEN)){
				return new XsdToken(lex);
			} else if(dt.equals(XSD.NMTOKEN)){
				return new XsdNMToken(lex);
			} else if(dt.equals(XSD.NAME)){
				return new XsdName(lex);
			} else if(dt.equals(XSD.NCNAME)){
				return new XsdNCName(lex);
			} else if(dt.equals(XSD.NONNEGATIVEINTEGER)){
				return new XsdNonNegativeInteger(lex);
			} else if(dt.equals(XSD.POSITIVEINTEGER)){
				return new XsdPositiveInteger(lex);
			} else if(dt.equals(XSD.NONPOSITIVEINTEGER)){
				return new XsdNonPositiveInteger(lex);
			} else if(dt.equals(XSD.NEGATIVEINTEGER)){
				return new XsdNegativeInteger(lex);
			} else if(dt.equals(XSD.NORMALIZEDSTRING)){
				return new XsdNormalisedString(lex);
			} else if(dt.equals(XSD.ANYURI)){
				return new XsdAnyURI(lex);
			} else if(dt.equals(XSD.LANGUAGE)){
				return new XsdLanguage(lex);
			} else if(dt.equals(XSD.UNSIGNEDLONG)){
				return new XsdUnsignedLong(lex);
			} else if(dt.equals(XSD.UNSIGNEDINT)){
				return new XsdUnsignedInt(lex);
			} else if(dt.equals(XSD.UNSIGNEDSHORT)){
				return new XsdUnsignedShort(lex);
			} else if(dt.equals(XSD.UNSIGNEDBYTE)){
				return new XsdUnsignedByte(lex);
			} 
		} else if(dt.equals(RDF.XMLLITERAL)){
			return new RdfXmlLiteral(lex);
		}
		return null;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		System.err.println(getDatatype("2005-03-04",XSD.DATETIME));
	}
}
