package org.semanticweb.yars.nx;

import org.junit.Test;

public class LiteralTest {

	@Test
	public void testString() throws Exception {
		Literal l = new Literal("abc");
		System.out.println(l.toN3());
		System.out.println(l.getData());
		System.out.println(l.getDatatype());
		System.out.println(l.getLanguageTag());
	}

	@Test
	public void testInt() throws Exception {
		Literal l = new Literal(5+"");
		System.out.println(l.toN3());
		System.out.println(l.getData());
		System.out.println(l.getDatatype());
		System.out.println(l.getLanguageTag());
	}

}
