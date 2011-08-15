package org.semanticweb.yars.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;

public class CheckLengthIterator implements Iterator<Node[]>{
	Iterator<Node[]> _in;
	Node[] _next = null;
	IncorrectLengthException _ile = null;
	short _nxlen = 0;
	long _skip = 0;
	
	static transient Logger _log = Logger.getLogger(CheckLengthIterator.class.getName());
	
	public CheckLengthIterator(Iterator<Node[]> iter, short nxlen){
		_in = iter;
		_nxlen = nxlen;
		loadNext();
	}
	
	private void loadNext(){
		_next = null;
		while(_in.hasNext() && _next==null){
			Node[] next = _in.next();
			if(next.length!=_nxlen){
				_ile = new IncorrectLengthException(next);
				_log.warning(_ile.getMessage());
				_skip++;
			} else{
				_next = next;
			}
		}
	}
	
	public long skipped(){
		return _skip;
	}
	
	public boolean hasNext() {
		return _next!=null;
	}

	public IncorrectLengthException getException(){
		return _ile;
	}
	
	public boolean isOkay(){
		return _ile == null;
	}
	
	public Node[] next() {
		if(_next==null)
			throw new NoSuchElementException();
		Node[] next = _next;
		loadNext();
		return next;
	}

	public void remove() {
		_in.remove();
	}
	
	public static class IncorrectLengthException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Node[] _a;
		
		public IncorrectLengthException(String msg){
			super(msg);
		}
		
		public IncorrectLengthException(Node[] a){
			super(createMessage(a));
			_a = a;
		}
		
		public Node[] getProblem(){
			return _a;
		}
		
		static String createMessage(Node[] a){
			StringBuffer buf = new StringBuffer();
			buf.append("Not correct length!");
			buf.append(" "+Nodes.toN3(a)+" ");
			return buf.toString();
		}
	}
}
