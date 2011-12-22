package org.semanticweb.yars.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

/**
 * Iterator which closes the door when it leaves...
 * @author aidhog
 *
 */
public class PleaseCloseTheDoorWhenYouLeaveIterator implements Iterator<Node[]>{
	final Iterator<Node[]> _in;
	final InputStream _is;
	boolean _closed = false;
	
	public PleaseCloseTheDoorWhenYouLeaveIterator(Iterator<Node[]> in, InputStream is){
		_in = in;
		_is = is;
	}

	public boolean hasNext() {
		boolean hn = _in.hasNext();
		if(!hn && !_closed){
			try{
				_is.close();
			} catch(IOException e){
				e.printStackTrace();
			}
			_closed = true;
		}
		return _in.hasNext();
	}
	
	public Node[] next() {
		return _in.next();
	}

	public void remove() {
		_in.remove();
	}
}
