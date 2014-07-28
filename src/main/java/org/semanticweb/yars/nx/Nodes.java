package org.semanticweb.yars.nx;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class Nodes implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Node[] EOM = new Node[0];

	Node[] _na;

	public Nodes(Node... n) {
		_na = n;
	}

	public Nodes(Collection<Node> cn) {
		_na = new Node[cn.size()];
		cn.toArray(_na);
	}

	public Node[] getNodes() {
		return _na;
	}

	boolean equals(Nodes n) {
		return Arrays.equals(_na, n.getNodes());
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
		return Arrays.hashCode(_na);
	}

	public String toN3() {
		return toN3(_na);
	}

	@Override
	public String toString() {
		return toN3();
	}

	public static String toN3(Node[] ns) {
		StringBuffer buf = new StringBuffer();
		for (Node n : ns) {
			buf.append(n.toN3());
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
