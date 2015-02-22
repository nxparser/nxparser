package org.semanticweb.yars.nx.sort;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeArrayComparator;

public class MergeSortIterator implements Iterator<Node[]> {
	static transient Logger _log = Logger.getLogger(MergeSortIterator.class.getName());
	
	private MergeSortArgs _args;
	private PriorityQueue<NodeArrayStreamPair> _q;
	private Node[] _current = null;
	private long _count = 0;
	private long _dupes;
	
	private NodeArrayStreamPair[] _last;
	
	public MergeSortIterator(Iterator<Node[]>... segments){
		this(new MergeSortArgs(segments));
	}
	
	public MergeSortIterator(MergeSortArgs args){
		_args = args;
		_dupes = _args._dupes;
		_q = new PriorityQueue<NodeArrayStreamPair>();
		_last = new NodeArrayStreamPair[_args._in.length];
		
		for(int i=0; i<_last.length; i++){
			loadNext(i);
		}
		
		if(_q.size()==0){
			return;
		}
		
		prepareNext();
	}

	public Node[] next(){
		Node[] result = new Node[_current.length];
		System.arraycopy(_current, 0, result, 0, _current.length);
		prepareNext();
		return result;
	}
	
	public boolean hasNext(){
		return _current!=null;
	}
	
	private void prepareNext(){ 
		Node[] next = null;
		while(_q.size()!=0){
			NodeArrayStreamPair nsp = _q.poll();
			next = nsp.getNodes(); 
			_count++;
			
			if(_args._ticks>0 && _count%_args._ticks==0){
				_log.info("Merged "+_count+" with "+_dupes+" duplicates.");
			}
			
			loadNext(nsp);
			if(_current == null || _args._nc.compare(_current, next)!=0){
				_current = next;
				return;
			} else{
				_dupes++;
			}
		} 
		_current = null;
	}
	
	private void loadNext(NodeArrayStreamPair current){
		if(_last[current.getStream()]!=null &&
				_last[current.getStream()] == current){
			loadNext(current.getStream());
		}
	}
	
	private void loadNext(int stream){
		int u = 0;
		//get unique entry in TreeSet
		NodeArrayStreamPair nsp = null;
		NodeArrayStreamPair last = null;
		Node[] na = null;
		while(u<_args._linesPerBatch && _args._in[stream].hasNext()){
			na = _args._in[stream].next();
			nsp = new NodeArrayStreamPair(na, stream, _args._nc);
			
			//q allows duplicates
			_q.add(nsp);
			last = nsp;
			u++;
		}

		_last[stream] = last;
	}
	
	public long duplicates(){
		return _dupes;
	}
	
	public void remove(){
		throw new UnsupportedOperationException();
	}
	
	private static class NodeArrayStreamPair implements Comparable<NodeArrayStreamPair> {
		private Node[] nodes;
		private int stream;
		private Comparator<Node[]> nc;
		
		private NodeArrayStreamPair(Node[] _nodes, int _stream, Comparator<Node[]> comp){
			nodes = _nodes;
			stream = _stream;
			nc = comp;
		}
		
		public Node[] getNodes(){
			return nodes;
		}
		
		public int getStream(){
			return stream;
		}
		
		public void setNodes(Node[] _nodes){
			nodes = _nodes;
		}
		
		public void setStream(int _stream){
			stream = _stream;
		}
		
		public int compareTo(NodeArrayStreamPair o){
			return nc.compare(nodes, o.getNodes());
		}
		
		public boolean equals(Object o){
			if(this==o)
				return true;
			else if(o instanceof NodeArrayStreamPair){
				NodeArrayStreamPair nsp = (NodeArrayStreamPair)o;
				return nc.compare(this.nodes, nsp.nodes)==0;
			}
			return false;
		}
		
		public int hashCode(){
			throw new UnsupportedOperationException("hashCode not implemented.");
		}
		
		public String toString(){
			StringBuffer buf = new StringBuffer();
			buf.append("NodeStreamPair : Nodes :");
			for(Node n:nodes)
				buf.append(n.toString()+" ");
			buf.append("Stream :"+stream);
			
			return buf.toString();
		}
	}
	
	public long count(){
		return _count;
	}
	
	public static class MergeSortArgs{
		private final Iterator<Node[]>[] _in;
		
		private Comparator<Node[]> _nc = NodeArrayComparator.NC;
		private int _linesPerBatch = 1;
		private long _dupes;
		
		private int _ticks = 0;
		
		public MergeSortArgs(Iterator<Node[]>... in){
			_in = in;
			_nc = NodeArrayComparator.NC;
			_linesPerBatch = 1;
			_dupes = 0;
		}
		
		public void setComparator(Comparator<Node[]> nc){
			_nc = nc;
		}
		
		/**
		 * Will read in linesPerBatch from each sorted batch every time...
		 * Wise to not set linesPerBatch to anything other than the default of 1,
		 * given testing on Unix machines where higher values strangely made the sort
		 * grind to a halt, and testing on Windows machines where larger batch sizes
		 * were marginally slower than for single sized batches.
		 * 
		 * That said, the evaluation was for GZipped input files... evaluation for
		 * non-compressed files has not been done.
		 * 
		 * @param linesPerBatch
		 */
		public void setLinesPerBatch(int linesPerBatch){
			if(linesPerBatch>0)
				_linesPerBatch = linesPerBatch;
		}
		
		public void setDuplicates(long duplicates){
			_dupes = duplicates;
		}
		
		public void setTicks(int ticks){
			_ticks = ticks;
		}
	}
}
