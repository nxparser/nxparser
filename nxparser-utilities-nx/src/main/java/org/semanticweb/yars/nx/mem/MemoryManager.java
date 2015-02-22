package org.semanticweb.yars.nx.mem;

public class MemoryManager {
	/**
	 * very conservative value for number of nodes that can
	 * fit in memory
	 * 
	 * EDIT: reduced from 1024 to 800 due to swapping issues
	 */
	public static final int NODES_PER_MB_IN_MEM = 800;
	
	/**
	 * estimate total heap space
	 **/
	public static final long estimateHeapSpace(){
		long max = Runtime.getRuntime().maxMemory();
		if(max==Long.MAX_VALUE){
			max = Runtime.getRuntime().freeMemory();
		} 
		return max;
	}
	
	/**
	 * estimate free heap space available in VM, 
	 * used for calculating in memory batches
	 **/
	public static final long estimateFreeSpace(){
		System.gc();
		long max = Runtime.getRuntime().maxMemory();
		long used = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		
		return free + (max - used);
	}
	
	public static final int estimateMaxNodes(){
		return (int)((estimateFreeSpace()/ (1024 * 1024)) * NODES_PER_MB_IN_MEM);
	}
	
	public static final int estimateMaxStatements(int stmtLength){
		return (int)((estimateFreeSpace()/ (1024 * 1024)) * (NODES_PER_MB_IN_MEM / stmtLength));
	}
}
