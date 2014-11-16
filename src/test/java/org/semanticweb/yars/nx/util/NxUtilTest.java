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
		assertEquals("http://ヒト\\/{}*?()-:@ſ&=><!^][_…#$|~`+%\"';",  NxUtil.unescapeIRI(NxUtil.escapeIRI("http://ヒト\\/{}*?()-:@ſ&=><!^][_…#$|~`+%\"';")));
	}
	
	@Test
	public void testNormalize() {
		assertEquals("http://example.org/test?uiae#123", NxUtil.normalize("HTTp://eXAMple.oRg/test?uiae#123"));
		assertEquals("http://example.org/test#123", NxUtil.normalize("HTTp://eXAMple.oRg/test#123"));
		assertEquals("http://example.org/test?uiae", NxUtil.normalize("HTTp://eXAMple.oRg/test?uiae"));
		assertEquals("http://example.org/", NxUtil.normalize("HTTp://eXAMple.oRg:80/"));
		assertEquals("http://example.org/", NxUtil.normalize("HTTp://eXAMple.oRg:/"));
		assertEquals("http://example.org:8080/", NxUtil.normalize("HTTp://eXAMple.oRg:8080/"));
	}
	
	@Test
	public void testDots() {
		assertEquals("http://example.org/a/g", NxUtil.normalize("HTTp://eXAMple.oRg/a/b/c/./../../g"));
		assertEquals("http://example.org/a/b/", NxUtil.normalize("HTTp://eXAMple.oRg/a/b/c/g/../.."));
		assertEquals("http://example.org/a/b/", NxUtil.normalize("HTTp://eXAMple.oRg/a/b/c/g/../../."));
		assertEquals("http://example.org/", NxUtil.normalize("HTTp://eXAMple.oRg/../"));
		assertEquals("http://example.org/", NxUtil.normalize("HTTp://eXAMple.oRg/.."));
		assertEquals("http://example.org/", NxUtil.normalize("HTTp://eXAMple.oRg/."));
		assertEquals("http://example.org/", NxUtil.normalize("HTTp://eXAMple.oRg"));
		assertEquals("http://example.org/mid/6", NxUtil.normalize("HTTp://eXAMple.oRg/mid/content=5/../6"));
		assertEquals("http://example.org/?uiae#123", NxUtil.normalize("HTTp://eXAMple.oRg/test/..?uiae#123"));
		assertEquals("http://example.org/?uiae#123", NxUtil.normalize("HTTp://eXAMple.oRg?uiae#123"));
	}
	
	@Test
	public void testNormalizeUnicodeComposition() {
		assertEquals("http://example.org/ä?uääe#123ä", NxUtil.normalize("HTTp://eXAMple.oRg/ä?uääe#123ä"));
	}
	
	@Test
	public void testPercentEncoding() {
		assertEquals("http://example.org/testü%22?uiae", NxUtil.normalize("HTTp://eXAMple.oRg/test%C3%BC%22?uiae"));
		
		// 3 Byte UTF8
		assertEquals("http://example.org/퀀?퀂", NxUtil.normalize("HTTp://eXAMple.oRg/%ED%80%80?%ED%80%82"));

		//Invalid UTF8
		assertEquals("http://example.org/%E4", NxUtil.normalize("HTTp://eXAMple.oRg/%E4"));

		// Private & 4 Byte UCS
		assertEquals("http://example.org/%F3%B0%80%80?󰀀", NxUtil.normalize("HTTp://eXAMple.oRg/%F3%b0%80%80?%F3%B0%80%80"));
		assertEquals("%3C%3E%7B%7D", NxUtil.caseNormalizePercentEncoding("%3c%3e%7b%7d"));
	}
}
