package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;

public class VCARD {
	public static final String NS = "http://www.w3.org/2001/vcard-rdf/3.0#";

	public final static Node ADR = new Resource(NS+"ADR");
	public final static Node TEL = new Resource(NS+"TEL");
	
	
	public final static Node STREET = new Resource(NS+"Street");
	public final static Node LOCALITY = new Resource(NS+"Locality");
	public final static Node PCODE = new Resource(NS+"Pcode");
	public final static Node COUNTRY = new Resource(NS+"Country");
	public final static Node EXTADD = new Resource(NS+"Extadd");
	public final static Node REGION = new Resource(NS+"Region");
	public final static Node POBOX = new Resource(NS+"Pobox");
	
	public final static Node POSTAL = new Resource(NS+"postal");
	
	public final static Node VOICE = new Resource(NS+"voice");
	public final static Node FAX = new Resource(NS+"fax");
	
}
