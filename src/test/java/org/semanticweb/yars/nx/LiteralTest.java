package org.semanticweb.yars.nx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LiteralTest {

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

	@Test
	public void testEmptyString() {
		String quotedEmptyStringData = "\"\"";
		Literal l = new Literal(quotedEmptyStringData, true);

		assertNull(l.getLanguageTag());
		assertNull(l.getDatatype());
		assertEquals("", l.getLabel());
		assertEquals(quotedEmptyStringData, l.toString());
	}

	@Test
	public void testMultilineString() {
		String s = "a\nb\nc";
		Literal l = new Literal(s);

		assertEquals("\"a\\nb\\nc\"", l.toString());
	}

	@Test
	public void testTabString() {
		String s = "a\tb\tc";
		Literal l = new Literal(s);

		assertEquals("\"a\\tb\\tc\"", l.toString());
	}
	
	@Test
	public void testQuotesInString() {
		String s = "he said \"wow\"";
		Literal l = new Literal(s);

		assertEquals("\"he said \\\"wow\\\"\"", l.toString());
	}

	@Test
	public void testQuotesInString2() {
		String s = "he said \"Shakespeare said 'to be or not to be'\"";
		Literal l = new Literal(s);

		assertEquals(
				"\"he said \\\"Shakespeare said 'to be or not to be'\\\"\"",
				l.toString());
	}

}