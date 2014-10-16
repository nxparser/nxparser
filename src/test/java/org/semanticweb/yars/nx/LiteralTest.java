package org.semanticweb.yars.nx;

import org.junit.Test;
import static org.junit.Assert.*;

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
	public void testLangTag() {
		String data = "\"Test{}…\"@en";
		Literal l = new Literal(data, true);
		assertEquals("en", l.getLanguageTag());
	}
	
	@Test
	public void testIRIREF() {
		String data = "\"Test{}…\"^^<http://example.org>";
		Literal l = new Literal(data, true);
		assertEquals("<http://example.org>", l.getDatatype().toString());
	}
	
	@Test
	public void testGetLabel() {
		String data = "Test\"{}…";
		Literal l = new Literal(data);
		System.out.println(l);
		assertEquals("Test\"{}…", l.getLabel());
	}	
	
//	@Test
	public void testEmptyStringN3() throws Exception {
		String data = "\"\"";
		System.out.println(data.substring(1, data.length() - 1));

		
		Literal l = new Literal("\"\"", true);
		System.out.println(l.toString());
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