package org.semanticweb.yars.nx.filter;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.semanticweb.yars.nx.Node;


public class FilterIterator implements Iterator<Node[]>{
	private Iterator<Node[]> _in;
	private NodeFilter[] _nf;
	private Node[] _current;
	
	public FilterIterator(Iterator<Node[]> in, NodeFilter[] nf){
		_in = in;
		_nf = nf;
		getNext();
	}

	public boolean hasNext() {
		return _current!=null;
	}

	public Node[] next() {
		if(!hasNext()){
			throw new NoSuchElementException();
		}
		Node[] old = _current;
		getNext();
		return old;
	}
	
	private void getNext(){
		_current = null;
		if(!_in.hasNext()){
			return;
		}else{
			while(_in.hasNext()){
				Node[] ns = _in.next();
				if(check(_nf, ns)){
					_current = ns;
					return;
				}
			}
		}
	}
	
	public static boolean check(NodeFilter[] nfs, Node[] ns){
		if(nfs==null)
			return true;
		for(int i=0; i<nfs.length; i++){
			NodeFilter f = nfs[i];
			
			if(f!=null){
				if(!f.check(ns[i])){
					return false;
				}
			}
		}
		return true;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	
	
}
