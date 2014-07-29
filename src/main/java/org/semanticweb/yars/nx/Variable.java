package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Variable implements Node, Serializable {
	private static final long serialVersionUID = 1L;

	private final String _data;
	private final boolean _existential;

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

	public Variable(String data, boolean isN3) {
		this(data, false, isN3);
	}

	public Variable(String data) {
		this(data, false, false);
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

	public boolean isExistential() {
		return _existential;
	}
}