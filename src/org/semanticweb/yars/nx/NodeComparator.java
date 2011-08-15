package org.semanticweb.yars.nx;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

public class NodeComparator implements Comparator<Node[]>, Serializable {
	private static Logger _log = Logger.getLogger(NodeComparator.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** if true, returns true for equals and 0 for compareTo 
	 * if both arrays contain same elements in same indices
	 * even if both arrays are of different lengths. If false
	 * arrays of different length with return false and non-zero
	 * for equals and compareTo.
	 */
	private boolean _variableLength = true;
	
	/**
	 * Not zero only returns zero if references are the same (allows for duplicates).
	 */
	private boolean _nZero = false;
	
	/**
	 * Not equals ensures only comparison on reference (if same object, allows duplicates).
	 */
	private boolean _nEquals = false;
	
	/**
	 * Mask stating which elements should be ordered (compare only) numerically
	 */
	private boolean[] _numeric = null;
	
	/**
	 * Mask stating which elements should be reverse ordered (compare only)
	 */
	private boolean[] _reverse = null;
	
	
	/** 
	 * Checks the nodes in the given order for the compare to method
	 * For example for sorting in POCS order, use order = {1, 2, 3, 0}. 
	 * Also, an element which does not affect sorting
	 * order can be omitted. For example, to compare only on POC for 
	 * SPOC quad, use {1, 2, 3}.
	 */
	private int[] _order = null;
	
	/** 
	 * Only consider at most first n nodes for equals and compare
	 */
	private int _n = Integer.MAX_VALUE;
	
	/** Statically accessible NodeComparator which returns 
	 * true for equals and 0 for compareTo if arrays contain 
	 * same elements in same indices even if they are of different 
	 * lengths**/
	public static final NodeComparator NC_VAR = new NodeComparator(true);
	
	/** Statically accesible NodeComparator which returns 
	 * true for equals and 0 for compareTo iff arrays contain 
	 * same elements in same indices and are of the same length**/
	public static final NodeComparator NC = new NodeComparator(false);
	
	/**
	 * 
	 * @param variableLength If true, comparison only performed on the 
	 * first N elements of the arrays where N is the number of the smaller array
	 */
	public NodeComparator(boolean variableLength){
		_variableLength = variableLength;
	}
	
	/**
	 * 
	 * @param notZero allows duplicates by only using object reference for compareTo method
	 * @param notEqual allows duplicates by only using object reference for equals method
	 */
	public NodeComparator(boolean notZero, boolean notEqual){
		_nZero = notZero;
		_nEquals = notEqual;
	}

	/**
	 * 
	 * @param n Compare and equals only over first n elements of array
	 */
	public NodeComparator(int n){
		_n = n;
	}
	
	/**
	 * 
	 * @param order The order in which the comparison should be made.
	 * For example, for sorting by POCS order use { 1, 2, 3, 0}.
	 * Equals functions operates as normal.
	 */
	public NodeComparator(int[] order){
		_order = order;
	}
	
	/**
	 * Equivalent to NodeComparator(true);
	 */
	public NodeComparator(){
		;
	}
	
	/**
	 * Constructor with NodeComparatorArgs setup
	 */
	public NodeComparator(NodeComparatorArgs nca){
		_variableLength = nca._varLength;
		_nZero = nca._nZero;
		_nEquals = nca._nEquals;
		_numeric = nca._numeric;
		_reverse = nca._reverse;
		_order = nca._order;
		_n = nca._n;
	}
	
	/**
	 * Compare two node arrays given the constructor restrictions.
	 */
	public int compare(Node[] n1, Node[] n2) {
		if(n1==n2){
			return 0;
		}
		
		int min = Math.min(Math.min(n1.length, n2.length),_n);
		for (int i=0; i<min; i++) {
			if(_order!=null){
				if(_order.length==i){
					if(_nZero)
						return -1;
					return 0;
				}
				
				int index = _order[i];
				if(index>=n1.length || index>=n2.length){
					_log.warning("Cannot compare "+Nodes.toN3(n1)+" with "+Nodes.toN3(n2)+" for order "+_order);
					return Integer.MIN_VALUE;
				}else{
					int comp = compare(n1[index], n2[index], _numeric!=null && _numeric.length>index && _numeric[index], _reverse!=null && _reverse.length>index && _reverse[index] );
					if (comp != 0)
						return comp;
				}
			}else{
				int comp = n1[i].compareTo(n2[i]);
				if (comp != 0)
					return comp;
			}
		}
		
		if(!_variableLength){
			int dif = n1.length - n2.length;
			if(dif!=0){
				return dif;
			}
		}

		if(_nZero)
			return -1;
		return 0;
	}
	
	private int compare(Node a, Node b, boolean numeric, boolean reverse) throws NumberFormatException {
		int comp;
		if(!numeric){
			comp =  a.compareTo(b);
		} else{
			double da = Double.parseDouble(a.toString());
			double db = Double.parseDouble(b.toString());
			comp = Double.compare(da, db);
		}
		
		if(reverse){
			comp*=-1;
		}
		return comp;
	}

	public boolean equals(Node[] n1, Node[] n2){
		if(n1==n2)
			return true;
		if(_nEquals)
			return false;
		if(!_variableLength){
			int dif = n2.length - n1.length;
			if(dif!=0){
				return false;
			}
		}
		for (int i=0; i<Math.min(Math.min(n1.length, n2.length),_n); i++) {
			if(_order!=null){
				if(_order.length==i)
					return true;
				
				int index = _order[i];
				if(index>=n1.length || index>=n2.length){
					_log.warning("Cannot compare "+Nodes.toN3(n1)+" with "+Nodes.toN3(n2)+" for order "+_order);
					return false;
				}else if(!n1[index].equals(n2[index]))
					return false;
			}
			else if(!n1[i].equals(n2[i]))
				return false;
		}
		
		return true;
	}
	
	public static final class NodeComparatorArgs{
		boolean _varLength = false;
		boolean _nZero = false;
		boolean _nEquals = false;
		boolean[] _numeric = null;
		boolean[] _reverse = null;
		int[] _order = null;
		int _n = Integer.MAX_VALUE;
		
		
		public NodeComparatorArgs(){
			;
		}
		
		public void setVarLength(boolean varLength){
			_varLength = varLength;
		}
		
		public void setNoZero(boolean nZero){
			_nZero = nZero;
		}
		
		public void setNoEquals(boolean nEquals){
			_nEquals = nEquals;
		}
		
		/**
		 * For compare only
		 * @param numeric
		 */
		public void setNumeric(boolean[] numeric){
			_numeric = numeric;
		}
		
		/**
		 * For compare only
		 * @param reverse
		 */
		public void setReverse(boolean[] reverse){
			_reverse = reverse;
		}
		
		public void setOrder(int[] order){
			_order = order;
		}
		
		public void setCompareMax(int n){
			_n = n;
		}
		
		public static int[] getIntegerMask(String arg){
			int[] reorder = new int[arg.length()];
			
			for(int i=0; i<reorder.length; i++){
				reorder[i] = Integer.parseInt(Character.toString(arg.charAt(i)));
			}
			
			return reorder;
		}
		
		public static boolean[] getBooleanMask(String arg){
			int[] m = getIntegerMask(arg);
			
			int max = 0;
			for(int i:m){
				if(max<i)
					max = i;
			}
			
			boolean[] bmask = new boolean[max+1];
			
			for(int i:m){
				bmask[i] = true;
			}
			
			return bmask;
		}
	}
	
	public static void main(String args[]){
		BNode a = new BNode("a");
		BNode b = new BNode("b");
		BNode c = new BNode("c");
		Node[] na = new Node[]{a};
		Node[] nab = new Node[]{a,b};
		Node[] nabc = new Node[]{a,b,c};
		Node[] nc = new Node[]{c};
		Node[] nccc = new Node[]{c,c,c};
		
		TreeSet<Node[]> sorted = new TreeSet<Node[]>(NodeComparator.NC);
		sorted.add(na);
		sorted.add(nab);
		sorted.add(nabc);
		sorted.add(nc);
		sorted.add(nccc);
		
		Iterator<Node[]> iter = sorted.iterator();
		
		while(iter.hasNext()){
			System.err.println(Nodes.toN3(iter.next()));
		}
	}
}

