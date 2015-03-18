package org.semanticweb.yars.nx;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeCompareTest {

	@Test
	public void testCompare() {
		Resource r1 = new Resource("http://example.org/one");
		Resource r2 = new Resource("http://example.org/two");

		System.out.println(r1.hashCode());
		System.out.println(r2.hashCode());

		System.out.println(r1.equals(r2));
		assertFalse(r1.equals(r2));
		
		System.out.println(((Node)r1).compareTo((Node)r2));
	}
}
