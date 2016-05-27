package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

/**
 * The W3C Linked Data Platform (LDP) Vocabulary.
 * 
 * This ontology provides an informal representation of the concepts and terms
 * as defined in the LDP specification. Consult the LDP specification for
 * normative reference.
 * 
 * : a owl:Ontology .
 * 
 * This NxParser vocabulary is derived from the Turtle serialization of
 * https://www.w3.org/ns/ldp.
 * 
 * Prefixes used in comments:
 * 
 * @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
 * @prefix owl: <http://www.w3.org/2002/07/owl#> .
 * @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
 * @prefix dcterms: <http://purl.org/dc/terms/> .
 * @prefix vs: <http://www.w3.org/2003/06/sw-vocab-status/ns#> .
 * @prefix : <http://www.w3.org/ns/ldp#> .
 * 
 * @SeeAlso http://www.w3.org/2012/ldp
 * @SeeAlso http://www.w3.org/TR/ldp-ucr/
 * @SeeAlso http://www.w3.org/TR/ldp/
 * @SeeAlso http://www.w3.org/TR/ldp-paging/
 * @SeeAlso http://www.w3.org/2011/09/LinkedData/
 * 
 * @author "Felix Leif Keppmann"
 */
public class LDP {

	/**
	 * Namespace of the Linked Data Platform (LDP).
	 */
	public static final String NS = "http://www.w3.org/ns/ldp#";

	/**
	 * A HTTP-addressable resource whose lifecycle is managed by a LDP server.
	 * 
	 * :Resource a rdfs:Class; vs:term_status "stable"; rdfs:isDefinedBy :;
	 * rdfs:label "Resource" .
	 * 
	 */
	public final static Resource RESOURCE = new Resource(NS + "Resource");

	/**
	 * A Linked Data Platform Resource (LDPR) whose state is represented as RDF.
	 * 
	 * :RDFSource a rdfs:Class; rdfs:subClassOf :Resource; vs:term_status
	 * "stable"; rdfs:isDefinedBy :; rdfs:label "RDFSource" .
	 */
	public final static Resource RDF_SOURCE = new Resource(NS + "RDFSource");

	/**
	 * A Linked Data Platform Resource (LDPR) whose state is NOT represented as
	 * RDF.
	 * 
	 * :NonRDFSource a rdfs:Class; rdfs:subClassOf :Resource; vs:term_status
	 * "stable"; rdfs:isDefinedBy :; rdfs:label "NonRDFSource" .
	 */
	public final static Resource NON_RDF_SOURCE = new Resource(NS + "NonRDFSource");

	/**
	 * A Linked Data Platform RDF Source (LDP-RS) that also conforms to
	 * additional patterns and conventions for managing membership. Readers
	 * should refer to the specification defining this ontology for the list of
	 * behaviors associated with it.
	 * 
	 * :Container a rdfs:Class; rdfs:subClassOf :RDFSource; vs:term_status
	 * "stable"; rdfs:isDefinedBy :; rdfs:label "Container" .
	 */
	public final static Resource CONTAINER = new Resource(NS + "Container");

	/**
	 * An LDPC that uses a predefined predicate to simply link to its contained
	 * resources.
	 * 
	 * :BasicContainer a rdfs:Class; rdfs:subClassOf :Container; vs:term_status
	 * "stable"; rdfs:isDefinedBy :; rdfs:label "BasicContainer" .
	 */
	public final static Resource BASIC_CONTAINER = new Resource(NS + "BasicContainer");

	/**
	 * An LDPC that is similar to a LDP-DC but it allows an indirection with the
	 * ability to list as member a resource, such as a URI representing a
	 * real-world object, that is different from the resource that is created.
	 * 
	 * :DirectContainer a rdfs:Class; rdfs:subClassOf :Container; vs:term_status
	 * "stable"; rdfs:isDefinedBy :; rdfs:label "DirectContainer" .
	 */
	public final static Resource DIRECT_CONTAINER = new Resource(NS + "DirectContainer");

	/**
	 * An LDPC that has the flexibility of choosing what form the membership
	 * triples take.
	 * 
	 * :IndirectContainer a rdfs:Class; rdfs:subClassOf :Container; vs:term_status "stable";
	 * rdfs:isDefinedBy :; rdfs:label "IndirectContainer" .
	 */
	public final static Resource INDIRECT_CONTAINER = new Resource(NS + "IndirectContainer");

	/**
	 * Indicates which predicate is used in membership triples, and that the
	 * membership triple pattern is < membership-constant-URI ,
	 * object-of-hasMemberRelation, member-URI >.
	 * 
	 * :hasMemberRelation a rdf:Property; vs:term_status "stable"; rdfs:domain
	 * :Container; rdfs:isDefinedBy :; rdfs:label "hasMemberRelation";
	 * rdfs:range rdf:Property .
	 */
	public final static Resource HAS_MEMBER_RELATION = new Resource(NS + "hasMemberRelation");

