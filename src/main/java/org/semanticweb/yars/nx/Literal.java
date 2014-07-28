package org.semanticweb.yars.nx;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.nx.util.NxUtil;

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

	// data in string representation
	String _data;
	// language identifier
	String _lang;
	// datatype uri
	Resource _dt;
	// the whole string including @ or ^^ etc.
	private transient String _wholeString;

	private static final Pattern PATTERN = Pattern
			.compile("(?:\"(.*)\")(?:@([a-zA-Z]+(?:-[a-zA-Z0-9]+)*)|\\^\\^(<\\S+>))?");

	// version number for serialization
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param data - the escaped string representation of the (simple) literal
	 */
	public Literal(String data) {
		this(data, null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param data
	 *            the escaped string representation of the (simple) literal
	 * @param lang
	 *            the language tag for the literal. Nx spec demands it to be
	 *            lowercase.
	 */
	public Literal(String data, String lang) {
		this(data, lang, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param data
	 *            the escaped string representation of the (simple) literal
	 * @param dt
	 *            the datatype Resource of the Literal
	 */
	public Literal(String data, Resource dt) {
		this(data, null, dt);
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

	public Literal(String data, String lang, Resource dt, boolean isN3) {
		if (!isN3) {
			if (data.equals("") || data.charAt(0) != '\"' || data.charAt(data.length() - 1) != '\"') {
				_log.log(Level.FINE, "Adding quotes for Literal {}",  data);
				data = '\"' + data + '\"';
			}

			if ((lang != null && !"".equals(lang)) && dt != null) {
				throw new IllegalArgumentException("Specify only one of language and datatype.");
			}

			_wholeString = (data + ((lang == null || "".equals(lang)) ? (dt == null) ? ""
					: ("^^" + dt.toN3())
					: ("@" + lang)));
			
			_wholeString = _wholeString.intern();

			if (dt != null) {
				_dt = dt;
			}
		} else {
			_wholeString = data.intern();

			try {
				parse();
			} catch (ParseException e) {
				_log.log(Level.INFO, "The parsing regex pattern didn't match for {}", _wholeString);
				return;
			}
			_data = getData().intern();
			_dt = getDatatype();
			_lang = getLanguageTag().intern();
		}
	}

	public Literal(String data, boolean isN3) {
		this(data, null, null, isN3);
	}
	
	void parse() throws ParseException {
		Matcher m = PATTERN.matcher(_wholeString);
		if (m.matches()) {
			_data = m.group(1);
			if (m.group(2) != null) {
				_lang = m.group(2);
			} else {
				_lang = null;
			}
			if (m.group(3) != null) {
				_dt = new Resource(m.group(3), true);
			} else {
				_dt = null;
			}
		} else {
			throw new ParseException("The parsing regex pattern didn't match for " + _wholeString);
		}
	}

	/**
	 * Get escaped data. For compatibility's sake, this returns the text of the
	 * literal (w/o surrounding quotes). (This method will now avoid writing a
	 * null.)
	 * 
	 * @return a) the text of the literal, b) the full N3 form of the literal if
	 *         there is a problem.
	 */
	public String getData() {
		return _data;
	}

	/**
	 * Get markup escaped data.
	 * 
	 * @return String data
	 */
	public String getMarkupEscapedData() {
		return NxUtil.escapeForMarkup(getData());
	}

	/**
	 * Get unescaped data.
	 * 
	 * @return String data
	 */
	public String getUnescapedData() {
		return NxUtil.unescape(getData());
	}

	/**
	 * Get language tag.
	 * 
	 * @return a) the language tag if one is supplied b) null pointer, if there
	 *         is no such language tag, c) null pointer, if there is something
	 *         wrong with the literal-backing string
	 */
	public String getLanguageTag() {
		return _lang;
	}

	/**
	 * Check whether it's a constant (literals are always).
	 * 
	 */
	public boolean isConstant() {
		return true;
	}

	/**
	 * Get data type.
	 * 
	 * @return a) the resource if one is supplied, b) null pointer, if there is
	 *         no such resource, c) null pointer, if there is something wrong
	 *         with the literal-backing string
	 */
	public Resource getDatatype() {
		return _dt;
	}

	/**
	 * Get value as a string.
	 * 
	 */
	@Override
	public String toString() {
		return NxUtil.unescape(getData());
	}

	/**
	 * Print N3 representation.
	 * 
	 */
	@Override
	public String toN3() {
		return _wholeString;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		return (o instanceof Literal)
				&& ((Literal) o)._wholeString.equals(_wholeString);
	}

	@Override
	public int hashCode() {
		return _wholeString.hashCode();
	}
}
