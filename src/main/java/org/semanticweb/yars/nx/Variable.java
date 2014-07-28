package org.semanticweb.yars.nx;

import java.io.Serializable;

public class Variable implements Node, Serializable {
	private static final long serialVersionUID = 1L;

	private final String _data;
	private boolean _isExistential = false;

	public Variable(String data, boolean isN3) {
		if (isN3) {
			_data = data.intern();
		} else {
			if (data.charAt(0) == '?') {
				_data = data.intern();
			} else {
				_data = '?' + data.intern();
			}
		}
	}

	public Variable(String data) {
		this(data, false);
	}

	@Override
	public String toString() {
		return _data.substring(Math.min(_data.length(), 1),
				Math.max(_data.length(), 0));
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
	public String toN3() {
		return _data;
	}

	public void setExistential(boolean ex) {
		_isExistential = ex;
	}

	public boolean isExistential() {
		return _isExistential;
	}
}