package org.semanticweb.yars.util;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUMapCache<E,F> extends LinkedHashMap<E, F>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int DEFAULT_CACHE_SIZE = 500;
	
	public static float DEFAULT_LOAD_FACTOR = 0.75f;
	
	private int _cache_size;
	
	public LRUMapCache(){
		this(DEFAULT_CACHE_SIZE, DEFAULT_LOAD_FACTOR);
	}
	
	public LRUMapCache(int cache){
		this(cache, DEFAULT_LOAD_FACTOR);
	}
	
	public LRUMapCache(int cache, float loadFactor){
		super(cache, loadFactor, true);
		_cache_size = cache;
	}
	
	
	protected boolean removeEldestEntry(Map.Entry<E,F> entry){
		return size() >= _cache_size;
	}
}
