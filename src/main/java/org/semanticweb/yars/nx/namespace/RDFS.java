package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class RDFS {
	public static final String NS = "http://www.w3.org/2000/01/rdf-schema#";
	
	//properties
	public static final Resource DOMAIN = new Resource(NS+"domain");
	public static final Resource RANGE = new Resource(NS+"range");
	
	public static final Resource SUBCLASSOF = new Resource(NS+"subClassOf");
	public static final Resource SUBPROPERTYOF = new Resource(NS+"subPropertyOf");
	
	public static final Resource LABEL = new Resource(NS+"label");
	public static final Resource COMMENT = new Resource(NS+"comment");
	
	public static final Resource SEEALSO = new Resource(NS+"seeAlso");
	public static final Resource ISDEFINEDBY = new Resource(NS+"isDefinedBy");
	
	public static final Resource MEMBER = new Resource(NS+"member");
	
	//classes
	public static final Resource RESOURCE = new Resource(NS+"Resource");
	
	public static final Resource CLASS = new Resource(NS+"Class");
	
	public static final Resource CONTAINER = new Resource(NS+"Container");
	public static final Resource CONTAINERMEMBERSHIPPROPERTY = new Resource(NS+"ContainerMembershipProperty");

	public static final Resource LITERAL = new Resource(NS+"Literal");
	public static final Resource DATATYPE = new Resource(NS+"Datatype");

}
