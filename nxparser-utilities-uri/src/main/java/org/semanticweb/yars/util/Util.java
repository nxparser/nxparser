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
	
	private static final URI SAME_DOCUMENT_REFERENCE ;
	
	static {
		String uristring = "http://ex.org/123"; 
		try {
			SAME_DOCUMENT_REFERENCE = new URI(uristring).relativize(new URI(uristring));
		} catch (URISyntaxException e) {
			// should not happen
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Fiddling with URIs to handle
	 * <a href="http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4708535">
	 * Java bug #4708535</a>, which says that {@link URI#resolve(URI)} is buggy
	 * when it comes to the same document reference, ie. relative URIs that are
	 * "".
	 * 
	 * @param uriToResolveAgainst
	 *            The URI to resolve against
	 * @param possiblyRelaitveUriToResolve
	 *            The possibly relative URI
	 * @return The possibly relative URI resolved against the other URI with
	 *         proper handling of the same document reference
	 */
	public static URI properlyResolve(URI uriToResolveAgainst, URI possiblyRelaitveUriToResolve) {
		if (SAME_DOCUMENT_REFERENCE.equals(possiblyRelaitveUriToResolve)) 
			return uriToResolveAgainst;
		else 
			return uriToResolveAgainst.resolve(possiblyRelaitveUriToResolve);
	}
}
