/**
 * 
 */
package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

/**
 * @author juergen
 *
 */
public class DCTERMS {
	public static final String NS = "http://purl.org/dc/terms/";

	public final static Resource TITLE = new Resource(NS+"title");
	public final static Resource DESCRIPTION = new Resource(NS+"description");
	public final static Resource DATE = new Resource(NS+"date");
}
