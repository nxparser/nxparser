package org.semanticweb.yars.util;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testRelativising() throws URISyntaxException {
		String[] relativeUriStrings = new String[] { "#it", "it", "it/123",
				"123#123", ""
				// TODO: as per http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6226081
				// the following do not work without using URIUtils from Apache
				// HTTPComponents or similar.
				// "../it", "../123#123", "../../../123#it"
				};

		for (String relativeUriString : relativeUriStrings) {
			URI relativeUri = new URI(relativeUriString);
			URI r = Util.THIS_URI.resolve(relativeUri);
			URI result = Util.getPossiblyRelativisedUri(r);
			System.err.println("Relative URI: " + relativeUri);
			System.err.println("Long URI:     " + r);
			System.err.println("Relativised:  " + result);
			System.err.println();
			assertEquals(relativeUri, result);
		}
	}

}
