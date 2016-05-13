package org.semanticweb.yars.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
	
	private static final Logger LOG = Logger.getLogger(Util.class.getName());

	private Util() {
	}

	public static final String THIS_SCHEME_AND_AUTHORITY_STRING = "http://this.nxparser.github.io";
	public static final String THIS_PATH_STRING = "/reference/to/URI/of/current/rdf/graph/for/representing/permanently/relative/URIs/in/N-Triples/";
	public static final String THIS_STRING = THIS_SCHEME_AND_AUTHORITY_STRING + THIS_PATH_STRING;

	/**
	 * The well-known URI to handle relative URIs in the N-Triples bases internal data model of NxParser:
	 * 
	 * {@code http://this.nxparser.github.io/reference/to/URI/of/current/rdf/graph/for/representing/permanently/relative/URIs/in/N-Triples/ }
	 */
	public static final URI THIS_URI;
	
	public static final Path THIS_PATH = Paths.get(THIS_PATH_STRING);

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

	/**
	 * Get URI relativised to {@link #THIS_URI} if it is on the same scheme and
	 * authority.
	 *
	 * @param uri The URI to get relativised
	 * @return The relativised URI
	 */
	public static URI getPossiblyRelativisedUri(URI uri) {
		// Implemented abusing Java7's {@link Path}, which is intended for file
		// paths, for http URIs.

		URI ret = uri;

		if (THIS_URI.getScheme().equalsIgnoreCase(uri.getScheme())
				&& THIS_URI.getAuthority().equals(uri.getAuthority())) {
			
			// Chopping off scheme and authority to process the URI using Path
			String uripathstring = uri.toString();
			uripathstring = uripathstring.substring(THIS_SCHEME_AND_AUTHORITY_STRING.length(), uripathstring.length());
		
			try {
				ret = new URI(THIS_PATH.relativize(Paths.get(uripathstring)).toString());
			} catch (URISyntaxException e) {
				LOG.log(Level.WARNING, "Had an issue relativising URI {0}", new Object[] { uri });
			}
		}

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
