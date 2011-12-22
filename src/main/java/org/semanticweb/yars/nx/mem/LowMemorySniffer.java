package org.semanticweb.yars.nx.mem;

import java.lang.ref.SoftReference;
import java.util.TreeSet;

public class LowMemorySniffer {
	private static final int SOFT_BYTE_ARRAY_LEN = 1024*512;
	private static final long UNSAFE_LOW_MEM = 1024*1024*5;
	
	private SoftReference<Byte[]> _soft;
	
	public LowMemorySniffer(){
		_soft = new SoftReference<Byte[]>(new Byte[SOFT_BYTE_ARRAY_LEN]);
	}
	
	/**
	 * 
	 * @return true if heap-space is running low
	 */
	public boolean lowMemory(){
		if(_soft.get()==null){
			long free = MemoryManager.estimateFreeSpace();
			if(free<UNSAFE_LOW_MEM){
				System.err.println("Free mem: "+free);
				return true;
			}
			_soft = new SoftReference<Byte[]>(new Byte[SOFT_BYTE_ARRAY_LEN]);
		}
		return false;
	}
	
	public static void main(String[] args){
		TreeSet<Integer> ts = new TreeSet<Integer>();
		LowMemorySniffer lms = new LowMemorySniffer();
		
		int i = 0, l = 0;
		while(true){
			try{
				i++;
				ts.add(i);
				if(l==0 && lms.lowMemory()){
					l = i;
				}
			}catch(OutOfMemoryError oome){
				System.err.println(i+" "+l);
				break;
			}
		}
	}
}