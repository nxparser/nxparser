package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class XSD {
	public static final String NS = "http://www.w3.org/2001/XMLSchema#";

	public static final Resource STRING = new Resource(NS+"string");
	public static final Resource NORMALIZEDSTRING = new Resource(NS+"normalizedString");
	public static final Resource TOKEN = new Resource(NS+"token");
	
	public static final Resource NAME = new Resource(NS+"Name");
	public static final Resource NCNAME = new Resource(NS+"NCName");
	public static final Resource NMTOKEN = new Resource(NS+"NMTOKEN");
	
	public static final Resource LANGUAGE = new Resource(NS+"language");

	public static final Resource BOOLEAN = new Resource(NS+"boolean");

	public static final Resource DECIMAL = new Resource(NS+"decimal");
	public static final Resource FLOAT = new Resource(NS+"float");
	public static final Resource DOUBLE = new Resource(NS+"double");
	
	public static final Resource INTEGER = new Resource(NS+"integer");
	public static final Resource NONPOSITIVEINTEGER = new Resource(NS+"nonPositiveInteger");
	public static final Resource NONNEGATIVEINTEGER = new Resource(NS+"nonNegativeInteger");
	public static final Resource POSITIVEINTEGER = new Resource(NS+"positiveInteger");
	public static final Resource NEGATIVEINTEGER = new Resource(NS+"negativeInteger");
	
	public static final Resource INT = new Resource(NS+"int");
	public static final Resource UNSIGNEDINT = new Resource(NS+"unsignedInt");
	
	public static final Resource LONG = new Resource(NS+"long");
	public static final Resource UNSIGNEDLONG = new Resource(NS+"unsignedLong");
	
	public static final Resource SHORT = new Resource(NS+"short");
	public static final Resource UNSIGNEDSHORT = new Resource(NS+"unsignedShort");
	
	public static final Resource BYTE = new Resource(NS+"byte");
	public static final Resource UNSIGNEDBYTE = new Resource(NS+"unsignedByte");
	
	public static final Resource DATETIME = new Resource(NS+"dateTime");
	public static final Resource DATETIMESTAMP = new Resource(NS+"dateTimeStamp");
	public static final Resource TIME = new Resource(NS+"time");
	public static final Resource DATE = new Resource(NS+"date");
	
	public static final Resource GYEARMONTH = new Resource(NS+"gYearMonth");
	public static final Resource GYEAR = new Resource(NS+"gYear");
	public static final Resource GMONTHDAY = new Resource(NS+"gMonthDay");
	public static final Resource GDAY = new Resource(NS+"gDay");
	public static final Resource GMONTH = new Resource(NS+"gMonth");
	
	public static final Resource HEXBINARY = new Resource(NS+"hexBinary");
	public static final Resource BASE64BINARY = new Resource(NS+"base64Binary");
	
	public static final Resource ANYURI = new Resource(NS+"anyURI");
	
}

