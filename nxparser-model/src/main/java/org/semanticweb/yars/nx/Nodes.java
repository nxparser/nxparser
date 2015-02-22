package org.semanticweb.yars.nx;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

/**
 * Basically a node array.
 * 
 * In addition, optionally the Nodes class can keep track of object
 * references, and only use canonical object references for Node objects.
 * 
 * @author aharth
 *
 */
public class Nodes implements Serializable, Comparable<Nodes> {
	private static final long serialVersionUID = 1L;

	public static final Node[] EOM = new Node[0];

	private final Node[] _data;
	
	public Nodes(Node... na) {
		_data = na;
	}

	public Nodes(Collection<Node> cn) {
		_data = new Node[cn.size()];
		cn.toArray(_data);
	}

	public Node[] getNodeArray() {
		return _data;
	}

	boolean equals(Nodes n) {
		return Arrays.equals(_data, n.getNodeArray());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		return (o instanceof Nodes) && equals((Nodes) o);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(_data);
	}

	@Override
	public int compareTo(Nodes o) {
		if (this == o) {
			return 0;
		}

		int min = Math.min(_data.length, o._data.length);

		for (int i = 0; i < min; i++) {
			int comp = _data[i].compareTo(o._data[i]);
			if (comp != 0) {
				return comp;
			}
		}

		int diff = _data.length - o._data.length;
		if (diff != 0) {
			return diff;
		}
		
		// same
		return 0;
	}

	@Override
	public String toString() {
		return toString(_data);
	}

	/**
	 * Returns the Nodes in the Node array as a String representation of a
	 * Nx-Statement, i.e. with spaces in between and a full stop at the end.
	 * 
	 * @param nx
	 *            Node objects in an array
	 * @return The statement
	 */
	public static String toString(Node[] nx) {
		StringBuilder buf = new StringBuilder();
		for (Node n : nx) {
			buf.append(n.toString());
			buf.append(" ");
		}
		buf.append(".");

		return buf.toString();
	}
//
//	/**
//	 * Fast hashcode method for Node arrays
//	 */
//	public static int hashCode(Node... nx) {
//		return Array.hashCode(nx, 0, nx.length);
//	}
//
//	/**
//	 * Fast hashcode method for Node arrays
//	 */
//	public static int hashCode(Node[] nx, int pos, int length) {
//		return Array.hashCode(nx, pos, length);
//	}
}
