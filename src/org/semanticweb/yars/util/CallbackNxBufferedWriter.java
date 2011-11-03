package org.semanticweb.yars.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackNxBufferedWriter implements Callback {

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
	 * ...you handle closing the BufferedWriter outside
	 */
	public CallbackNxBufferedWriter(BufferedWriter out) {
		this(out, false);
	}

	/**
	 * ...if close flag is set true, endDocument() will close
	 * the BufferedWriter
	 */
	public CallbackNxBufferedWriter(BufferedWriter out, boolean close) {
		_bw = out;
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
}