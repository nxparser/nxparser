package org.semanticweb.yars.util;

import java.util.Iterator;

public interface ResetableIterator<E> extends Iterator<E> {
	/**
	 * Reset the iterator back to start. Can then be used as new.
	 * Should give idempotent behaviour.
	 */
	public void reset();
}
