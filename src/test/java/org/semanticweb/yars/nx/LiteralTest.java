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

		// assertEquals("\"a\\tb\\tc\"", l.toString());
		//
		// Tabs do no have to be espace; only quotes, LF and CR and backslash:
		// http://www.w3.org/TR/n-triples/#sec-literals
		assertEquals("\"a\tb\tc\"", l.toString());
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

	@Test
	public void testCanonicalNtriplesLiteral() {
		StringBuffer sb = new StringBuffer();

		// only the following four characters should be escaped:
		sb.append((char) 0x22); // quotation mark
		sb.append((char) 0x5c); // slash
		sb.append((char) 0x0a); // carriage return
		sb.append((char) 0x0d); // line feed

		// not others like for example:
		sb.append((char) 0x08); // backspace
		sb.append((char) 0x09); // tab
		sb.append("ä");
		sb.append("'");
		sb.append((char) 0x0e07); // a Thai character
		sb.append((char) 0x1ce1); // a Vedic Character
		char[] pair = Character.toChars(0x100002);
		sb.append(pair[0]); // UTF-16 surrogate pair part 1
		sb.append(pair[1]); // UTF-16 surrogate pair part 2

		Literal l = new Literal(sb.toString());

		assertEquals("\"\\\"\\\\\\n\\r\u0008\tä'\u0e07\u1ce1\udbc0\udc02\"",
				l.toString());
	}
}