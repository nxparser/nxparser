package org.semanticweb.yars.util;

public class Array<E extends Comparable<E>> implements Comparable<Array<E>> {
	private E[] _array;
	
	public static final int HASHCODE_SEED = 23;
	public static final int PRIME = 37;
	
	private int _hashCode;
	
	public Array(E[] array){
		_array = array;
		_hashCode = getHashCode();
	}
	
	public E[] getArray(){
		return _array;
	}
	
	public boolean equals(Array<E> o){
		if(o==null)
			return false;
		if(o==this)
			return true;
		if(_array.length == o._array.length)
			for(int i=0; i<_array.length; i++)
				if(!_array[i].equals(o._array[i]))
					return false;
		
		return true;
	}

	public int compareTo(Array<E> o) {
		if(o==this)
			return 0;
		if(_array.length == o._array.length){
			for(int i=0; i<_array.length; i++){
				int comp = _array[i].compareTo(o._array[i]);
				if (comp != 0)
					return comp;
			}
		}else{
			return o._array.length - _array.length;
		}
		
		return 0;
	}
	
	/**
	 * Fast hashcode method for Node arrays
	 */
	private int getHashCode() {
		return hashCode(_array, 0, _array.length);
	}
	
		
	public int hashCode() {
		return _hashCode;
	}
	
	/**
	 * FAST hashcode method for object arrays
	 */
	public static int hashCode(Object... oa) {
		return hashCode(oa, 0, oa.length);
	}
	
	/**
	 * FAST hashcode method for object arrays
	 */
	public static int hashCode(Object[] oa, int from, int to) {
		int result = HASHCODE_SEED;
		
		if ( oa == null) 
			return 0;

		for(int i=from; i<to; i++){
			Object o = oa[i];
			if(o==null){
				result = PRIME * result;
			}else{
				result = (PRIME * result) + o.hashCode();
			}
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String r="";
		for(E e:_array) {
			r+=e.toString()+" ";
		}
		return r.trim()	;
	}
}
