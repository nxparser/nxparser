package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class DC {
	public static final String NS = "http://purl.org/dc/elements/1.1/";

	public final static Resource TITLE = new Resource(NS+"title");
	public final static Resource DESCRIPTION = new Resource(NS+"description");
	public final static Resource SUBJECT = new Resource(NS+"subject");
	public final static Resource FORMAT = new Resource(NS+"format");
	public final static Resource DATE = new Resource(NS+"date");
	public final static Resource CREATOR = new Resource(NS+"creator");

}
