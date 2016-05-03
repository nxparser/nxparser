package org.semanticweb.yars.nx.commonsrdf;

import java.util.UUID;

import org.apache.commons.rdf.api.BlankNode;
import org.apache.commons.rdf.api.BlankNodeOrIRI;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDFTerm;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Resource;

import nxparser.org.apache.commons.rdf.api.Graph;
import nxparser.org.apache.commons.rdf.api.RDFTermFactory;

public class TermFactory implements RDFTermFactory {
	
	static TermFactory _singleton = null;
	
	private TermFactory() {
		
	}
	
	public static TermFactory getInstance() {
		if (_singleton == null)
			_singleton = new TermFactory();
		return _singleton;	
	}
	
	@Override
	public BlankNode createBlankNode() throws UnsupportedOperationException {
		return new BNode(UUID.randomUUID().toString());
	}

	@Override
	public BlankNode createBlankNode(String name) throws UnsupportedOperationException {
		return new BNode(name);
	}

	@Override
	public Graph createGraph() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IRI createIRI(String iri) throws IllegalArgumentException, UnsupportedOperationException {
		return new Resource(iri);
	}

	@Override
	public nxparser.org.apache.commons.rdf.api.Literal createLiteral(String lexicalForm)
			throws IllegalArgumentException, UnsupportedOperationException {
		return new Literal(lexicalForm);
	}

	@Override
	public nxparser.org.apache.commons.rdf.api.Literal createLiteral(String lexicalForm, IRI dataType)
			throws IllegalArgumentException, UnsupportedOperationException {
		return new Literal(lexicalForm, new Resource(dataType.ntriplesString(), true));
	}

	@Override
	public nxparser.org.apache.commons.rdf.api.Literal createLiteral(String lexicalForm, String languageTag)
			throws IllegalArgumentException, UnsupportedOperationException {
		return new Literal(lexicalForm, languageTag);
	}

	@Override
	public org.apache.commons.rdf.api.Triple createTriple(BlankNodeOrIRI subject, IRI predicate, RDFTerm object)
			throws IllegalArgumentException, UnsupportedOperationException {
		Term nxpSubject = (Term) ((subject instanceof Term)
				? subject : subject instanceof BlankNode ? new BNode(subject.ntriplesString(), true) 
				: createIRI(subject.ntriplesString()));
		Term nxpPredicate = (Term) ((predicate instanceof Term)? predicate: createIRI(predicate.ntriplesString()));
		
		Term nxpObject = null;
		if (object instanceof Term) 
			nxpObject = (Term) object;
		else if (object instanceof nxparser.org.apache.commons.rdf.api.Literal) {
			nxparser.org.apache.commons.rdf.api.Literal lit = (nxparser.org.apache.commons.rdf.api.Literal) object;
			if (lit.getLanguageTag() == null) {
				nxpObject = (Term) createLiteral(lit.getLexicalForm(), lit.getDatatype());
			} else {
				nxpObject = (Term) createLiteral(lit.getLexicalForm(), lit.getLanguageTag());
			}
		} else if (object instanceof BlankNode)
			nxpObject = new BNode(object.ntriplesString(), true);
		else if (object instanceof IRI)
			nxpObject = new Resource(object.ntriplesString(), true);
		else
			throw new IllegalArgumentException("Problem interpreting: " + object);
		
		return new Triple(nxpSubject, nxpPredicate, nxpObject);
	}

}
