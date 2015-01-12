package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class RSS {
	public static final String NS = "http://purl.org/rss/1.0/";

	public final static Resource CHANNEL = new Resource(NS+"channel");

	public final static Resource ITEM = new Resource(NS+"item");

	public final static Resource TITLE = new Resource(NS+"title");
	public final static Resource DESCRIPTION = new Resource(NS+"description");

}
