package org.semanticweb.yars.util;

import java.io.OutputStream;

/**
@deprecated
Replaced by CallbackNxBufferedWriter
**/
public class CallbackNQOutputStream extends CallbackNxOutputStream {
	public CallbackNQOutputStream(OutputStream out) {
		super(out, true);
	}
}