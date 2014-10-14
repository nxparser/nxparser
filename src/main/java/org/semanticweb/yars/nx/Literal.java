package org.semanticweb.yars.nx;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.util.NxUtil;

/**
 * An RDF literal of any data type
 * 
 * Need to do newline escaping for (string) literals otherwise it's not valid
 * RDF any more.
 * 
 * @author Andreas Harth
 * @author Tobias Kaefer
 * @author Leonard Lausen
 */
public class Literal implements Node {
	private static Logger _log = Logger.getLogger(Literal.class.getName());

	// the entire string in N3 syntax, including "", @ or ^^
	private final String _data;

	// version number for serialization
	private static final long serialVersionUID = 2L;

	/**
	 * Constructor (not in N3 syntax).
	 * 
	 * @param data - the string representation of the (simple) literal
	 */
	public Literal(String data) {
		this(data, null, null, false);
	}
	
	/**
	 * Constructor, possibly in N3 syntax.
	 * 
	 * @param data
	 * @param isN3 true if data is in N3 (i.e., with "" and ^^<> or @) - no guarantees are made, if input does not
	 * conform to the spec. Make sure your escaping is valid!
	 * @see <a href="http://www.w3.org/TR/n-triples/">The N3 spec</a>
	 */
	public Literal(String data, boolean isN3) {
		this(data, null, null, isN3);
	}

	/**
	 * Constructor (not in N3 syntax)
	 * 
	 * @param data
	 *            the not-escaped string representation of the (simple) literal
	 * @param lang
	 *            the language tag for the literal.
	 */
	public Literal(String data, String lang) {
		this(data, lang, null, false);
	}

	/**
	 * Constructor (not in N3)
	 * 
	 * @param data
	 *            the not-escaped string representation of the (simple) literal
	 * @param dt
	 *            the datatype Resource of the Literal
	 */
	public Literal(String data, Resource dt) {
		this(data, null, dt, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param data
	 *            the not-escaped string representation of the (simple) literal
	 * @param lang
	 *            the language tag for the literal.
	 * @param dt
	 *            the datatype Resource of the Literal
	 */
	public Literal(String data, String lang, Resource dt) {
		this(data, lang, dt, false);
	}

	/**
	 * Construct a literal from constituent elements (which are not in N3).
	 * 
	 * @param data
	 * @param lang
	 * @param dt
	 * @param isN3
	 *            whether parameter data is in already in N3, i.e. can be stored
	 *            without further checking (if true, parameters lang and dt get
	 *            ignored). - no guarantees are made, if input does not
	 * 			  conform to the spec. Make sure your escaping is valid!
	 * @see <a href="http://www.w3.org/TR/n-triples/">The N3 spec</a>.
	 */
	private Literal(String data, String lang, Resource dt, boolean isN3) {
		if (isN3) {
			_data = data;
		} else {
			if (data.equals("") || data.charAt(0) != '\"' || data.charAt(data.length() - 1) != '\"') {
				_log.log(Level.FINE, "Adding quotes for Literal {}",  data);
				data = NxUtil.escapeLiteral(data);
				data = '\"' + data + '\"';
			}

			if ((lang != null) && dt != null) {
				throw new IllegalArgumentException("Specify only one of language and datatype.");
			}
			
			_data = (data + ((lang == null) ? (dt == null) ? ""
					: ("^^" + dt)
					: ("@" + lang.toLowerCase())));
		}
	}

	/**
	 * Get data. For compatibility's sake, this returns the text of the
	 * literal (w/o surrounding quotes). 
	 * 
	 * @return the text of the literal
	 */
	@Override
    public String getLabel() {
		int i = _data.lastIndexOf("\"");
    	return _data.substring(1, i);
    }

	/*
	 * Get language tag.
	 * 
	 * @return a) the language tag if one is supplied b) null pointer, if there
	 *         is no such language tag
	 */
	public String getLanguageTag() {
		int i = _data.lastIndexOf("\"");
		String str = _data.substring(i + 1);
		
		if (!str.startsWith("@")) {
			return null;
		} else {
			return str.substring(1);
		}
	}

	/**
	 * Get data type.
	 * 
	 * @return a) the resource if one is supplied, b) null pointer, if there is
	 *         no such resource
	 */
	public Resource getDatatype() {
		int i = _data.lastIndexOf("\"");
		String str = _data.substring(i + 1);
		
		System.out.println(str);
		if (!str.startsWith("^^<")) {
			return null;
		} else {
			return new Resource(str.substring(2), true);
		}
	}

	/**
	 * Get value as a string.
	 * 
	 */
	@Override
	public String toString() {
		return _data;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		return (o instanceof Literal)
				&& ((Literal) o)._data.equals(_data);
	}

	@Override
	public int hashCode() {
		return _data.hashCode();
	}
	
	@Override
	public int compareTo(Node n) {
		return toString().compareTo(n.toString());
	}
}
