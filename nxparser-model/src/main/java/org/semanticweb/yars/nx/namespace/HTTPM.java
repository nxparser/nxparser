package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class HTTPM {
	public final static String NS = "http://www.w3.org/2011/http-methods#";

	public static final Resource HTTP_GET = new Resource(NS + "GET");
	public static final Resource HTTP_PUT = new Resource(NS + "PUT");
	public static final Resource HTTP_POST = new Resource(NS + "POST");
	public static final Resource HTTP_DELETE = new Resource(NS + "DELETE");
}