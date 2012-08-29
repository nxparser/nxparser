package org.semanticweb.yars.nx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Literal implements Node, Serializable {

	private static Logger _log = Logger.getLogger(Literal.class.getName());

	// data in string representation
	protected String _data = null;
	// language identifier
	protected String _lang = null;
	// datatype uri
	protected Resource _dt = null;
	// the whole string including @ or ^^ etc.
	protected String _wholeString = null;

	@Deprecated
	public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
	@Deprecated
	public static final Resource STRING = new Resource(XSD + "string");
	@Deprecated
	public static final Resource BOOLEAN = new Resource(XSD + "boolean");
	@Deprecated
	public static final Resource FLOAT = new Resource(XSD + "float");
	@Deprecated
	public static final Resource DECIMAL = new Resource(XSD + "decimal");
	@Deprecated
	public static final Resource DOUBLE = new Resource(XSD + "double");
	@Deprecated
	public static final Resource DATETIME = new Resource(XSD + "dateTime");

	private static final Pattern PATTERN = Pattern
	        .compile("(?:\"(.*)\")(?:@([a-zA-Z]+(?:-[a-zA-Z0-9]+)*)|\\^\\^(<\\S+>))?"); 

	// version number for serialization
	private static final long serialVersionUID = 8911891129019471564L;

	
	
	/**
	 * Constructor.
	 * 
	 * @param data
	 *            the escaped string representation of the (simple) literal
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
	 *            the language tag for the literal
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
	 *            the language tag for the literal
	 * @param dt
	 *            the datatype Resource of the Literal
	 */
	public Literal(String data, String lang, Resource dt) {
		this(data, lang, dt, false);
	}

	public Literal(String data, String lang, Resource dt, boolean isN3) {
		if (!isN3) {
			if (data.equals("") || data.charAt(0) != '\"'
					|| data.charAt(data.length() - 1) != '\"') {
				_log.fine("String for Literal ("
						+ data
						+ ") had no surrounding quotes. Adding some and proceeding...");
				data = '\"' + data + '\"';
			}
			if (lang != null && dt != null)
				throw new IllegalArgumentException(
						"In Nx, only one of language and datatype can be given.");
			_wholeString = data
					+ (lang == null ? (dt == null) ? "" : ("^^" + dt.toN3())
							: ("@" + lang));
			if (dt != null)
				_dt = dt;
		} else
			_wholeString = data;
	}

	public Literal(String data, boolean isN3) {
		this(data, null, null, isN3);
	}

	/**
	 * Get escaped data. For compatibility's sake, this returns the text of the
	 * literal (w/o surrounding quotes).
	 * 
	 * @return a) the text of the literal, b) null pointer if there is something
	 *         wrong with the literal backing string.
	 */
	public String getData() {
		if (_data == null) {
			Matcher m  = PATTERN.matcher(_wholeString);
			if (m.matches())
				_data = m.group(1);
			else
				_log.warning("Something wrong with the literal-backing string. The parsing regex pattern didn't match. Check the string for correct N3 syntax. The malicious string is: "
						+ _wholeString);
		}
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
		if (_lang == null) {
			Matcher m  = PATTERN.matcher(_wholeString);
			if (!m.matches())
				_log.warning("The parsing regex pattern didn't match, so no language tag is returned. Check the Literal for proper N3 syntax. The malicious Literal was: "
						+ _wholeString);
			else
				_lang = m.group(2);
		}
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
		if (_dt == null) {
			Matcher m  = PATTERN.matcher(_wholeString);
			if (m.matches()) {
				if (m.group(3) == null)
					return null;
				_dt = new Resource(m.group(3), true);
			} else {
				_log.warning("Something wrong with the Resource. Its String: "
						+ _wholeString
						+ " didn't match the parsing regex pattern. Probably it's no proper N3.");
			}
		}
		return _dt;
	}

//	 /**
//	 * Get object representing datatype-value of literal.
//	 *
//	 * @return datatype value or null if (i) unsupported datatype; (ii) plain literal (w/wo/ lang tag)
//	 * @throws DatatypeParseException if supported datatype with bad syntax
//	 */
//	 public Datatype<? extends Object> getDatatypeObject() throws DatatypeParseException {
//		 return DatatypeFactory.getDatatype(getUnescapedData(), getDatatype());
//	 }
	
	/**
	 * Get value as a string.
	 * 
	 */
	public String toString() {
		return NxUtil.unescape(getData());
	}

	/**
	 * Print N3 representation.
	 * 
	 */
	public String toN3() {
		return _wholeString;
	}

	@Override
	public int compareTo(Object o) {
		if (o == this)
			return 0;

		if (o instanceof Literal) {
			Literal l = (Literal) o;
			return this._wholeString.compareTo(l._wholeString);
		} else if (o instanceof Resource) {
			return Integer.MIN_VALUE / 4;
		} else if (o instanceof BNode) {
			return Integer.MIN_VALUE / 3;
		} else if (o instanceof Unbound) {
			return Integer.MIN_VALUE / 2;
		} else if (o instanceof Variable) {
			return Integer.MIN_VALUE;
		}

		throw new ClassCastException("parameter is not of type Literal but "
				+ o.getClass().getName());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		return 
				(o != null)
				&& (o instanceof Literal)
				&& ((Literal) o)._wholeString.equals(this._wholeString);
	}

	@Override
	public int hashCode() {
		return _wholeString.hashCode();
	}

	protected int getHashCode() {
		return hashCode();
	}

	/**
	 * Escapes strings to unicode.
	 * 
	 * @deprecated Use
	 *             {@link org.semanticweb.yars.nx.util.NxUtil#escapeForNx(String)}
	 */
	public static String escapeForNx(String lit) {
		return NxUtil.escapeForNx(lit);
	}

	/**
	 * Escapes strings for markup.
	 * 
	 * @deprecated Use
	 *             {@link org.semanticweb.yars.nx.util.NxUtil#escapeForMarkup(String)}
	 */
	public static String escapeForMarkup(String lit) {
		return NxUtil.escapeForMarkup(lit);
	}

	/**
	 * Unescape special characters in literal by removing excess backslashes.
	 * 
	 * @param str
	 *            The string to escape
	 * @deprecated Use
	 *             {@link org.semanticweb.yars.nx.util.NxUtil#unescape(String)}
	 */
	public static String unescape(String str) {
		return NxUtil.unescape(str);
	}

	/**
	 * Override readObject for backwards compatability and storing hashcode.
	 */
	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}
}
