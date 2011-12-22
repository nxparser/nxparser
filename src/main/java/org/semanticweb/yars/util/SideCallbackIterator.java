package org.semanticweb.yars.util;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * Iterator which does a Callback on the side...
 * @author aidhog
 *
 */
public class SideCallbackIterator implements Iterator<Node[]>{
	final Iterator<Node[]> _in;
	final Callback _cb;
	final boolean _autoclose;
	boolean _closed = false;
	
	public SideCallbackIterator(Iterator<Node[]> in, Callback cb){
		this(in, cb, false);
	}
	
	public SideCallbackIterator(Iterator<Node[]> in, Callback cb, boolean autoclose){
		_in = in;
		_cb = cb;
		_autoclose = autoclose;
	}

	public boolean hasNext() {
		boolean hn = _in.hasNext();
		if(!hn && _autoclose && !_closed){
			_cb.endDocument();
			_closed = true;
		}
		return hn;
	}
	
	public Node[] next() {
		Node[] next = _in.next();
		_cb.processStatement(next);
		return next;
	}

	public void remove() {
		_in.remove();
	}
}
