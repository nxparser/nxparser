package org.semanticweb.yars.util;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testRelativisingUsingUri() throws URISyntaxException {
		String[] relativeUriStrings = new String[] { "#it", "it", "it/123",
				"123#123", "", 
				 "../it", "../123#123", "../../../123#it"
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
	
	@Test
	public void testRelativisingUsingPath() throws URISyntaxException {
		String[] relativeUriStrings = new String[] { "#it", "it", "it/123",
				"123#123", "",
				 "../it", "../123#123", "../../../123#it"
				};

		for (String relativeUriString : relativeUriStrings) {
			Path relativeUri = Paths.get(relativeUriString);
			Path r = Paths.get(Util.THIS_URI.getPath()).resolve(relativeUri).normalize();
			Path result = Paths.get(Util.THIS_URI.getPath()).relativize(r);
			System.err.println("Relative URI: " + relativeUri);
			System.err.println("Long URI:     " + r);
			System.err.println("Relativised:  " + result);
			System.err.println();
			assertEquals(relativeUri, result);
		}
	}

}
