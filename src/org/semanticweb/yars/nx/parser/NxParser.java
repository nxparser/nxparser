package org.semanticweb.yars.nx.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.Unbound;
import org.semanticweb.yars.nx.Variable;
import org.semanticweb.yars.nx.util.NxUtil;

/**
 * NxParser is a non-validating N1, N2, N3, N4, Nx parser.
 * 
 * Assumes the data to be formatted according to NTRIPLES spec, including it to
 * be encoded in ASCII with Unicode characters properly escaped as in the
 * NTRIPLES spec.
 * 
 * Deviates from NTRIPLES spec in two points known so far:
 * <ul>
 * <li>Only space delimits parts of a statement, not space or tab.</li>
 * <li>Previous to the full stop at the end of a statement, there must be a
 * space.</li>
 * </ul>
 * 
 * 
 * @link http://www.w3.org/TR/rdf-testcases/#ntriples
 * @version 1.2
 * 
 * @author Aidan Hogan
 * @author Andreas Harth
 * @author Tobias Kaefer
 */
public class NxParser implements Iterator<Node[]>, Iterable<Node[]> {

	private static Logger _log = Logger.getLogger(NxParser.class.getName());

	private int _lineNo = 0;
	private String _line = null;
	private Iterator<String> _stringIt = null;
	private Node[] next = null;

	public NxParser(Reader r) {
		this(new BufferedReader(r));
	}

	public NxParser(InputStream is, Charset cs) {
		this(new BufferedReader(new InputStreamReader(is, cs)));
	}

	public NxParser(InputStream is) {
		this(new BufferedReader(new InputStreamReader(is)));
	}

	public NxParser(BufferedReader br) {
		this(stringItFromBufferedReader(br));
	}

	public NxParser(Iterable<String> iterable) {
		this(iterable.iterator());
	}

	public NxParser(Iterator<String> iterator) {
		_stringIt = iterator;
		loadNext();
	}

	public boolean hasNext() {
		return next != null;
	}

	public Node[] next(){
		if(next==null)
			throw new NoSuchElementException();
		Node[] now = next;
		loadNext();
		return now;
	}
	
	private void loadNext() {
		do{
			if (_stringIt.hasNext()){
				_line = _stringIt.next();
			} else {
				next = null;
				return;
			}
			++_lineNo;
		} while(_line==null || isEntirelyWhitespaceOrEmpty(_line));
		//iterate until we get a non-blank line
		
		try {
			next = parseNodesInternal(_line);
			//iterate until we get a valid parsed Node[]
		} catch (Exception e) {
			_log.warning("Moving on to the next line, as I couldn't parse line "
					+ _lineNo + ": " + _line);
			e.printStackTrace();
			loadNext();
		}
	}
	
	private static boolean isEntirelyWhitespaceOrEmpty(String s){
		for(char c:s.toCharArray()){
			if (!Character.isWhitespace(c))
				return false;
		}
		return true;
	}

	/**
	 * Calls remove from underlying string iterator.
	 */
	@Override
	public void remove() {
		_stringIt.remove();
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}

	public static Node[] parseNodes(String line) throws ParseException {
		try {
			return parseNodesInternal(line);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException("Exception while parsing " + line);
		}
	}

