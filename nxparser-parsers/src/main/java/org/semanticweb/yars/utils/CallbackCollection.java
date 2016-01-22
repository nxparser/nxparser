package org.semanticweb.yars.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * 
 * @author aharth
 * @version $Revision: 1.0 $
 */
public class CallbackCollection extends Callback {
	final Set<Node[]> _set = new HashSet<Node[]>();

	/**
	 * Method getCollection.
	 */
	public Collection<Node[]> getCollection() {
		return _set;
	}

	/**
	 * Method startDocument.
	 * 
	 * @see org.semanticweb.yars.nx.parser.Callback#startDocument()
	 */
	@Override
	public void startDocumentInternal() {
		;
	}

	/**
	 * Method endDocument.
	 * 
	 * @see org.semanticweb.yars.nx.parser.Callback#endDocument()
	 */
	@Override
	public void endDocumentInternal() {
		;
	}

	/**
	 * The input might be quads, but we're handling origin ourselves.
	 * So just add triples.
	 * 
	 * @param nx
	 *            Node[]
	 * 
	 * @see org.semanticweb.yars.nx.parser.Callback#processStatement(Node[])
	 */
	@Override
	public void processStatementInternal(Node[] nx) {
		_set.add(nx);
	}
}