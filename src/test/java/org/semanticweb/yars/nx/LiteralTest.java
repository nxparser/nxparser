package org.semanticweb.yars.nx;

import org.junit.Test;

public class LiteralTest {

//	@Test
//	public void testString() throws Exception {
//		Literal l = new Literal("abc");
//		System.out.println(l.toN3());
//		System.out.println(l.getData());
//		System.out.println(l.getDatatype());
//		System.out.println(l.getLanguageTag());
//	}
//	
//	@Test
//	public void testEmptyString() throws Exception {
//		Literal l = new Literal("");
//		System.out.println(l.toN3());
//		System.out.println(l.getData());
//		System.out.println(l.getDatatype());
//		System.out.println(l.getLanguageTag());
//	}

	@Test
	public void testEmptyStringN3() throws Exception {
		String data = "\"\"";
		System.out.println(data.substring(1, data.length() - 1));

		
		Literal l = new Literal("\"\"", true);
		System.out.println(l.toString());
		System.out.println(l.getLiteralString());
		System.out.println(l.getDatatype());
		System.out.println(l.getLanguageTag());
	}

//
//	@Test
//	public void testInt() throws Exception {
//		Literal l = new Literal(5+"");
//		System.out.println(l.toN3());
//		System.out.println(l.getData());
//		System.out.println(l.getDatatype());
//		System.out.println(l.getLanguageTag());
//	}

}
