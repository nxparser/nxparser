package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class SKOS {
	public static final String NS = "http://www.w3.org/2004/02/skos/core#";
	
	public final static Resource BROADER = new Resource(NS+"broader");
	public final static Resource NARROWER = new Resource(NS+"narrower");
	public final static Resource RELATED = new Resource(NS+"related");

}
