package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class OWL {
	public static final String NS = "http://www.w3.org/2002/07/owl#";
	
	//classes
	public static final Resource NAMEDINDIVIDUAL = new Resource(NS+"NamedIndividual");
	
	public static final Resource AXIOM = new Resource(NS+"Axiom");
	public static final Resource ANNOTATION = new Resource(NS+"Annotation");
	
	public static final Resource THING = new Resource(NS+"Thing");
	public static final Resource NOTHING = new Resource(NS+"Nothing");
	
	public static final Resource CLASS = new Resource(NS+"Class");
	public static final Resource DEPRECATEDCLASS = new Resource(NS+"DeprecatedClass");
	
	public static final Resource DEPRECATEDPROPERTY = new Resource(NS+"DeprecatedProperty");
	public static final Resource ANNOTATIONPROPERTY = new Resource(NS+"AnnotationProperty");
	public static final Resource ONTOLOGYPROPERTY = new Resource(NS+"OntologyProperty");
	
	public static final Resource INVERSEFUNCTIONALPROPERTY = new Resource(NS+"InverseFunctionalProperty");
	public static final Resource FUNCTIONALPROPERTY = new Resource(NS+"FunctionalProperty");
	public static final Resource TRANSITIVEPROPERTY = new Resource(NS+"TransitiveProperty");
	public static final Resource SYMMETRICPROPERTY = new Resource(NS+"SymmetricProperty");
	public static final Resource ASYMMETRICPROPERTY = new Resource(NS+"AsymmetricProperty");
	public static final Resource REFLEXIVEPROPERTY = new Resource(NS+"ReflexiveProperty");
	public static final Resource IRREFLEXIVEPROPERTY = new Resource(NS+"IrreflexiveProperty");
	
	public static final Resource DATATYPEPROPERTY = new Resource(NS+"DatatypeProperty");
	public static final Resource OBJECTPROPERTY = new Resource(NS+"ObjectProperty");

	public static final Resource ONTOLOGY = new Resource(NS+"Ontology");
	
	public static final Resource RESTRICTION = new Resource(NS+"Restriction");
	
	public static final Resource DATARANGE = new Resource(NS+"DataRange");
	
	public static final Resource NEGATIVEPROPERTYASSERTION = new Resource(NS+"NegativePropertyAssertion");
	
	public static final Resource ALLDISJOINTCLASSES = new Resource(NS+"AllDisjointClasses");
	public static final Resource ALLDISJOINTPROPERTIES = new Resource(NS+"AllDisjointProperties");
	public static final Resource ALLDIFFERENT = new Resource(NS+"AllDifferent");
	
	
	//properties
	
	public static final Resource HASSELF = new Resource(NS+"hasSelf");
	
	public static final Resource TOPOBJECTPROPERTY = new Resource(NS+"topObjectProperty");
	public static final Resource BOTTOMOBJECTPROPERTY = new Resource(NS+"bottomObjectProperty");
	
	public static final Resource TOPDATATPROPERTY = new Resource(NS+"topDataProperty");
	public static final Resource BOTTOMDATATPROPERTY = new Resource(NS+"bottomDataProperty");
	
	public static final Resource DEPRECATED = new Resource(NS+"deprecated");
	
	public static final Resource SAMEAS = new Resource(NS+"sameAs");
	
	public static final Resource EQUIVALENTCLASS = new Resource(NS+"equivalentClass");
	public static final Resource EQUIVALENTPROPERTY = new Resource(NS+"equivalentProperty");
	
	public static final Resource INVERSEOF = new Resource(NS+"inverseOf");

	public static final Resource ONPROPERTY = new Resource(NS+"onProperty");
	public static final Resource ONCLASS = new Resource(NS+"onClass");
	
	public static final Resource SOMEVALUESFROM = new Resource(NS+"someValuesFrom");
	public static final Resource ALLVALUESFROM = new Resource(NS+"allValuesFrom");
	public static final Resource HASVALUE = new Resource(NS+"hasValue");
	
	public static final Resource MINCARDINALITY = new Resource(NS+"minCardinality");
	public static final Resource MAXCARDINALITY = new Resource(NS+"maxCardinality");
	public static final Resource CARDINALITY = new Resource(NS+"cardinality");
	
	public static final Resource MINQUALIFIEDCARDINALITY = new Resource(NS+"minQualifiedCardinality");
	public static final Resource MAXQUALIFIEDCARDINALITY = new Resource(NS+"maxQualifiedCardinality");
	public static final Resource QUALIFIEDCARDINALITY = new Resource(NS+"qualifiedCardinality");
	
	public static final Resource DISJOINTWITH = new Resource(NS+"disjointWith");
	public static final Resource DISTINCTMEMBERS = new Resource(NS+"distinctMembers");
	public static final Resource DIFFERENTFROM = new Resource(NS+"differentFrom");
	
	public static final Resource ONEOF = new Resource(NS+"oneOf");
	
	public static final Resource UNIONOF = new Resource(NS+"unionOf");
	public static final Resource INTERSECTIONOF = new Resource(NS+"intersectionOf");
	public static final Resource COMPLEMENTOF = new Resource(NS+"complementOf");

	public static final Resource IMPORTS = new Resource(NS+"imports");
	
	public static final Resource BACKWARDCOMPATIBLEWITH = new Resource(NS+"backwardCompatibleWith");
	public static final Resource INCOMPATIBLEWITH = new Resource(NS+"incompatibleWith");
	
	public static final Resource PRIORVERSION = new Resource(NS+"priorVersion");
	public static final Resource VERSIONINFO = new Resource(NS+"versionInfo");
	public static final Resource VERSIONIRI = new Resource(NS+"versionIRI");
	
	public static final Resource DATATYPECOMPLEMENTOF = new Resource(NS+"datatypeComplementOf");
	
	public static final Resource WITHRESTRICTIONS = new Resource(NS+"withRestrictions");
	
	public static final Resource DISJOINTUNIONOF = new Resource(NS+"disjointUnionOf");
	public static final Resource PROPERTYDISJOINTWITH = new Resource(NS+"propertyDisjointWith");

	public static final Resource ANNOTATEDSOURCE = new Resource(NS+"annotatedSource");
	public static final Resource ANNOTATEDPROPERTY = new Resource(NS+"annotatedProperty");
	public static final Resource ANNOTATEDTARGET = new Resource(NS+"annotatedTarget");
	
	public static final Resource SOURCEINDIVIDUAL = new Resource(NS+"sourceIndividual");
	public static final Resource ASSERTIONPROPERTY = new Resource(NS+"assertionProperty");
	public static final Resource TARGETINDIVIDUAL = new Resource(NS+"targetIndividual");
	public static final Resource TARGETVALUE = new Resource(NS+"targetValue");
	
	public static final Resource HASKEY = new Resource(NS+"hasKey");
	
	public static final Resource MEMBERS = new Resource(NS+"members");
	
	public static final Resource PROPERTYCHAINAXIOM = new Resource(NS+"propertyChainAxiom");
	
	//datatypes
	public static final Resource RATIONAL = new Resource(NS+"rational");
	public static final Resource REAL= new Resource(NS+"real");
}
