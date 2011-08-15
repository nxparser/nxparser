/*
 * Created on 08-Aug-2006
 *
 * Author Aidan Hogan
 */
package org.semanticweb.yars.nx.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.Variable;


/**
 * NxParser is a non-validating N3 or N4 parser
 * Optionally unescapes characters in literals
 * 
 * @author Aidan Hogan
 * @deprecated use NxParser
 */
public class NqParser implements Iterator<Node[]> {
	private boolean _strict;
	private BNodeHandler _bnh;
	private Node[] _current;
	private BufferedReader _br;
	private int _lineNo;

	/**
	 * Constructor.
	 * 
	 * Defualts to strict off
	 */
	public NqParser(Reader r) throws ParseException, IOException {
		this(r, false);

//		throw new RuntimeException("---------------------------------------------  BUUUUH");
	}
	
	/**
	 * Constructor.
	 * 
	 * @param strict
	 * either true, then throws a ParseException on any error, or false,
	 * then just print out warning on errors
	 */
	public NqParser(Reader r, boolean strict) throws IOException {
		_strict = strict;

		_lineNo = 0;

		_bnh = new DummyBNodeHandler();

		_br = new BufferedReader(r);

		String line;
		while ((line = _br.readLine()) != null) {
			_lineNo++;
			try {
				Node[] n = parseNodes(line);
				if (n != null) {
					_current = new Node[n.length];
					System.arraycopy(n, 0, _current, 0, n.length);
					break;
				}
			} catch (ParseException pe) {
				if (_strict == true)
					throw new RuntimeException(new ParseException(pe));
				else
					System.err.println("warning on line " + _lineNo + " " + line + " : " + pe.getMessage());
			} catch (RuntimeException ex) {
				if (_strict == true)
					throw ex;
				else
					System.err.println("warning in NqParser constructor " + _lineNo + " " + line + " " + ex.getMessage());
			}
		}
	}

	/**
	 * Constructor. Deprecated. 
	 * 
	 * @deprecated Literals can now self escape
	 * @param unescapeLiterals
	 * deprecated argument
	 * @param strict
	 * either true, then throws a ParseException on any error, or false,
	 * then just print out warning on errors
	 */
	public NqParser(Reader r, boolean unescapeLiterals, boolean strict) throws IOException {
		this(r, strict);	
	}

	public void setBNodeHandler(BNodeHandler bnh) {
		_bnh = bnh;
	}

	public BNodeHandler getBNodeHandler() {
		return _bnh;
	}

	public boolean hasNext() {
		if (_current != null)
			return true;
		return false;
	}

	public void remove() {
		throw new UnsupportedOperationException("remove not supported");
	}