	private static Node[] parseNodesInternal(String line) throws ParseException {
		int startIndex = 0;
		int endIndex = 0;
		List<Node> nx = new LinkedList<Node>();

		while (true) {
			while (line.charAt(startIndex) == ' ') {
				// skipping spaces
				++startIndex;
			}

			if (line.charAt(startIndex) == '<') {
				// resource.
				endIndex = line.indexOf(' ', startIndex);
				nx.add(new Resource(line.substring(startIndex, endIndex), true));
			} else if (line.charAt(startIndex) == '_') {
				// bnode.
				endIndex = line.indexOf(' ', startIndex);
				nx.add(new BNode(line.substring(startIndex, endIndex), true));
			} else if (line.charAt(startIndex) == '.') {
				// statement's end.
				break;
			} else if (line.charAt(startIndex) == '"') {
				// literal.
				// telling escaped quotes within the literal from
				// literal-delimiting ones:
				endIndex = startIndex;
				do {
					endIndex = line.indexOf('\"', endIndex + 1);
				} while (line.charAt(endIndex - 1) == '\\'
						&& (((endIndex - 1 - onlyCharUntil(line, '\\',
								endIndex - 1)) % 2) == 0));
				// ^^ if the number of backslashes in front of a quote is even,
				// the found quote is the literal-delimiting one.
				while (line.charAt(endIndex) != ' ') {
					++endIndex;
				}
				nx.add(new Literal(line.substring(startIndex, endIndex), true));
			} else if (line.charAt(startIndex) == '?') {
				// variable.
				endIndex = line.indexOf(' ', startIndex);
				nx.add(new Variable(line.substring(startIndex, endIndex), true));
			} else if (line.charAt(startIndex) == Unbound.TO_STRING.charAt(0)) {
				// unbound.
				if (line.substring(startIndex,
						startIndex + Unbound.TO_STRING.length()).equals(
						Unbound.TO_STRING)) {
					nx.add(new Unbound());
					endIndex = line.indexOf(' ', startIndex);
				}
			}
			
			if (startIndex == (endIndex + 1))
				throw new ParseException("Exception at position " + endIndex
						+ " while parsing " + line);
			startIndex = endIndex + 1;
		}
		return nx.toArray(new Node[nx.size()]);
	}

	/**
	 * Looks from a given position i in a string line backwards and returns the
	 * index of the last occurence of parameter c in a row.
	 * 
	 * @param line
	 *            the string
	 * @param c
	 *            the character
	 * @param i
	 *            the starting index
	 * @return the index to be returned
	 */
	private static int onlyCharUntil(String line, char c, int i) {
		while (line.charAt(i) == c) {
			--i;
		}
		return i + 1;
	}

