package org.semanticweb.yars.nx;

import org.junit.Test;

public class ResourceTest {

	@Test
	public void test() {
		 System.err.println(new Resource("asd").equals(new Resource("asd")));
		 System.err.println(new Resource("asd").equals(new Resource("asdf")));
		 System.err.println(new Resource("bsd").equals(new Resource("asd")));
		
//		 long b4 = System.currentTimeMillis();
//		 for(int i=0; i<10000000; i++){
//		 "http://google.com/asd/asd".equals("http://google.com/asd/asd");
//		 "http://google.com/asd/asd".equals("http://google.com/asd/asdf");
//		 "http://google.com/asd/bsd".equals("http://google.com/asd/csd");
//		 "http://google.com/asd/csd".equals("http://google.com/asd/dsd");
//		 }
//		
//		 System.err.println(System.currentTimeMillis()-b4);
//		 b4 = System.currentTimeMillis();
//		 for(int i=0; i<10000000; i++){
//		 equals("http://google.com/asd/asd", "http://google.com/asd/asd");
//		 equals("http://google.com/asd/asd", "http://google.com/asd/asdf");
//		 equals("http://google.com/asd/bsd", "http://google.com/asd/csd");
//		 equals("http://google.com/asd/csd", "http://google.com/asd/dsd");
//		 }
//		
//		 System.err.println(System.currentTimeMillis()-b4);
//		 }
	}

}
