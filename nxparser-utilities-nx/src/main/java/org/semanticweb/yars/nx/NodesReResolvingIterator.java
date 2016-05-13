package org.semanticweb.yars.nx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.util.Util;

public class NodesReResolvingIterator implements Iterable<Node[]>, Iterator<Node[]> {
	
	private static final Logger LOG = Logger.getLogger(NodesReResolvingIterator.class.getName());
	
	private final Iterator<Node[]> _it;
	private final URI _uriToResolveAgainst;
	
	/**
	 * Changes a RDF graph that contains URIs relative to NxParser's well known
	 * URI for generating relative URIs to a RDF graph that contains URIs
	 * relative to a different URI.
	 * 
	 * The resolving is conforming to the
	 * <a href="https://www.w3.org/TR/ldp">Linked Data Platform</a>. 
	 * 
	 * @param it
	 *            An iterator over the RDF graph with NxParser's well-known URI.
	 * @param uriToResolveAgainst
	 *            The other URI to which URIs should be relative instead.
	 */
	public NodesReResolvingIterator(Iterable<Node[]> it, URI uriToResolveAgainst) {
		this(it.iterator(), uriToResolveAgainst);
	}
	
	/**
	 * @see {@link #NodesReResolvingIterator(Iterable, URI)}
	 */
	public NodesReResolvingIterator(Iterator<Node[]> it, URI uriToResolveAgainst) {
		_it = it;
		_uriToResolveAgainst = uriToResolveAgainst;
	}

	@Override
	public boolean hasNext() {
		return _it.hasNext();
	}

	@Override
	public Node[] next() {
		Node[] nx = _it.next();
		for (int i = 0; i < nx.length; ++i)
			if (nx[i] instanceof Resource) {
				Resource r = (Resource)nx[i];
				if (r.getLabel().toLowerCase().startsWith(Util.THIS_SCHEME_AND_AUTHORITY_STRING))
					try {
						URI relativeURI = Util.getPossiblyRelativisedUri(r.toURI());
						URI resolvedURI = Util.properlyResolve(_uriToResolveAgainst, relativeURI);
						r = new Resource("<" + resolvedURI.toString() + ">", true);
					} catch (URISyntaxException e) {
						LOG.log(Level.WARNING, "Stumbled upon a bad URI: {0}", e);
					}
				nx[i] = r;
			} else if (nx[i] instanceof BNode) {
				nx[i] = reResolveContextualBnode((BNode)nx[i], _uriToResolveAgainst);
			}
		return nx;
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}
	
	@Override
	public void remove() {
		_it.remove();
	}
	
	/**
	 * Ugly contextual Blank Node treatment. Necessary for uniqueness of Blank
	 * Nodes that otherwise would be all relative to NxParser's well-known URI
	 * for relative URIs.
	 * 
	 * @param bn
	 *            The Blank node relative to NxParser's well-known URI.
	 * @param uriToResolveAgainst
	 *            The new URI to which the blank node should be relative.
	 * @return The blank node relative to the new URI.
	 **/
	public static BNode reResolveContextualBnode(BNode bn,
			URI uriToResolveAgainst) {
		try {
			String[] components = bn.parseContextualBNode();

			int thisIdx = -1, otherIdx = -1;
			// Whoever knows the order in a contextual blank node.
			if (Util.THIS_STRING.equals(components[0])) {
				thisIdx = 0;
				otherIdx = 1;
			} else if (Util.THIS_STRING.equals(components[1])) {
				thisIdx = 1;
				otherIdx = 0;
			}

			if (thisIdx > -1) {
				try {
					BNode bn2 = new BNode(components[otherIdx]);
					// String[] components2 =
					bn2.parseContextualBNode();

					// Blank node is already contextual.
					bn = bn2;
				} catch (ParseException e2) {
					// "Inner" Blank node is not contextual, let's
					// make it contextual.

					bn = BNode.createBNode(
							uriToResolveAgainst.toString(),
							components[otherIdx]);
				}
			} else {
				// should not happen as we created the blank node
				// ourselves.
				LOG.log(Level.WARNING,
						"Encountered malicious Blank Node: {0}",
						new Object[] { bn });
			}

		} catch (ParseException e) {
			// should not happen as we created the blank node
			// ourselves.
			LOG.log(Level.WARNING, "Encountered malicious Blank Node: {0}",
					new Object[] { bn });
		}

		return bn;
	}
}
