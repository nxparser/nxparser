package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Variable implements Node, Serializable {
	private static final long serialVersionUID = 1L;

	private final String _data;
	private final boolean _existential;

	/**
	 * Constructor for Variables. Although they do not exist in N-Triples, they
	 * are sometimes useful.
	 * 
	 * @param data
	 * @param existential
	 * @param isN3
	 *            whether the data comes according to N3, i.e. with a question
	 *            mark
	 */
	public Variable(String data,  boolean existential, boolean isN3) {
		if (isN3) {
			_data = data;
		} else {
			if (data.charAt(0) == '?') {
				_data = data;
			} else {
				_data = '?' + data;
			}
		}
		_existential = existential;
	}

	/**
	 * Constructor for Variables. Although they do not exist in N-Triples, they
	 * are sometimes useful.
	 * 
	 * @param data
	 * @param isN3 whether the data comes according to N3, i.e. with a question mark
	 */
	public Variable(String data, boolean isN3) {
		this(data, false, isN3);
	}

	/**
	 * Constructor for Variables. Although they do not exist in N-Triples, they
	 * are sometimes useful. If you can vouch that your data with a
	 * question mark, use {@link #Variable(String, boolean)}.
	 * 
	 * @param data
	 */
	public Variable(String data) {
		this(data, false, false);
	}
	

	public boolean isExistential() {
		return _existential;
	}

	/**
	 * Equality check.
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		return (o instanceof Variable) && ((Variable) o)._data.equals(_data);
	}

	/**
	 * Needed for storing in hashtables.
	 */
	@Override
	public int hashCode() {
		return _data.hashCode();
	}

	@Override
	public String toString() {
		return _data;
	}
	
	@Override
    public String getLabel() {
    	return _data.substring(1);
    }

	@Override
	public int compareTo(Node n) {
		return toString().compareTo(n.toString());
	}
}