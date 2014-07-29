package org.semanticweb.yars.nx;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.parser.ParseException;

/**
 * A bnode, anonymous resource.
 * 
 * @author Andreas Harth
 * @author Tobias Kaefer
 */
public class BNode implements Node {
	private static Logger _log = Logger.getLogger(BNode.class.getName());

	static String PREFIX = "_:";

	public static boolean PRETTY_PRINT = false;

	// the value of the bnode in N3 syntax (including _:)
	private final String _data;

	// version number for serialization
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor if we have a bnode with a nodeID. Need to add context of the
	 * file to the nodeID later, otherwise there will be clashes.
	 */
	public BNode(String nodeid) {
		this(nodeid, false);
	}

	public BNode(String nodeid, boolean isN3) {
		if (isN3) {
			_data = nodeid;
		} else if (!nodeid.startsWith(PREFIX)) {
			_data = (PREFIX + nodeid);
		} else {
			_data = nodeid;
		}
	}

	/**
	 * Get URI.
	 */
//	@Override
//	public String toString() {
//		if (PRETTY_PRINT) {
//			try {
//				String[] conb = parseContextualBNode();
//				return conb[1] + "@[" + conb[0] + "]";
//			} catch (ParseException pe) {
//				return unescapeForBNode(_data.substring(PREFIX.length()));
//			}
//		} else {
//			return _data.substring(PREFIX.length());
//		}
//	}
	
	public String getBNodeString() {
		return _data.substring(PREFIX.length());
	}

	@Override
	public String toString() {
		return _data;
	}

	/**
	 * Equality check
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		return (o instanceof BNode) && ((BNode) o)._data.equals(_data);
	}

	@Override
	public int hashCode() {
		return _data.hashCode();
	}

	public String[] parseContextualBNode() throws ParseException {
		String d = _data.substring(PREFIX.length());
		String[] uri = d.toString().split("xx");
		if (uri.length != 2) {
			throw new ParseException("Not a valid context encoded BNode " + _data);
		}
		uri[0] = unescapeForBNode(uri[0]);
		uri[1] = unescapeForBNode(uri[1]);
		return uri;
	}

	public static String[] parseContextualBNode(BNode b) throws ParseException {
		String[] uri = b.toString().split("xx");
		if (uri.length != 2) {
			throw new ParseException("Not a valid context encoded BNode " + b);
		}
		uri[0] = unescapeForBNode(uri[0]);
		uri[1] = unescapeForBNode(uri[1]);
		return uri;
	}

	public static BNode createBNode(String docURI, String localID) {
		String escapedDU = escapeForBNode(docURI);
		String escapedLI = escapeForBNode(localID);
		return new BNode(escapedLI + "xx" + escapedDU);
	}

	public static BNode createBNode(String unescaped) {
		String escaped = escapeForBNode(unescaped);
		return new BNode(escaped);
	}

	public static String escapeForBNode(String unescaped) {
		try {
			return URLEncoder.encode(unescaped, "utf-8").replace("x", "x78")
					.replace("-", "x2D").replace(".", "x2E")
					.replace("_", "x5F").replace("*", "x2A").replace('%', 'x');
		} catch (UnsupportedEncodingException e) {
			_log.log(Level.WARNING, "Exception: {}", e.getMessage());
			return null;
		}
	}

	public static String unescapeForBNode(String escaped) {
		try {
			return URLDecoder.decode(escaped.replace('x', '%'), "utf-8");
		} catch (UnsupportedEncodingException e) {
			_log.log(Level.WARNING, "Exception: {}", e.getMessage());
			return null;
		}
	}
}