	/**
	 * Indicates which predicate is used in membership triples, and that the
	 * membership triple pattern is < member-URI , object-of-isMemberOfRelation,
	 * membership-constant-URI >.
	 * 
	 * :isMemberOfRelation a rdf:Property; vs:term_status "stable"; rdfs:domain
	 * :Container; rdfs:isDefinedBy :; rdfs:label "isMemmberOfRelation";
	 * rdfs:range rdf:Property .
	 */
	public final static Resource IS_MEMBER_OF_RELATION = new Resource(NS + "isMemberOfRelation");

	/**
	 * Indicates the membership-constant-URI in a membership triple. Depending
	 * upon the membership triple pattern a container uses, as indicated by the
	 * presence of ldp:hasMemberRelation or ldp:isMemberOfRelation, the
	 * membership-constant-URI might occupy either the subject or object
	 * position in membership triples.
	 * 
	 * :membershipResource a rdf:Property; vs:term_status "stable"; rdfs:domain
	 * :Container; rdfs:isDefinedBy :; rdfs:label "membershipResource";
	 * rdfs:range rdf:Property .
	 */
	public final static Resource MEMBERSHIP_RESOURCE = new Resource(NS + "membershipResource");

	/**
	 * Indicates which triple in a creation request should be used as the
	 * member-URI value in the membership triple added when the creation request
	 * is successful.
	 * 
	 * :insertedContentRelation a rdf:Property; vs:term_status "stable";
	 * rdfs:domain :Container; rdfs:isDefinedBy :; rdfs:label
	 * "insertedContentRelation"; rdfs:range rdf:Property .
	 */
	public final static Resource INSERTED_CONTENT_RELATION = new Resource(NS + "insertedContentRelation");

	/**
	 * LDP servers should use this predicate as the membership predicate if
	 * there is no obvious predicate from an application vocabulary to use.
	 * 
	 * :member a rdf:Property; vs:term_status "stable"; rdfs:domain :Resource;
	 * rdfs:isDefinedBy :; rdfs:label "member"; rdfs:range rdfs:Resource .
	 */
	public final static Resource MEMBER = new Resource(NS + "member");

	/**
	 * Links a container with resources created through the container.
	 * 
	 * :contains a rdf:Property; vs:term_status "stable"; rdfs:domain
	 * :Container; rdfs:isDefinedBy :; rdfs:label "contains"; rdfs:range
	 * rdfs:Resource .
	 */
	public final static Resource CONTAINS = new Resource(NS + "contains");

	/**
	 * Used to indicate default and typical behavior for
	 * ldp:insertedContentRelation, where the member-URI value in the membership
	 * triple added when a creation request is successful is the URI assigned to
	 * the newly created resource.
	 * 
	 * :MemberSubject a owl:Individual; vs:term_status "stable";
	 * rdfs:isDefinedBy :; rdfs:label "MemberSubject" .
	 */
	public final static Resource MEMBER_SUBJECT = new Resource(NS + "MemberSubject");

	/**
	 * URI identifying a LDPC's containment triples, for example to allow
	 * clients to express interest in receiving them.
	 * 
	 * :PreferContainment a owl:Individual; vs:term_status "stable";
	 * rdfs:isDefinedBy :; rdfs:label "PreferContainment" .
	 */
	public final static Resource PREFER_CONTAINMENT = new Resource(NS + "PreferContainment");

	/**
	 * URI identifying a LDPC's membership triples, for example to allow clients
	 * to express interest in receiving them.
	 * 
	 * :PreferMembership a owl:Individual; vs:term_status "stable";
	 * rdfs:isDefinedBy :; rdfs:label "PreferMembership" .
	 */
	public final static Resource PREFER_MEMBERSHIP = new Resource(NS + "PreferMembership");

	/**
	 * Archaic alias for ldp:PreferMinimalContainer.
	 * 
	 * :PreferEmptyContainer a owl:Individual; vs:term_status "archaic";
	 * rdfs:isDefinedBy :; owl:equivalentProperty :PreferMinimalContainer;
	 * rdfs:seeAlso :PreferMinimalContainer; rdfs:label "PreferEmptyContainer" .
	 */
	public final static Resource PREFER_EMPTY_CONTAINER = new Resource(NS + "PreferEmptyContainer");

