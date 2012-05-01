package org.semanticweb.yars.util;

import java.net.URI;
import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
/**
 * 
 * @author Tobias Kaefer
 *
 */
public class Node2uriConvertingIterator implements Iterator<URI> {
	private Iterator<Node[]> _nodesIt = null;
	private int _placeInArray;

	public Node2uriConvertingIterator(Iterator<Node[]> nodesIt, int placeInArray) {
		_nodesIt = nodesIt;
		_placeInArray = placeInArray;
	}

	public boolean hasNext() {
		return _nodesIt.hasNext();
	}

	public URI next() {
		return ((Resource) _nodesIt.next()[_placeInArray]).toURI();
	}

	public void remove() {
		_nodesIt.remove();
	}

}