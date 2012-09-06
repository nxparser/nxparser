package org.semanticweb.yars.nx.reorder;

import java.util.Iterator;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;

public class ReorderIterator implements Iterator<Node[]>{
	static transient Logger _log = Logger.getLogger(ReorderIterator.class.getName());
	
	private final Iterator<Node[]> _in;
	private final int[] _order;
	private final int _ticks;
	
	private long _count;
	
	public ReorderIterator(Iterator<Node[]> in, int[] order){
		this(in, order, 0);
	}
	
	public ReorderIterator(Iterator<Node[]> in, int[] order, int ticks){
		_in = in;
		_order = order;
		_ticks = ticks;
		_count = 0;
	}

	public boolean hasNext() {
		return _in.hasNext();
	}

	public Node[] next() {
		_count++;
		if(_ticks>0 && _count%_ticks==0)
			_log.info("Reordered "+_count+" statements.");
		return reorder(_in.next(), _order);
	}

	public void remove() {
		_in.remove();
	}
	
	public long count(){
		return _count;
	}
	
	public static Node[] reorder(Node[] nx, int[] order) {
		Node[] nxout = new Node[order.length];

		for(int i=0; i<order.length; i++){
			nxout[i] = nx[order[i]];
		}

		return nxout;

	}
	
	public static int[] getInvOrder(int[] order) {
		int[] invOrder = new int[order.length];
		
		for(int i=0; i<order.length; i++){
			invOrder[order[i]] = i;
		}
		
		return invOrder;
	}
}
