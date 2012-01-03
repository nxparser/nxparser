package org.semanticweb.yars.nx.dt;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.binary.XSDBase64Binary;
import org.semanticweb.yars.nx.dt.binary.XSDHexBinary;
import org.semanticweb.yars.nx.dt.bool.XSDBoolean;
import org.semanticweb.yars.nx.dt.datetime.XSDDate;
import org.semanticweb.yars.nx.dt.datetime.XSDDateTime;
import org.semanticweb.yars.nx.dt.datetime.XSDDateTimeStamp;
import org.semanticweb.yars.nx.dt.datetime.XSDGDay;
import org.semanticweb.yars.nx.dt.datetime.XSDGMonth;
import org.semanticweb.yars.nx.dt.datetime.XSDGMonthDay;
import org.semanticweb.yars.nx.dt.datetime.XSDGYear;
import org.semanticweb.yars.nx.dt.datetime.XSDGYearMonth;
import org.semanticweb.yars.nx.dt.datetime.XSDTime;
import org.semanticweb.yars.nx.dt.numeric.XSDByte;
import org.semanticweb.yars.nx.dt.numeric.XSDDecimal;
import org.semanticweb.yars.nx.dt.numeric.XSDDouble;
import org.semanticweb.yars.nx.dt.numeric.XSDFloat;
import org.semanticweb.yars.nx.dt.numeric.XSDInt;
import org.semanticweb.yars.nx.dt.numeric.XSDInteger;
import org.semanticweb.yars.nx.dt.numeric.XSDLong;
import org.semanticweb.yars.nx.dt.numeric.XSDNegativeInteger;
import org.semanticweb.yars.nx.dt.numeric.XSDNonNegativeInteger;
import org.semanticweb.yars.nx.dt.numeric.XSDNonPositiveInteger;
import org.semanticweb.yars.nx.dt.numeric.XSDPositiveInteger;
import org.semanticweb.yars.nx.dt.numeric.XSDShort;
import org.semanticweb.yars.nx.dt.numeric.XSDUnsignedByte;
import org.semanticweb.yars.nx.dt.numeric.XSDUnsignedInt;
import org.semanticweb.yars.nx.dt.numeric.XSDUnsignedLong;
import org.semanticweb.yars.nx.dt.numeric.XSDUnsignedShort;
import org.semanticweb.yars.nx.dt.string.XSDLanguage;
import org.semanticweb.yars.nx.dt.string.XSDNCName;
import org.semanticweb.yars.nx.dt.string.XSDNMToken;
import org.semanticweb.yars.nx.dt.string.XSDName;
import org.semanticweb.yars.nx.dt.string.XSDNormalisedString;
import org.semanticweb.yars.nx.dt.string.XSDString;
import org.semanticweb.yars.nx.dt.string.XSDToken;
import org.semanticweb.yars.nx.dt.uri.XSDAnyURI;
import org.semanticweb.yars.nx.dt.xml.RDFXMLLiteral;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.XSD;

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
	public static Datatype<? extends Object> getDatatype(Literal l) throws DatatypeParseException{
		return getDatatype(l.getUnescapedData(), l.getDatatype());
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
				return new XSDString(lex);
			} else if(dt.equals(XSD.BOOLEAN)){
				return new XSDBoolean(lex);
			} else if(dt.equals(XSD.INTEGER)){
				return new XSDInteger(lex);
			} else if(dt.equals(XSD.DATETIME)){
				return new XSDDateTime(lex);
			} else if(dt.equals(XSD.DATETIMESTAMP)){
				return new XSDDateTimeStamp(lex);
			} else if(dt.equals(XSD.DATE)){
				return new XSDDate(lex);
			} else if(dt.equals(XSD.TIME)){
				return new XSDTime(lex);
			} else if(dt.equals(XSD.INT)){
				return new XSDInt(lex);
			} else if(dt.equals(XSD.LONG)){
				return new XSDLong(lex);
			} else if(dt.equals(XSD.SHORT)){
				return new XSDShort(lex);
			} else if(dt.equals(XSD.BYTE)){
				return new XSDByte(lex);
			} else if(dt.equals(XSD.DECIMAL)){
				return new XSDDecimal(lex);
			} else if(dt.equals(XSD.FLOAT)){
				return new XSDFloat(lex);
			} else if(dt.equals(XSD.DOUBLE)){
				return new XSDDouble(lex);
			} else if(dt.equals(XSD.GYEARMONTH)){
				return new XSDGYearMonth(lex);
			} else if(dt.equals(XSD.GYEAR)){
				return new XSDGYear(lex);
			} else if(dt.equals(XSD.GMONTHDAY)){
				return new XSDGMonthDay(lex);
			} else if(dt.equals(XSD.GMONTH)){
				return new XSDGMonth(lex);
			} else if(dt.equals(XSD.GDAY)){
				return new XSDGDay(lex);
			} else if(dt.equals(XSD.HEXBINARY)){
				return new XSDHexBinary(lex);
			} else if(dt.equals(XSD.BASE64BINARY)){
				return new XSDBase64Binary(lex);
			} else if(dt.equals(XSD.TOKEN)){
				return new XSDToken(lex);
			} else if(dt.equals(XSD.NMTOKEN)){
				return new XSDNMToken(lex);
			} else if(dt.equals(XSD.NAME)){
				return new XSDName(lex);
			} else if(dt.equals(XSD.NCNAME)){
				return new XSDNCName(lex);
			} else if(dt.equals(XSD.NONNEGATIVEINTEGER)){
				return new XSDNonNegativeInteger(lex);
			} else if(dt.equals(XSD.POSITIVEINTEGER)){
				return new XSDPositiveInteger(lex);
			} else if(dt.equals(XSD.NONPOSITIVEINTEGER)){
				return new XSDNonPositiveInteger(lex);
			} else if(dt.equals(XSD.NEGATIVEINTEGER)){
				return new XSDNegativeInteger(lex);
			} else if(dt.equals(XSD.NORMALIZEDSTRING)){
				return new XSDNormalisedString(lex);
			} else if(dt.equals(XSD.ANYURI)){
				return new XSDAnyURI(lex);
			} else if(dt.equals(XSD.LANGUAGE)){
				return new XSDLanguage(lex);
			} else if(dt.equals(XSD.UNSIGNEDLONG)){
				return new XSDUnsignedLong(lex);
			} else if(dt.equals(XSD.UNSIGNEDINT)){
				return new XSDUnsignedInt(lex);
			} else if(dt.equals(XSD.UNSIGNEDSHORT)){
				return new XSDUnsignedShort(lex);
			} else if(dt.equals(XSD.UNSIGNEDBYTE)){
				return new XSDUnsignedByte(lex);
			} 
		} else if(dt.equals(RDF.XMLLITERAL)){
			return new RDFXMLLiteral(lex);
		}
		return null;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		System.err.println(getDatatype("2005-03-04",XSD.DATETIME));
	}
}
