package org.semanticweb.yars.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackNxOutputStream implements Callback {

	final BufferedWriter _bw;

	long _cnt = 0;
	long _time, _time1;
	final boolean _close;

	public final static Charset DEFAULTCHARSET = Charset.forName("US-ASCII");

	public final static String DOTNEWLINE = "."
			+ System.getProperty("line.separator");

	public final static byte[] SPACE = " ".getBytes();
	public final static byte[] DOT_NEWLINE = (DOTNEWLINE).getBytes();

//	public LRUMapCache<Node, ByteArray> _cache = new LRUMapCache<Node, ByteArray>();

	/**
	 * @Deprecated
	 * ...add true to the constructor to close the internal buffer
	 * as well as the passed OutputStream.
	 * 
	 * Use #endDocument to close the Callback. 
	 */
	@Deprecated
	public CallbackNxOutputStream(OutputStream out) {
		this(out, DEFAULTCHARSET, false);
	}

	public CallbackNxOutputStream(OutputStream out, Charset charset) {
		this(out, charset, false);
	}

	public CallbackNxOutputStream(OutputStream out, boolean close) {
		this(out, DEFAULTCHARSET, close);
	}

	public CallbackNxOutputStream(OutputStream out, Charset charset,
			boolean close) {
		_bw = new BufferedWriter(new OutputStreamWriter(out, charset));
		_close = close;
	}

	public synchronized void processStatement(Node[] nx) {
		try {
			for (Node n : nx) {
				_bw.write(n.toN3());
				_bw.write(' ');
			}
			_bw.write(DOTNEWLINE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		_cnt++;
	}

	public void startDocument() {
		_time = System.currentTimeMillis();
	}
	
	public void endDocument() {
		try {
			if (_close)
				_bw.close();
			else
				_bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		_time1 = System.currentTimeMillis();
	}

	public String toString() {
		return _cnt + " tuples in " + (_time1 - _time) + " ms";
	}

//	public byte[] getBytes(Node n) {
//		// return n.toN3().getBytes();
//		ByteArray ba = _cache.get(n);
//		if (ba == null) {
//			ba = new ByteArray();
//			ba._b = n.toN3().getBytes();
//			_cache.put(n, ba);
//		}
//		return ba._b;
//	}
//
//	public static class ByteArray {
//		byte[] _b;
//	}
}