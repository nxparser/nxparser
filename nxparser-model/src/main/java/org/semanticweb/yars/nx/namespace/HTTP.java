package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

/**
 * The HTTP-in-RDF 1.0 vocabulary.
 * 
 * @see <a href="http://www.w3.org/TR/HTTP-in-RDF10/">The corresponding Working
 *      Draft at the W3C</a>
 *
 */
public class HTTP {

	// Generated using:
	// rapper http://www.w3.org/2011/http | awk 'FS=" " { print $1 } '|
	// sort -u | grep '2011/http#' | awk '{ sub(".*#", ""); sub(">",""); sav =
	// $0; gsub("-","_");c=toupper($0); print "public static final Resource", c,
	// "= new Resource(NS +", "\"" sav "\");" ;}'

	public final static String NS = "http://www.w3.org/2011/http#";

	public static final Resource ABSOLUTEPATH = new Resource(NS + "absolutePath");
	public static final Resource ABSOLUTEURI = new Resource(NS + "absoluteURI");
	public static final Resource AUTHORITY = new Resource(NS + "authority");
	public static final Resource BODY = new Resource(NS + "body");
	public static final Resource CONNECTION = new Resource(NS + "Connection");
	public static final Resource CONNECTIONAUTHORITY = new Resource(NS + "connectionAuthority");
	public static final Resource ELEMENTNAME = new Resource(NS + "elementName");
	public static final Resource ELEMENTVALUE = new Resource(NS + "elementValue");
	public static final Resource ENTITYHEADER = new Resource(NS + "EntityHeader");
	public static final Resource FIELDNAME = new Resource(NS + "fieldName");
	public static final Resource FIELDVALUE = new Resource(NS + "fieldValue");
	public static final Resource GENERALHEADER = new Resource(NS + "GeneralHeader");
	public static final Resource HDRNAME = new Resource(NS + "hdrName");
	public static final Resource HEADERELEMENT = new Resource(NS + "HeaderElement");
	public static final Resource HEADERELEMENTS = new Resource(NS + "headerElements");
	public static final Resource HEADERNAME = new Resource(NS + "HeaderName");
	public static final Resource HEADERS = new Resource(NS + "headers");
	public static final Resource HTTPVERSION = new Resource(NS + "httpVersion");
	public static final Resource MESSAGE = new Resource(NS + "Message");
	public static final Resource MESSAGEHEADER = new Resource(NS + "MessageHeader");
	public static final Resource METHOD = new Resource(NS + "Method");
	public static final Resource METHODNAME = new Resource(NS + "methodName");
	public static final Resource MTHD = new Resource(NS + "mthd");
	public static final Resource PARAMETER = new Resource(NS + "Parameter");
	public static final Resource PARAMNAME = new Resource(NS + "paramName");
	public static final Resource PARAMS = new Resource(NS + "params");
	public static final Resource PARAMVALUE = new Resource(NS + "paramValue");
	public static final Resource REASONPHRASE = new Resource(NS + "reasonPhrase");
	public static final Resource REQUEST = new Resource(NS + "Request");
	public static final Resource REQUESTHEADER = new Resource(NS + "RequestHeader");
	public static final Resource REQUESTS = new Resource(NS + "requests");
	public static final Resource REQUESTURI = new Resource(NS + "requestURI");
	public static final Resource RESP = new Resource(NS + "resp");
	public static final Resource RESPONSE = new Resource(NS + "Response");
	public static final Resource RESPONSEHEADER = new Resource(NS + "ResponseHeader");
	public static final Resource SC = new Resource(NS + "sc");
	public static final Resource STATUSCODE = new Resource(NS + "StatusCode");
	public static final Resource STATUSCODENUMBER = new Resource(NS + "statusCodeNumber");
	public static final Resource STATUSCODEVALUE = new Resource(NS + "statusCodeValue");
}