	/**
	 * Creates an iterator of strings from a given buffered reader.
	 * 
	 * @param br
	 *            the buffered reader
	 * @return the iterator
	 */
	private static Iterator<String> stringItFromBufferedReader(
			final BufferedReader br) {
		return new Iterator<String>() {

			boolean nextIsFetched = true;
			String next = null;

			@Override
			public boolean hasNext() {
				if (!nextIsFetched) {
					return next == null ? false : true;
				} else {
					try {
						next = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					nextIsFetched = false;
					return next == null ? false : true;
				}
			}

			@Override
			public String next() {
				if (nextIsFetched)
					try {
						next = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				nextIsFetched = true;
				if (next == null)
					throw new NoSuchElementException();
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/*
	 * 
	 * FROM HERE DOWNWARDS: LEGACY STUFF
	 */

	/**
	 * Returns the line number that has currently been processed.
	 * 
	 * @return the line number
	 */
	public int lineNumber() {
		return _lineNo;
	}

	/**
	 * @deprecated This is an iterator and therefore cannot throw exceptions at
	 *             all. Therefore parameter strict is pointless here.
	 */
	@Deprecated
	public NxParser(Reader r, boolean strict) {
		this(r);
	}

	/**
	 * @deprecated See {@link #NxParser(Reader, boolean)} and {@link #DEFAULT_PARSE_DTS}.
	 */
	@Deprecated
	public NxParser(Reader r, boolean strict, boolean parseDts) {
		this(r);
	}

	/**
	 * @deprecated See {@link #NxParser(Reader, boolean)} and {@link #DEFAULT_PARSE_DTS}.
	 */
	@Deprecated
	public NxParser(InputStream is, boolean strict, boolean parseDts) {
		this(is);
	}

	/**
	 * @deprecated See {@link #NxParser(Reader, boolean)}.
	 */
	@Deprecated
	public NxParser(InputStream is, boolean strict) {
		this(is);
	}

	/**
	 * @deprecated If you call a literal's
	 *             {@link org.semanticweb.yars.nx.Literal#getDatatype()} method,
	 *             you are presented with the data type, if there is one. So the
	 *             processing of that is to be moved.
	 */
	@Deprecated
	public static boolean DEFAULT_PARSE_DTS = false;

	// private static BNodeHandler _bnh = null;
	//
	// @Deprecated
	// public static void setBNodeHandler(BNodeHandler bnh) {
	// _bnh = bnh;
	// }
	//
	// @Deprecated
	// public static BNodeHandler getBNodeHandler() {
	// return _bnh == null? _bnh = new DummyBNodeHandler(): _bnh;
	// }
	//
	// @Deprecated
	// private static class DummyBNodeHandler implements BNodeHandler {
	// public BNode getBNode(String id) {
	// return new BNode(id);
	// }
	// }

	/**
	 * @see #DEFAULT_PARSE_DTS
	 */
	@Deprecated
	public static Node[] parseNodes(String line, boolean parseDts)
			throws ParseException {
		return parseNodes(line);
	}

	/**
	 * @see #parseLiteral(String, boolean)
	 */
	@Deprecated
	public static Literal parseLiteral(String str) throws ParseException {
		return parseLiteral(str, DEFAULT_PARSE_DTS);
	}

	/**
	 * @deprecated Doesn't do much parsing apart from checking the first char to
	 *             be a quotation mark.
	 * @see #DEFAULT_PARSE_DTS
	 */
	@Deprecated
	public static Literal parseLiteral(String str, boolean parseDTs)
			throws ParseException {
		if (str.charAt(0) != '"') {
			throw new ParseException("literal must be enclosed with \"\"");
		}
		return new Literal(str, true);
	}

	/**
	 * @deprecated Doesn't do much parsing apart from checking the first char to
	 *             be a question mark.
	 */
	@Deprecated
	public static Variable parseVariable(String str) throws ParseException {
		if (str.charAt(0) != '?') {
			throw new ParseException("variable must start with ?");
		}
		return new Variable(str, true);
	}

	/**
	 * @deprecated Doesn't do much parsing apart from checking the first and the
	 *             last char to be angle brackets.
	 */
	@Deprecated
	public static Resource parseResource(String str) throws ParseException {
		if (str.charAt(0) != '<' && str.charAt(str.length() - 1) != '>') {
			throw new ParseException("resource must be enclosed with <>");
		}
		return new Resource(str, true);
	}

	/**
	 * @deprecated Doesn't do much parsing apart from checking the first chars
	 *             to be :_.
	 */
	@Deprecated
	public static BNode parseBNode(String str) throws ParseException {
		if (str.charAt(0) != '_' || str.charAt(1) != ':') {
			throw new ParseException("bnode must start with :_");
		}
		return new BNode(str, true);
	}

	/**
	 * @see #parseNode(String, boolean)
	 */
	@Deprecated
	public static Node parseNode(String str) throws ParseException {
		return parseNode(str, DEFAULT_PARSE_DTS);
	}

	/**
	 * @deprecated Tells the type of the node according to the first char in
	 *             parameter str.
	 * @see #DEFAULT_PARSE_DTS
	 */
	@Deprecated
	public static Node parseNode(String str, boolean parseDts)
			throws ParseException {
		if (str.charAt(0) == '_') {
			// blank node
			return parseBNode(str);
		} else if (str.charAt(0) == '<') {
			return parseResource(str);
		} else if (str.charAt(0) == '"') {
			return parseLiteral(str, parseDts);
		} else if (str.charAt(0) == '?') {
			return parseVariable(str);
		} else {
			throw new ParseException("cannot parse " + str);
		}
	}

	/**
	 * Escapes strings to unicode
	 * 
	 * @deprecated Moved to {@link org.semanticweb.yars.nx.util.NxUtil}.
	 */
	@Deprecated
	public static String escapeForNx(String lit) {
		return NxUtil.escapeForNx(lit);
	}

	/**
	 * Escapes strings for markup
	 * 
	 * @deprecated Moved to {@link org.semanticweb.yars.nx.util.NxUtil}.
	 */
	@Deprecated
	public static String escapeForMarkup(String lit) {
		return NxUtil.escapeForMarkup(lit);
	}

	/**
	 * Unescape special characters in literal by removing excess backslashes
	 * 
	 * @param str
	 *            The string to escape
	 * @deprecated Moved to {@link org.semanticweb.yars.nx.util.NxUtil}.
	 */
	@Deprecated
	public static String unescape(String str) {
		return NxUtil.unescape(str, false);
	}

	/**
	 * Unescape special characters in literal by removing excess backslashes
	 * 
	 * @param str
	 *            The string to escape
	 * @param clean
	 *            If true, cleans up excess slashes
	 * @deprecated Moved to {@link org.semanticweb.yars.nx.util.NxUtil}.
	 */
	@Deprecated
	public static String unescape(String str, boolean clean) {
		return NxUtil.unescape(str, clean);
	}
	
}
