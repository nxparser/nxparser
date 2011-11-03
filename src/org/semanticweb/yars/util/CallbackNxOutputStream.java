package org.semanticweb.yars.util;

import java.io.IOException;
import java.io.OutputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * @deprecated
 * @author aidhog
 * 
 * CallbackNxBufferedOutputStream is a lot faster.
 */
@Deprecated
public class CallbackNxOutputStream implements Callback {
	final OutputStream _out;

	long _cnt = 0;
	long _time, _time1;
	final boolean _close;
	
	public final static byte[] SPACE = " ".getBytes();
	public final static byte[] DOT_NEWLINE = ("."+System.getProperty("line.separator")).getBytes();
	
	public LRUMapCache<Node, ByteArray> _cache = new LRUMapCache<Node, ByteArray>();

	/**
	 * @deprecated
	 * ...add true to the constructor to close the internal buffer
	 * as well as the passed OutputStream.
	 * 
	 * Use #endDocument to close the Callback. 
	 */
	@Deprecated
	public CallbackNxOutputStream(OutputStream out) {
		this(out, false);
	}
	
	public CallbackNxOutputStream(OutputStream out, boolean close) {
		_out = out;
		_close = close;
	}
	
	public synchronized void processStatement(Node[] nx) {
		try {
			for(Node n:nx){
				_out.write(n.toN3().getBytes());
				_out.write(SPACE);
			}
			_out.write(DOT_NEWLINE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		_cnt++;
	}

	public void startDocument() {
		_time = System.currentTimeMillis();
	}
	
	public void endDocument() {
		try {
			if(_close)
				_out.close();
			else
				_out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		_time1 = System.currentTimeMillis();
	}
	
	public String toString() {
		return _cnt + " tuples in " + (_time1-_time) + " ms";
	}
	
	public byte[] getBytes(Node n){
//		return n.toN3().getBytes();
		ByteArray ba = _cache.get(n);
		if(ba==null){
			ba = new ByteArray();
			ba._b = n.toN3().getBytes();
			_cache.put(n, ba);
		}
		return ba._b;
	}
	
	public static class ByteArray{
		byte[] _b;
	}
}