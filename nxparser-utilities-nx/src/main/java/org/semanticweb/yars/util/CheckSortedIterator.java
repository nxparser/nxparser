package org.semanticweb.yars.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeArrayComparator;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;

public class CheckSortedIterator implements Iterator<Node[]>{
	Iterator<Node[]> _in;
	Node[] _old = null;
	Comparator<Node[]> _nc;
	NotSortedException _last = null;
	
	static transient Logger _log = Logger.getLogger(CheckSortedIterator.class.getName());
	
	
	public CheckSortedIterator(Iterator<Node[]> iter){
		this(iter, NodeArrayComparator.NC);
	}
	
	public CheckSortedIterator(Iterator<Node[]> iter, Comparator<Node[]> nc){
		_in = iter;
		_nc = nc;
	}
	
	public boolean hasNext() {
		return _in.hasNext();
	}

	public NotSortedException getException(){
		return _last;
	}
	
	public boolean isOkay(){
		return _last == null;
	}
	
	public Node[] next() {
		Node[] next = _in.next();
		if(_old!=null){
			int comp = _nc.compare(_old,next);
			if(comp>0 && comp!=NodeArrayComparator.NOT_EQUALS_COMP){
				_last = new NotSortedException(_old, next);
				_log.severe(_last.getMessage());
			}
		}
		_old = next;
		return next;
	}

	public void remove() {
		_in.remove();
	}
	
	public static class NotSortedException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Node[] _a;
		private Node[] _b;
		
		public NotSortedException(String msg){
			super(msg);
		}
		
		public NotSortedException(Node[] a, Node[] b){
			super(createMessage(a, b));
			_a = a;
			_b = b;
		}
		
		public Node[] getFirstNodes(){
			return _a;
		}
		
		public Node[] getSecondNodes(){
			return _b;
		}
		
		static String createMessage(Node[] a, Node[] b){
			StringBuffer buf = new StringBuffer();
			buf.append("#########Not sorted!#########\n");
			buf.append(" "+Nodes.toString(a)+" before\n");
			buf.append(" "+Nodes.toString(b)+"\n");
			return buf.toString();
		}
	}
	
	public static void main(String[] args){
		Node[] a = new Node[]{ new Resource("http://docs.openlinksw.com/virtuoso/creatingtxtidxs.html"),  new Resource("http://rdfs.org/sioc/ns#title"), new Literal("Database"), new Resource("http://docs.openlinksw.com/virtuoso/creatingtxtidxs.sioc.rdf")}; 
		Node[] b = new Node[]{ new Resource("http://docs.openlinksw.com/virtuoso/creatingtxtidxs.html"),  new Resource("http://rdfs.org/sioc/ns#title"), new Literal("Creating Free Text Indexes"), new Resource("http://docs.openlinksw.com/virtuoso/virtdocs.sioc.rdf")};
		
		System.err.println(NodeArrayComparator.NC.compare(a,b));
		
	}
}
