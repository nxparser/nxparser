package org.semanticweb.yars.nx;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Basically a node array.
 * 
 * In addition, optionally the Nodes class can keep track of object
 * references, and only use canonical object references for Node objects.
 * 
 * @author aharth
 *
 */
public class Nodes implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Node[] EOM = new Node[0];

	private final Node[] _data;
	
	public Nodes(Node... na) {
		_data = na;
	}

//	public Nodes(Collection<Node> cn) {
//		Arrays.as
//		cn.
//		_data = new Node[cn.size()];
//		cn.toArray(_data);
//	}

	public Node[] getNodes() {
		return _data;
	}

	boolean equals(Nodes n) {
		return Arrays.equals(_data, n.getNodes());
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

	public String toN3() {
		return toN3(_data);
	}

	@Override
	public String toString() {
		return toN3();
	}

	public static String toN3(Node[] ns) {
		StringBuffer buf = new StringBuffer();
		for (Node n : ns) {
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
