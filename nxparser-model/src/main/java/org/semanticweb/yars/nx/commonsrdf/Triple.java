package org.semanticweb.yars.nx.commonsrdf;

import org.apache.commons.rdf.api.BlankNodeOrIRI;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDFTerm;
import org.semanticweb.yars.nx.Nodes;

public class Triple extends Nodes implements org.apache.commons.rdf.api.Triple {

	private static final long serialVersionUID = 1L;

	public Triple(Term... nx) {
		super(nx);
		if (nx.length != 3) {
			throw new IllegalArgumentException("A triple is of length 3!");
		}
	}

	@Override
	public BlankNodeOrIRI getSubject() {
		return (BlankNodeOrIRI) getNodeArray()[0];
	}

	@Override
	public IRI getPredicate() {
		return (IRI) getNodeArray()[1];
	}

	@Override
	public RDFTerm getObject() {
		return (RDFTerm) getNodeArray()[2];
	}

}
