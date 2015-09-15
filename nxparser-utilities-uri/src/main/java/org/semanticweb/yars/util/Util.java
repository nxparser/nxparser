package org.semanticweb.yars.util;

import java.net.URI;
import java.net.URISyntaxException;

public class Util {

	private Util() {
	}

	public static final String THIS_SCHEME_AND_AUTHORITY = "http://this.nxparser.github.io/";
	public static final String THIS_STRING = THIS_SCHEME_AND_AUTHORITY
			+ "reference/to/URI/of/current/rdf/graph/for/representing/permanently/relative/URIs/in/N-Triples/";
	public static final URI THIS_URI;

	static {
		try {
			THIS_URI = new URI(THIS_STRING);
		} catch (URISyntaxException e) {
			// should not happen.
			e.printStackTrace();
			throw new RuntimeException(
					"Could not init class as this is no proper URI: "
							+ THIS_STRING, e);
		}
	}

	public static URI getPossiblyRelativisedUri(URI uri) {

		URI ret = uri;

		if (THIS_URI.getScheme().equalsIgnoreCase(uri.getScheme())
				&& THIS_URI.getAuthority().equals(uri.getAuthority()))
			// TODO: handle URIs that traverse up the path hierarchy
			ret = Util.THIS_URI.relativize(uri.normalize());

		return ret;
	}
}
