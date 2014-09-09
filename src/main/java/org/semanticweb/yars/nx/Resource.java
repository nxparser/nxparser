package org.semanticweb.yars.nx;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.util.NxUtil;

/**
 * An Iri (http://...).
 * 
 * @author Andreas Harth
 * @author Tobias Kaefer
 */
public class Resource implements Node, Serializable {
	private static Logger _log = Logger.getLogger(Resource.class.getName());

	// the value of the Iri in N3 syntax (including <>)
	String _data;

	// version number for serialisation
	public static final long serialVersionUID = 1l;

	/**
	 * Constructor. Assumes no angle brackets around the uri like in Nx.
	 * Assuming conformance to the spec, which can be achieved e.g. using
	 * {@link NxUtil#escapeForNx(String)} after {@link URI#toASCIIString()}.
	 * 
	 * @see <a href="http://www.w3.org/TR/rdf-testcases/#sec-uri-encoding">The
	 *      spec</a>
	 */
	public Resource(String uri) {
		this(uri, false);
	}

	/**
	 * Constructor. Assuming conformance to the spec, which can be achieved e.g.
	 * using {@link NxUtil#escapeForNx(String)} after
	 * {@link URI#toASCIIString()}.
	 * 
	 * @see <a href="http://www.w3.org/TR/rdf-testcases/#sec-uri-encoding">The
	 *      spec</a>
	 * @param isN3 - true if angle brackets are already around URI as required by N3.
	 */
	public Resource(String uri, boolean isN3) {
		if (!isN3) {
			if (uri == null || uri.length() == 0) {
				// maybe throw Exception, or just be silent
				_log.log(Level.WARNING, "Empty string not allowed.");

				_data = uri;
			} else if (uri.charAt(0) != '<') {
				_data = ("<" + uri + ">");
			} else {
				_data = uri;
			}
		} else {
			_data = uri;
		}
	}

	/**
	 * Returns the URI that this resource represents.
	 * 
	 * @return the URI
	 * @throws URISyntaxException 
	 */
	public URI toURI() throws URISyntaxException {
		return new URI(getUriString());
	}

	@Deprecated
	public String getUriString() {
		return NxUtil.unescape(toString().substring(1, toString().length() - 1));
	}
	
	@Override
    public String getLabel() {
    	return getUriString();
    }

	/**
	 * Get URI in N3 notation.
	 * 
	 */
//	@Override
	public String toString() {
		return _data;
	}

	/**
	 * Needed for storing in hashtables.
	 */
	@Override
	public int hashCode() {
		return _data.hashCode();
	}

	/**
	 * Equality check.
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		return  (o instanceof Resource)
				&& ((Resource) o)._data.equals(_data);
	}
	
	@Override
	public int compareTo(Node n) {
		return toString().compareTo(n.toString());
	}
}