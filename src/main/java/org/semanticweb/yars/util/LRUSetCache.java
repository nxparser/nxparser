package org.semanticweb.yars.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;


public class LRUSetCache<E> extends AbstractSet<E> implements Set<E>{
	
	private static final long serialVersionUID = 1L;

	public static int DEFAULT_CACHE_SIZE = 100;
	
	public static float DEFAULT_LOAD_FACTOR = 0.75f;
	
	LRUMapCache<E,E> _map;
	
	public LRUSetCache(){
		this(DEFAULT_CACHE_SIZE, DEFAULT_LOAD_FACTOR);
	}
	
	public LRUSetCache(int cache){
		this(cache, DEFAULT_LOAD_FACTOR);
	}
	
	public LRUSetCache(int cache, float loadFactor){
		_map = new LRUMapCache<E,E>(cache, loadFactor);
	}
	
	/**
	 * Slightly breaking set contract, returns true
	 * iff element was not in cache.
	 */
	public boolean add(E e){
		return _map.put(e, e) == null;
	}
	
	/**
	 * True if cache contains the element,
	 * false otherwise. Will return the checked
	 * element to the head of the cache.
	 */
	public boolean contains(Object e){
		return _map.containsKey(e);
	}
	
	public void clear() {
		_map.clear();
	}

	public boolean isEmpty() {
		return _map.isEmpty();
	}

	public Iterator<E> iterator() {
		return _map.keySet().iterator();
	}

	public boolean remove(Object arg0) {
		return _map.remove(arg0)!=null;
	}

	public int size() {
		return _map.size();
	}
}
