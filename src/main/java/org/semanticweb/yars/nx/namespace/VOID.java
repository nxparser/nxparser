/**
 * 
 */
package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

/**
 * @author juergen, aidhog
 *
 */
public class VOID {
	public static final String NS = "http://rdfs.org/ns/void#";
	
	//Classes
	public final static Resource DATASET = new Resource(NS+"Dataset");
	public final static Resource DATASETDESCRIPTION = new Resource(NS+"DatasetDescription");
	public final static Resource LINKSET = new Resource(NS+"Linkset");
	public final static Resource TECHNICALFEATURE = new Resource(NS+"TechnicalFeature");

	//Properties
	public final static Resource CLASS = new Resource(NS+"class");
	public final static Resource CLASSPARTITION = new Resource(NS+"classPartition");
	public final static Resource CLASSES = new Resource(NS+"classes");
	public final static Resource DATADUMP = new Resource(NS+"dataDump");
	public final static Resource DISTINCTOBJECTS = new Resource(NS+"distinctObjects");
	public final static Resource DISTINCTSUBJECTS = new Resource(NS+"distinctSubjects");
	public final static Resource DOCUMENTS = new Resource(NS+"documents");
	public final static Resource ENTITIES = new Resource(NS+"entities");
	public final static Resource EXAMPLERESOURCE = new Resource(NS+"exampleResource");
	public final static Resource FEATURE = new Resource(NS+"feature");
	public final static Resource INDATASET = new Resource(NS+"inDataset");
	public final static Resource LINKPREDICATE = new Resource(NS+"linkPredicate");
	public final static Resource OBJECTSTARGET = new Resource(NS+"objectsTarget");
	public final static Resource OPENSEARCHDESCRIPTION = new Resource(NS+"openSearchDescription");
	public final static Resource PROPERTIES = new Resource(NS+"properties");
	public final static Resource PROPERTY = new Resource(NS+"property");
	public final static Resource PROPERTYPARTITION = new Resource(NS+"propertyPartition");
	public final static Resource ROOTRESOURCE = new Resource(NS+"rootResource");
	public final static Resource SPARQLENDPOINT = new Resource(NS+"sparqlEndpoint");
	public final static Resource SUBJECTSTARGET = new Resource(NS+"subjectsTarget");
	public final static Resource TARGET = new Resource(NS+"target");
	public final static Resource TRIPLES = new Resource(NS+"triples");
	public final static Resource URILOOKUPENDPOINT = new Resource(NS+"uriLookupEndpoint");
	public final static Resource URIREGEXPATTERN = new Resource(NS+"uriRegexPattern");
	public final static Resource URISPACE = new Resource(NS+"uriSpace");
	public final static Resource VOCABULARY = new Resource(NS+"vocabulary");
}
