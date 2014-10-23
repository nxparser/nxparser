/**
 * 
 */
package org.semanticweb.yars.nx.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.util.NxUtil;

/**
 * @author Leonard Lausen
 *
 */
public class NxUtilTest {

	/**
	 * Test method for {@link org.semanticweb.yars.nx.util.NxUtil#escapeIRI(java.lang.String)}.
	 */
	@Test
	public void testEscapeIRI() {
		assertEquals(NxUtil.escapeIRI("<>\"{}|^`\\"), "\\u003C\\u003E\\u0022\\u007B\\u007D\\u007C\\u005E\\u0060\\u005C");
		for (int i = 1; i <= 32; i++) {
			assertEquals(NxUtil.escapeIRI(String.valueOf((char) i)), String.format("\\u%04X", i));
		}
	}
	
	@Test
	public void testIRI() {
		System.out.println(NxUtil.escapeIRI("http://ヒト\\/{}*?()-:@ſ&=><!^][_…#$|~`+%\"';"));
		assertEquals("http://ヒト\\/{}*?()-:@ſ&=><!^][_…#$|~`+%\"';",  NxUtil.unescapeIRI(NxUtil.escapeIRI("http://ヒト\\/{}*?()-:@ſ&=><!^][_…#$|~`+%\"';")));
	}
	
	@Test
	public void testNormalize() {
		assertEquals("http://example.org/test?uiae#123", NxUtil.normalize("HTTp://eXAMple.oRg/test?uiae#123"));
		assertEquals("http://example.org/test#123", NxUtil.normalize("HTTp://eXAMple.oRg/test#123"));
		assertEquals("http://example.org/test?uiae", NxUtil.normalize("HTTp://eXAMple.oRg/test?uiae"));
	}
	
	@Test
	public void testNormalizeUnicodeComposition() {
		assertEquals("http://example.org/ä?uääe#123ä", NxUtil.normalize("HTTp://eXAMple.oRg/ä?uääe#123ä"));
	}
	
	@Test
	public void testPercentEncoding() {
		assertEquals("<>{}", NxUtil.unescapePercentEncoding("%3C%3E%7B%7D"));
		assertEquals("http://example.org/test{|}?uiae", NxUtil.normalize("HTTp://eXAMple.oRg/test%7B%7C%7D?uiae", false));
	}
}