	/**
	 * URI identifying the subset of a LDPC's triples present in an empty LDPC,
	 * for example to allow clients to express interest in receiving them.
	 * Currently this excludes containment and membership triples, but in the
	 * future other exclusions might be added. This definition is written to
	 * automatically exclude those new classes of triples.
	 * 
	 * :PreferMinimalContainer a owl:Individual; vs:term_status "stable";
	 * rdfs:isDefinedBy :; rdfs:label "PreferMinimalContainer" .
	 */
	public final static Resource PREFER_MINIMAL_CONTAINER = new Resource(NS + "PreferMinimalContainer");

	/**
	 * Links a resource with constraints that the server requires requests like
	 * creation and update to conform to.
	 * 
	 * :constrainedBy a rdf:Property; vs:term_status "stable"; rdfs:domain
	 * :Resource; rdfs:isDefinedBy :; rdfs:label "constrainedBy"; rdfs:range
	 * rdfs:Resource .
	 */
	public final static Resource CONSTRAINED_BY = new Resource(NS + "constrainedBy");

	/**
	 * Link to the list of sorting criteria used by the server in a
	 * representation. Typically used on Link response headers as an extension
	 * link relation URI in the rel= parameter.
	 * 
	 * :pageSortCriteria a rdf:Property; vs:term_status "testing"; rdfs:domain
	 * :Page; rdfs:isDefinedBy :; rdfs:label "pageSortCriteria"; rdfs:range
	 * rdf:List.
	 */
	public final static Resource PAGE_SORT_CRITERIA = new Resource(NS + "pageSortCriteria");

	/**
	 * Element in the list of sorting criteria used by the server to assign
	 * container members to pages.
	 * 
	 * :PageSortCriterion a rdfs:Class; vs:term_status "testing"; rdfs:label
	 * "PageSortCriterion"; rdfs:isDefinedBy : .
	 */
	public final static Resource PAGE_SORT_CRITERION = new Resource(NS + "PageSortCriterion");

	/**
	 * Predicate used to specify the order of the members across a page
	 * sequence's in-sequence page resources; it asserts nothing about the order
	 * of members in the representation of a single page.
	 * 
	 * :pageSortPredicate a rdf:Property; vs:term_status "testing"; rdfs:domain
	 * :PageSortCriterion; rdfs:isDefinedBy :; rdfs:label "pageSortPredicate";
	 * rdfs:range rdf:Property .
	 */
	public final static Resource PAGE_SORT_PREDICATE = new Resource(NS + "pageSortPredicate");

	/**
	 * The ascending/descending/etc order used to order the members across pages
	 * in a page sequence.
	 * 
	 * :pageSortOrder a rdf:Property; vs:term_status "testing"; rdfs:domain
	 * :PageSortCriterion; rdfs:isDefinedBy :; rdfs:label "pageSortOrder";
	 * rdfs:range rdf:Resource .
	 */
	public final static Resource PAGE_SORT_ORDER = new Resource(NS + "pageSortOrder");

	/**
	 * The collation used to order the members across pages in a page sequence
	 * when comparing strings.
	 * 
	 * :pageSortCollation a rdf:Property; vs:term_status "testing"; rdfs:domain
	 * :PageSortCriterion; rdfs:isDefinedBy :; rdfs:label "pageSortCollation";
	 * rdfs:range rdf:Property .
	 */
	public final static Resource PAGE_SORT_COLLATION = new Resource(NS + "pageSortCollation");

	/**
	 * Ascending order.
	 * 
	 * :Ascending a owl:Individual; vs:term_status "testing"; rdfs:isDefinedBy
	 * :; rdfs:label "Ascending" .
	 */
	public final static Resource ASCENDING = new Resource(NS + "Ascending");

	/**
	 * Descending order.
	 * 
	 * :Descending a owl:Individual; vs:term_status "testing"; rdfs:isDefinedBy
	 * :; rdfs:label "Descending" .
	 */
	public final static Resource DESCENDING = new Resource(NS + "Descending");

	/**
	 * URI signifying that the resource is an in-sequence page resource, as
	 * defined by LDP Paging. Typically used on Link rel='type' response
	 * headers.
	 * 
	 * :Page a rdfs:Class; vs:term_status "testing"; rdfs:isDefinedBy :;
	 * rdfs:label "Page" .
	 */
	public final static Resource PAGE = new Resource(NS + "Page");

	/**
	 * Link to a page sequence resource, as defined by LDP Paging. Typically
	 * used to communicate the sorting criteria used to allocate LDPC members to
	 * pages.
	 * 
	 * :pageSequence a rdf:Property; vs:term_status "testing"; rdfs:isDefinedBy
	 * :; rdfs:label "Page" .
	 */
	public final static Resource PAGE_SEQUENCE = new Resource(NS + "pageSequence");

}