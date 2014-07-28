package org.semanticweb.yars.nx;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.util.NxUtil;

/**
 * An RDF resource (http://...).
 * 
 * @author Andreas Harth
 * @author Tobias Kaefer
 */
public class Resource implements Node, Serializable {
	private static Logger _log = Logger.getLogger(Resource.class.getName());

	// the value of the resource (now includes < >)
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
	 * Constructor. Does some escaping so is possibly not too fast. Escaping is
	 * done using {@link NxUtil#escapeForNx(String)} after
	 * {@link URI#toASCIIString()}.
	 */
	public Resource(URI uri) {
		this("<" + NxUtil.escapeForNx(uri.toASCIIString()) + ">", true);
	}

	/**
	 * Constructor. Assuming conformance to the spec, which can be achieved e.g.
	 * using {@link NxUtil#escapeForNx(String)} after
	 * {@link URI#toASCIIString()}.
	 * 
	 * @see <a href="http://www.w3.org/TR/rdf-testcases/#sec-uri-encoding">The
	 *      spec</a>
	 * @param isN3
	 *            Set this to true if angle brackets are already around URI as
	 *            required by N3.
	 */
	public Resource(String uri, boolean isN3) {
		if (!isN3) {
			if (uri.length() == 0 || uri == null) {
				_log.log(Level.WARNING, "Empty string not allowed as Resource");

				if (uri == null) {
					_data = null;
				} else {
					_data = "";
				}
				return;
			}
			if (uri.charAt(0) != '<') {
				_data = ("<" + uri + ">").intern();
			} else {
				_data = uri.intern();
			}
		} else {
			_data = uri.intern();
		}
	}

	public String getHost() throws URISyntaxException {
		return toURI().getHost();
	}

	@Override
	public String toString() {
		return NxUtil.unescape(toN3().substring(1, toN3().length() - 1));
	}

	/**
	 * Returns the URI that this resource represents.
	 * 
	 * @return the URI
	 * @throws URISyntaxException 
	 */
	public URI toURI() throws URISyntaxException {
		return new URI(toString());
	}

	/**
	 * Get URI in N3 notation.
	 * 
	 */
	@Override
	public String toN3() {
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
}
