package org.semanticweb.yars.parsers.external.json.jsonld_java;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;

import com.github.jsonldjava.core.JsonLdTripleCallback;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.core.RDFDataset.Quad;

/**
 * Converts between jsonld-java's class hierarchy and NxParser's class
 * hierarchy.
 *
 * @author Tobias Käfer
 *
 */
public class JsonLDtripleCallback implements JsonLdTripleCallback {

	private final static Logger _log = Logger
			.getLogger(JsonLDtripleCallback.class.getName());

	Collection<Node[]> _nx = Collections.emptyList();
	final Resource _baseURI;

	public JsonLDtripleCallback(String baseURI) {
		_baseURI = new Resource(baseURI);
	}

	public Iterable<Node[]> getNx() {
		return _nx;
	}

	@Override
	public Object call(RDFDataset dataset) {
		_nx = new LinkedList<Node[]>();
		for (String graphName : dataset.graphNames()) {
			Node context = _baseURI;

			for (Quad quad : dataset.getQuads(graphName))
				_nx.add(new Node[] {
						jsonld_java_Node__to__nxparser_Node(quad.getSubject(),
								context),
						jsonld_java_Node__to__nxparser_Node(
								quad.getPredicate(), context),
						jsonld_java_Node__to__nxparser_Node(quad.getObject(),
								context), context });

		}
		return _nx;
	}

	public static Node jsonld_java_Node__to__nxparser_Node(
			com.github.jsonldjava.core.RDFDataset.Node jsonldNode,
			Node graphName) {
		if (jsonldNode.isBlankNode()) {
			return BNode.createBNode(graphName.toString(),
					jsonldNode.getValue());
		} else if (jsonldNode.isIRI()) {
			return new Resource(jsonldNode.getValue());
		} else if (jsonldNode.isLiteral()) {
			return new Literal(jsonldNode.getValue(), jsonldNode.getLanguage(),
					jsonldNode.getLanguage() == null ? new Resource(
							jsonldNode.getDatatype()) : null);
		}

		_log.log(Level.WARNING, "Could not convert: {0}",
				new Object[] { jsonldNode });
		return null;

	}

}
