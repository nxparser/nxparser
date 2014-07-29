package org.semanticweb.yars.nx;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.parser.ParseException;

/**
 * An RDF literal of any data type
 * 
 * Need to do newline escaping for (string) literals otherwise it's not valid
 * RDF any more.
 * 
 * @author Andreas Harth
 * @author Tobias Kaefer
 */
public class Literal implements Node {
	private static Logger _log = Logger.getLogger(Literal.class.getName());

	// the entire string in N3 syntax, including "", @ or ^^
	private final String _data;

	private static final Pattern PATTERN = Pattern.compile("(?:\"(.*)\")(?:@([a-zA-Z]+(?:-[a-zA-Z0-9]+)*)|\\^\\^(<\\S+>))?");

	// version number for serialization
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor (not in N3 syntax).
	 * 
	 * @param data - the string representation of the (simple) literal
	 */
	public Literal(String data) {
		this(data, null, null, false);
	}
	
	/**
	 * Constructor, possibly in in N3 syntax.
	 * 
	 * @param data
	 * @param isN3 - true if data is in N3 (with "" and ^^<> or @
	 */
	public Literal(String data, boolean isN3) {
		this(data, null, null, isN3);
	}

	/**
	 * Constructor (not in N3 syntax)
	 * 
	 * @param data
	 *            the string representation of the (simple) literal
	 * @param lang
	 *            the language tag for the literal. Nx spec demands it to be
	 *            lowercase.
	 */
	public Literal(String data, String lang) {
		this(data, lang, null, false);
	}

	/**
	 * Constructor (not in N3)
	 * 
	 * @param data
	 *            the escaped string representation of the (simple) literal
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
	 *            the escaped string representation of the (simple) literal
	 * @param lang
	 *            the language tag for the literal. Nx spec demands it to be
	 *            lowercase.
	 * @param dt
	 *            the datatype Resource of the Literal
	 */
	public Literal(String data, String lang, Resource dt) {
		this(data, lang, dt, false);
	}

	/**
	 * Construct a literal from consituent elements (which are not in N3).
	 * 
	 * @param data
	 * @param lang
	 * @param dt
	 */
	private Literal(String data, String lang, Resource dt, boolean isN3) {
		if (isN3) {
			_data = data;
		} else {
			if (data.equals("") || data.charAt(0) != '\"' || data.charAt(data.length() - 1) != '\"') {
				_log.log(Level.FINE, "Adding quotes for Literal {}",  data);
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
	 * literal (w/o surrounding quotes). (This method will now avoid writing a
	 * null.)
	 * 
	 * @return a) the text of the literal, b) the full N3 form of the literal if
	 *         there is a problem.
	 * @throws ParseException 
	 */
	public String getLiteralString() throws ParseException {
		Matcher m = PATTERN.matcher(_data);
		if (!m.matches()) {
			throw new ParseException("The parsing regex pattern didn't match for " + _data);
		}
		
		return "\"" + m.group(1) + "\"";
	}

	/*
	 * Get language tag.
	 * 
	 * @return a) the language tag if one is supplied b) null pointer, if there
	 *         is no such language tag, c) null pointer, if there is something
	 *         wrong with the literal-backing string
	 * @throws ParseException 
	 */
	public String getLanguageTag() throws ParseException {
		Matcher m = PATTERN.matcher(_data);
		if (!m.matches()) {
			throw new ParseException("The parsing regex pattern didn't match for " + _data);
		}

		if (m.group(2) != null) {
			return m.group(2);
		} else {
			return null;
		}
	}

	/**
	 * Get data type.
	 * 
	 * @return a) the resource if one is supplied, b) null pointer, if there is
	 *         no such resource, c) null pointer, if there is something wrong
	 *         with the literal-backing string
	 * @throws ParseException 
	 */
	public Resource getDatatype() throws ParseException {
		Matcher m = PATTERN.matcher(_data);
		if (!m.matches()) {
			throw new ParseException("The parsing regex pattern didn't match for " + _data);
		}

		if (m.group(3) != null) {
			return new Resource(m.group(3), true);
		} else {
			return null;
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
}
