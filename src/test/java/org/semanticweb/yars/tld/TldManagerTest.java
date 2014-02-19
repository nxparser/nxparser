package org.semanticweb.yars.tld;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class TldManagerTest extends TestCase {

	public void testGetPLD() throws IOException, URISyntaxException {
		TldManager tm = new TldManager();

		List<Object[]> testdata = new LinkedList<Object[]>();

		testdata.add(new Object[]{new URI("http://www.bbc.co.uk/blabla)"),	"bbc.co.uk"});
		testdata.add(new Object[]{new URI("http://www.example.org/)"),		"example.org"});
		testdata.add(new Object[]{new URI("http://www.example.org"),		"example.org"});
		testdata.add(new Object[]{new URI("http://example.org/123"),		"example.org"});
		testdata.add(new Object[]{new URI("https://example.org/123"),		"example.org"});
		testdata.add(new Object[]{new URI("https://example.org/123"),		"example.org"});
		testdata.add(new Object[]{new URI("https://lksdfjsdf.bbc.co.uk"),	"bbc.co.uk"});
		testdata.add(new Object[]{new URI("https://lksdfjsdf.bbc.co.uk."),	"bbc.co.uk"});
		testdata.add(new Object[]{new URI("https://bla.aero."),				"bla.aero"});
		testdata.add(new Object[]{new URI("https://bla.express.aero."),		"bla.express.aero"});
		testdata.add(new Object[]{new URI("https://12.1.bla.express.aero."),"bla.express.aero"});
		testdata.add(new Object[]{new URI("https://a.abira.hokkaido.jp"),   "a.abira.hokkaido.jp"});
		testdata.add(new Object[]{new URI("https://kuerbis.hokkaido.jp"),   "kuerbis.hokkaido.jp"});
		// java.net.URI finds no host here:
		testdata.add(new Object[]{new URI("https://a.lurøy.no"),   			null});		
		testdata.add(new Object[]{new URI("https://org."),					"org"});
		testdata.add(new Object[]{new URI("https://org"),					"org"});
		testdata.add(new Object[]{new URI("https://uk"),					"uk"});
		testdata.add(new Object[]{new URI("https://uk/234"),				"uk"});
		testdata.add(new Object[]{new URI("https://192.168.2.1/ldkjflksd"), "192.168.2.1"});
		testdata.add(new Object[]{new URI("https://192.168.2.1:88/ldajfsd"),"192.168.2.1"});

		testdata.add(new Object[]{new URI("https://192.168.3.1./ldkjfksd"), null}); // not valid?
		testdata.add(new Object[]{new URI("https://./123"),					null});
		testdata.add(new Object[]{new URI("https://."),						null});
		try {
		testdata.add(new Object[]{new URI("https://"),						null});
		} catch (URISyntaxException u) {

		}
		testdata.add(new Object[]{
				new URI(
						"http://www.rkbexplorer.com/sameAs/?uri=http%3A%2F%2Facm.rkbexplorer.com%2Fid%2Fperson-715486-950006ecd385595c1641ecb94ea283a1"),
																			"rkbexplorer.com"});

		int i = 0;

		for (Object[] e : testdata) {
			++i;
			String expected = (String) e[1];
			String actual = tm.getPLD((URI) e[0]);

			if (expected == null) {
				assertNull(actual);
			} else
				assertEquals(expected, actual);
		}
		System.out.println("URIs tested: " + i);
	}

}
