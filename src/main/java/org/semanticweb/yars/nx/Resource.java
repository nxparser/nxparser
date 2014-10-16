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
 * @author Leonard Lausen
 */
public class Resource implements Node, Serializable {
	private static Logger _log = Logger.getLogger(Resource.class.getName());

	// the value of the Iri in N3 syntax (including <>)
	String _data;

	// version number for serialisation
	public static final long serialVersionUID = 2l;

	/**
	 * Constructor. Assumes valid IRI.
	 * 
	 * @see <a href="http://tools.ietf.org/html/rfc3987">The IRI spec</a>
	 */
	public Resource(String iri) {
		this(iri, false);
	}

	/**
	 * Constructor. Assumes valid IRI or valid N3 (including <> brackets).
	 * 
	 * @see <a href="http://tools.ietf.org/html/rfc3987">The IRI spec</a> and
	 * <a href="http://www.w3.org/TR/n-triples/">The N3 spec</a>
	 * @param isN3 If true expects valid N3, else valid IRI.
	 */
	public Resource(String iri, boolean isN3) {
		if (!isN3) {
			if (iri == null || iri.length() == 0) {
				// maybe throw Exception, or just be silent
				_log.log(Level.WARNING, "Empty string not allowed.");

				_data = iri;
			} else if (iri.charAt(0) != '<') {
				//TODO remove?
				_data = ("<" + NxUtil.escapeIRI(iri) + ">");
			} else {
				_data = NxUtil.escapeIRI(iri);
			}
		} else {
			_data = iri;
		}
	}

	/**
	 * Returns the URI that this resource represents. Be careful, as Java URI represents
	 * URIs according to RFC2396 and not IRIs according to RFC3986.
	 * 
	 * @return the URI
	 * @throws URISyntaxException 
	 */
	public URI toURI() throws URISyntaxException {
		return new URI(getUriString());
	}

	@Deprecated
	public String getUriString() {
		return NxUtil.unescapeIRI(toString().substring(1, toString().length() - 1));
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