	public Node[] next() {

		Node[] result = new Node[_current.length];

		System.arraycopy(_current, 0, result, 0, _current.length);

		_current = null;

		String line;
		try {
			while ((line = _br.readLine()) != null) {
				_lineNo++;
				try {
					Node[] n = parseNodes(line);
					if (n != null) {
						_current = new Node[n.length];
						System.arraycopy(n, 0, _current, 0, n.length);
						break;
					}
				} catch (ParseException pe) {
					System.err.println("warning in NqParser.next on line " + _lineNo + " " + line + " : " +  pe.getMessage());
					continue;
				} catch (RuntimeException e) {
					System.err.println("warning on line " + _lineNo + " " + line + " : " +  e.getMessage());
					continue;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private Node[] parseNodes(String line) throws ParseException {
		ArrayList<Node> n = new ArrayList<Node>();
		int firstIndex = 0;
		int lastIndex = 0;
		int pos = 0;

		String value = line.trim();

		while (true) {
			value = value.substring(firstIndex).trim();
			if (value.equals(".") || value.equals("\n"))
				break;

			if (value.startsWith("_")) {
				// blank node
				lastIndex = value.indexOf(" ");
				n.add(parseBNode(value, 2, lastIndex));
			} else if (value.startsWith("<")) {
				// resource
				lastIndex = value.indexOf("> ");
				n.add(new Resource(value.substring(0, lastIndex + 1),true));
			} else if (value.startsWith("\"")) {
				// literal
				lastIndex = value.indexOf('"', 2);
				// check if quote is escaped (and if the escaping slash is itself not escaped)
				
				while(true){		
					boolean escaped = false;
					int temp = lastIndex-1;
					
					while (temp>0 && value.charAt(temp)=='\\'){
						if(escaped)
							escaped = false;
						else escaped = true;
						temp--;
					}
					
					if(!escaped)
						break;
					
					lastIndex++; 
					if(lastIndex==value.length())
						throw new ParseException("Cannot find the (unescaped) \" end of literal");
					
					lastIndex = value.indexOf('"', lastIndex);
					if(lastIndex==-1)
						throw new ParseException("Cannot find the (unescaped) \" end of literal");
				}
				
				n.add(parseLiteral(value, 1, lastIndex));
				// problem here: literal can extend to a bit more than just the ",
				// also take into account lang and datatype strings
				if (value.charAt(lastIndex+1) == '@') {
					// find end of literal/lang tag
					lastIndex = value.indexOf(' ', lastIndex+1) - 1;
				} else if (value.charAt(lastIndex+1) == '^') {
					lastIndex = value.indexOf('>', lastIndex+1);
				}
				//System.out.println("literal parsing + " + n[i] + " blasdf");
			} else if(value.startsWith("?")) {
				// variable
				lastIndex = value.indexOf(" ");
				n.add(parseVariable(value,1,lastIndex));
			}
			else {
				throw new ParseException("cannot parse " + pos + "th element: " + value);
			}

			firstIndex = lastIndex+1;
			// just for error reporting
			pos++;
		}

		//System.out.println(n);
		//System.out.println(n.size());

		return n.toArray(new Node[n.size()]);
	}

	private static Resource parseResource(String value, int fromIndex, int toIndex) throws ParseException {
		if(toIndex<=-1){
			throw new ParseException("Error parsing resource subject from triple:\n"+value+"\nExpected ending '> ' not found.\n");
		}
		String data = value.substring(fromIndex, toIndex);

		//System.out.println(data);

		return new Resource(data);
	}

	private static Variable parseVariable(String value, int fromIndex, int toIndex) throws ParseException {
		if(toIndex<=-1){
			throw new ParseException("Error parsing variable from segment:\n"+value+"\nExpected ending ' ' not found\n");
		}
		String data = value.substring(fromIndex, toIndex);

		return new Variable(data);
	}

	private BNode parseBNode(String value, int fromIndex, int toIndex) throws ParseException {
		if(toIndex<=-1){
			throw new ParseException("Error parsing bnode subject from triple:\n"+value+"\nExpected ending ' ' not found.\n");
		}
		String data = value.substring(fromIndex,toIndex);

		//System.out.println(data);

		return _bnh.getBNode(data);
	}

	public static Literal parseLiteral(String str) throws ParseException {
		if (str.charAt(0) != '"') {
			throw new ParseException("literal must be enclosed with \"\"");
		}
		return parseLiteral(str, 1, str.length());
	}
	
	public static Variable parseVariable(String str) throws ParseException {
		if (str.charAt(0) != '?') {
			throw new ParseException("variable must start with ?");
		}
		return parseVariable(str, 1, str.length());
	}
	
	public static Resource parseResource(String str) throws ParseException {
		if (str.charAt(0) != '<' && str.charAt(str.length()-1) != '>') {
			throw new ParseException("resource must be enclosed with <>");
		}
		return new Resource(str,true);
//		return parseResource(str, 1, str.length()-1);
	}
	
	public static BNode parseBNode(String str) throws ParseException {
		if (str.charAt(0) != '_' || str.charAt(1) != ':') {
			throw new ParseException("bnode must be enclosed with <>");
		}
		
		String data = str.substring(2,str.length());

		//System.out.println(data);

		return new BNode(data);
	}
	
	public static Node parseNode(String str) throws ParseException {
		if (str.charAt(0) == '_') {
			// blank node
			return parseBNode(str);
		} else if (str.charAt(0) == '<') {
			return parseResource(str);
		} else if (str.charAt(0) == '"') {
			return parseLiteral(str);
		} else if(str.charAt(0) == '?') {
			return parseVariable(str);
		}
		else {
			throw new ParseException("cannot parse " + str);
		}
	}

	/**
	 * 
	 * @param value
	 * @param fromIndex where literal starts (i.e. first ")
	 * @param toIndex where literal ends (i.e. last ")
	 * @param unescape
	 * @return
	 * @throws ParseException
	 */
	private static Literal parseLiteral(String value, int fromIndex, int toIndex) throws ParseException {
		if(toIndex<=-1){
			throw new ParseException("Error parsing object literal from triple :\n"+value+"\nCannot find closing object delimiter '\"'.\n");
		}

		int endquoteIndex = value.lastIndexOf('"');

		String literal = value.substring(fromIndex,endquoteIndex);

		//find datatype or language if any
		String literalProp;
		endquoteIndex++;
		try{
			literalProp  = value.substring(endquoteIndex);
		}catch(StringIndexOutOfBoundsException e){
			throw new ParseException("Error checking for datatype/lang property of object literal from triple :\n"+value+"\nNo closing '.' in triple?\n");
		}

		literalProp.trim();

		//get language approach
		//1. Get string cut off after closing '"' literal char
		//2. Trim exterior whitespace
		//3. If result begins with '@' ...
		//3. Parse lang as string between '@' and ' '
		//will not work if
		//1. No space after '@lang' and before triple ending '.'
		//2. Space occurs anywhere in '@lang' expression
		if(literalProp.startsWith("@"))
		{
			int end = literalProp.indexOf(' ');
			if (end < 0)
				end = literalProp.length();

			//System.err.println(literalProp);
			String lang = literalProp.substring(1, end).trim();
			//System.out.println("------------lang" + lang);
			return new Literal(literal,lang);
		}

		//get datatype approach
		//1. Get string cut off after closing '"' literal char
		//2. Trim exterior whitespace
		//3. If result begins with '^^' ...
		//3. Parse datatype uri as string between '^^' and ' '
		//will not work if
		//1. No space after '^^datatype_uri' and before triple ending '.'
		//2. Space occurs anywhere in '^^datatype_uri' expression 
		else if(literalProp.startsWith("^^"))
		{
			toIndex = literalProp.indexOf("> ");
			if(toIndex <= -1){
				if(literalProp.charAt(literalProp.length()-1) == '>'){
					toIndex = literalProp.length()-1;
				}
				else
					throw new ParseException("Error checking for datatype property of object literal from triple :\n"+value+"\nNo closing > after datatype property\n");
			}
			Resource dt = new Resource(literalProp.substring(3,toIndex));

			return new Literal(literal,dt);
		}

		else
			return new Literal(literal,(String)null);
	}
	
	
	private class DummyBNodeHandler implements BNodeHandler {
		public BNode getBNode(String id) {
			return new BNode(id);
		}
	}
}

