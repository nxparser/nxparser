package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Variable implements Node, Serializable {

	private static final long serialVersionUID = 4927370223302416068L;
	private String _data;
	private boolean _isExistential = false;
	public static final String JOIN_CONST_PREFIX = "y2joinvar:";

	/**
	 * Constructor. If the data is in N3 syntax (i.e. it has a '?' in the
	 * beginning), set isN3 to true.
	 * 
	 * @param data
	 *            a string
	 * @param isN3
	 *            whether the string is to be assumed to be N3 (i.e. if it is
	 *            stored unchecked or not)
	 */
	public Variable(String data, boolean isN3) {
		if (isN3)
			_data = data;
		else {
			if (data.charAt(0) == '?')
				_data = data;
			else
				_data = '?' + data;
		}
	}

	/**
	 * Constructor. Supply only the variable's name, without the question mark
	 * in the beginning.
	 * 
	 * @param data
	 *            variable's name w/o ?.
	 */
	public Variable(String data) {
		this(data, false);
	}

	public String toString() {
		return _data.substring(Math.min(_data.length(), 1),
				_data.length() );
	}

	/**
	 * if parameter is of type Variable, compare the _data representations else
	 * a variable is always equals to a Resource, Blanknode, or Literal
	 * 
	 * @param o
	 *            - Object
	 */
	public int compareTo(Object o) {
		if (o instanceof Variable) {
			return _data.compareTo(o.toString());
		} else
			return -1;
	}

	/**
	 * Equality check.
	 * 
	 */
	public boolean equals(Object o) {
		return (o != null) && (o instanceof Variable)
				&& ((Variable) o)._data.equals(_data);
	}

	public String toN3() {
		return _data;
	}

	public void setExistential(boolean ex) {
		_isExistential = ex;
	}

	public boolean isExistential() {
		return _isExistential;
	}

	/**
	 * Needed for storing in hashtables.
	 */
	public int hashCode() {
		return _data.hashCode();
	}

	public Literal toJoinLiteral() {
		return new Literal(JOIN_CONST_PREFIX + toN3());
	}

	public static Variable fromJoinLiteral(Literal l) {
		if (isJoinLiteral(l)) {
			return new Variable(l.toString().substring(
					JOIN_CONST_PREFIX.length() + 1));
		}
		return null;
	}

	public static boolean isJoinLiteral(Literal l) {
		return l.toString().startsWith(JOIN_CONST_PREFIX);
	}

	public static boolean isJoinLiteral(Node n) {
		return n instanceof Literal
				&& ((Literal) n).toString().startsWith(JOIN_CONST_PREFIX);
	}
}