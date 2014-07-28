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

	public Iterator<Node[]> parse(Reader r) {
		return parse(new BufferedReader(r));
	}

	public Iterator<Node[]> parse(InputStream is, Charset cs) {
		return parse(new BufferedReader(new InputStreamReader(is, cs)));
	}

	public Iterator<Node[]> parse(InputStream is) {
		return parse(new BufferedReader(new InputStreamReader(is)));
	}

	public Iterator<Node[]> parse(BufferedReader br) {
		return parse(stringItFromBufferedReader(br));
	}

	public Iterator<Node[]> parse(Iterable<String> iterable) {
		return parse(iterable.iterator());
	}

	public Iterator<Node[]> parse(Iterator<String> iterator) {
		_stringIt = iterator;
		loadNext();
		return this;
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
		next = null;
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
		} catch (Exception e) {
			_log.warning("Moving on to the next line, as I couldn't parse line "
					+ _lineNo + ": " + _line);
			e.printStackTrace();
			//invalid: skip and try again
			loadNext();
		}
		
		if(next.length==0)//valid but empty: skip and try again
			loadNext();
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

	public static Node[] parseNodes(final String line) throws ParseException {
		try {
			return parseNodesInternal(line);
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}

	/**
	 * Parses line and returns a Node[] contained within that line.
	 * May return an empty Node[] if the line is valid N-Triples
	 * but contains no nodes (e.g., a blank or comment line).
	 * 
	 * @param line
	 * @return
	 * @throws ParseException
	 */
	private static Node[] parseNodesInternal(final String line) throws ParseException {
		int startIndex = 0;
		int endIndex = 0;
		List<Node> nx = new LinkedList<Node>();
		
		if(line.isEmpty()) return new Node[0];

		//instead of checking for individual IndexOutOfBoundExceptions,
		//they are allowed to be thrown and caught in parseNodes()
		
		while (true) {
			while (Character.isWhitespace(line.charAt(startIndex))) {
				// skipping spaces
				++startIndex;
				++endIndex;
				
				if(startIndex==line.length()){
					if(nx.isEmpty()){
						return new Node[0];
					} else{
						throw new ParseException("Could not find closing '.' bracket for line "+line);
					}
				}
			}

			if (line.charAt(startIndex) == '<') {
				// resource.
				endIndex = line.indexOf("> ", startIndex)+1;
				if(endIndex==0) throw new ParseException("Could not find closing '>' bracket for resource starting at char "+startIndex+" while parsing line "+line);
				nx.add(new Resource(line.substring(startIndex, endIndex), true));
			} else if (line.charAt(startIndex) == '_') {
				// bnode.
				endIndex = line.indexOf(' ', startIndex);
				nx.add(new BNode(line.substring(startIndex, endIndex), true));
			} else if (line.charAt(startIndex) == '.') {
				// statement's end.
				if(nx.isEmpty()){
					throw new ParseException("Exception at position " + startIndex+ " while parsing: '" + line +"'");
				}
				for(int i=startIndex+1; i<line.length(); i++){
					if(!Character.isWhitespace(line.charAt(i))){
						throw new ParseException("Exception at position " + i + " while parsing: '" + line +"'");
					}
				}
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
			} else if(line.charAt(startIndex) == '#' && nx.isEmpty()){
				// comment line.
				return new Node[0];
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
					endIndex = startIndex + Unbound.TO_STRING.length();
					if(endIndex >= line.length() || line.charAt(endIndex)!=' '){
						throw new ParseException("Exception at position " + startIndex+ " while parsing: '" + line +"'");
					}
				} else{
					throw new ParseException("Exception at position " + endIndex+ " while parsing: '" + line +"'");
				}
			} else{
				throw new ParseException("Exception at position " + endIndex+ " while parsing: '" + line +"'");
			}

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
						next = null;
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
}
