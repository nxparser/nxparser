/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.yars.turtle;

import java.math.BigInteger;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.XSD;
import org.semanticweb.yars.nx.parser.Callback;

/** Base class parsers, mainly SPARQL related */ 
public class TurtleParserBase {
	final static Logger _log = Logger.getLogger(TurtleParserBase.class.getName());

	int _bnodeid = 0;

	boolean strictTurtle = true;

    // NodeConst
    protected final Literal XSD_TRUE       = new Literal("true", XSD.BOOLEAN);
    protected final Literal XSD_FALSE      = new Literal("false", XSD.BOOLEAN);
    
    protected final Node nRDFtype       = RDF.TYPE;

    protected final Node nRDFnil        = RDF.NIL;
    protected final Node nRDFfirst      = RDF.FIRST;
    protected final Node nRDFrest       = RDF.REST;
    
    protected final Node nRDFsubject    = RDF.SUBJECT;
    protected final Node nRDFpredicate  = RDF.PREDICATE;
    protected final Node nRDFobject     = RDF.OBJECT;
    
    // This is the map used allocate blank node labels during parsing.
    // 1/ It is different between CONSTRUCT and the query pattern
    // 2/ Each BasicGraphPattern is a scope for blank node labels so each
    //    BGP causes the map to be cleared at the start of the BGP
        
    //LabelToNodeMap listLabelMap = new LabelToNodeMap(true, new VarAlloc("L")) ;
    // ----
    Callback _callback;
    
    public TurtleParserBase() { _prologue = new Prologue(); }

    protected Prologue _prologue ;
    public void setPrologue(Prologue prologue) { _prologue = prologue ; }
    public Prologue getPrologue() { return _prologue ; }

    protected Literal createLiteralInteger(String lexicalForm)
    {
        return new Literal(lexicalForm, XSD.INTEGER) ;
    }
    
    protected Literal createLiteralDouble(String lexicalForm)
    {
        return new Literal(lexicalForm, XSD.DOUBLE) ;
    }
    
    protected Literal createLiteralDecimal(String lexicalForm)
    {
        return new Literal(lexicalForm, XSD.DECIMAL) ;
    }

    protected Resource createResource(String s) {
    	if (s.equals("<>")) {
    		return new Resource(_prologue.getBaseURI().toString());
    	}

    	URI u = URI.create(s.substring(1, s.length()-1));
    	if (!u.isAbsolute()) {
    		_log.log(Level.FINE, "Resolving {0} relative to {1}", new Object[] { u, _prologue.getBaseURI() } );
    		u = _prologue.getBaseURI().resolve(u);
    	}

    	return new Resource(u.toString());
    }

    protected Node stripSign(Node node)
    {
        if ( ! (node instanceof Literal) ) return node ;

//        String lex = node.getLiteralLexicalForm() ;
//        String lang = node.getLiteralLanguage() ;
//        RDFDatatype dt = node.getLiteralDatatype() ;
//        
//        if ( ! lex.startsWith("-") && ! lex.startsWith("+") )
//            throw new ARQInternalErrorException("Literal does not start with a sign: "+lex) ;
//        
//        lex = lex.substring(1) ;
//        return NodeFactory.createLiteral(lex, lang, dt) ;

        return node;
    }
        
    protected long integerValue(String s) throws TurtleParseException
    {
        try {
            if ( s.startsWith("+") )
                s = s.substring(1) ;
            if ( s.startsWith("0x") )
            {
                // Hex
                s = s.substring(2) ;
                return Long.parseLong(s, 16) ;
            }
            return Long.parseLong(s) ;
        } catch (NumberFormatException ex)
        {
            try {
                // Possible too large for a long.
                BigInteger integer = new BigInteger(s) ;
                throw new TurtleParseException("Number '"+s+"' is a valid number but can't not be stored in a long") ;
            } catch (NumberFormatException ex2) {}
            throw new TurtleParseException(ex, -1, -1) ;
        }
    }
    
    protected double doubleValue(String s)
    {
        if ( s.startsWith("+") )
            s = s.substring(1) ;
        double valDouble = Double.parseDouble(s) ;
        return valDouble ; 
    }
    
    /** Remove first and last characters (e.g. ' or "") from a string */
    protected static String stripQuotes(String s)
    {
        return s.substring(1,s.length()-1)  ;
    }
    
    /** Remove first 3 and last 3 characters (e.g. ''' or """) from a string */ 
    protected static String stripQuotes3(String s)
    {
        return s.substring(3,s.length()-3)  ;
    }

    /** remove the first n charcacters from the string*/ 
    public static String stripChars(String s, int n)
    {
        return s.substring(n, s.length())  ;
    }
        
//    protected Variable createVariable(String s, int line, int column)
//    {
//        s = s.substring(1) ; // Drop the marker
//        
//        // This is done by the parser input stream nowadays.
//        //s = unescapeCodePoint(s, line, column) ;
//        // Check \ u did not put in any illegals. 
//        return new Variable(s) ;
//    }

    // ---- IRIs and Nodes
    // See RiotLib re bNode IRIs.
    // Merge sometime.
    
//    public static final String ParserLoggerName = "SPARQL" ;
//    private static Logger parserLog = LoggerFactory.getLogger(ParserLoggerName) ;
//    private static ErrorHandler errorHandler = ErrorHandlerFactory.errorHandlerStd(parserLog) ;

    protected String resolvePName(String qname, int line, int column) throws TurtleParseException {
        // It's legal.
        int idx = qname.indexOf(':') ;
        
        // -- Escapes in local name
        String prefix = qname.substring(0, idx) ;
        String local = qname.substring(idx+1) ;
        local = unescapePName(local, line, column) ;
        
        String s = _prologue.expandPrefix(prefix) ;
        if ( s == null ) {
            throw new TurtleParseException("Unresolved prefix: "+qname, line, column) ;
        }
        return s+local ;
    }
    
	public BNode getBNode(String id) {
		return new BNode(id);
	}

	// -------- Basic Graph Patterns and Blank Node label scopes
    
    // A BasicGraphPattern is any sequence of TripleBlocks, separated by filters,
    // but not by other graph patterns. 
     
    protected void startTriplesBlock()
    { }
    
    protected void endTriplesBlock()
    { } 
    
    // --------
    
    // BNode from a list
//    protected Node createListNode()
//    { return listLabelMap.allocNode() ; }
    
    protected Node createListNode(int line, int column) { return createBNode() ; }

    // Unlabelled bNode.
	public BNode createBNode() {
		String s = _prologue.getBaseURI().toString();

		return BNode.createBNode(s, "b"+_bnodeid++);
	}

    // Labelled bNode.
    protected Node createBNode(String label, int line, int column) {
    	String s = _prologue.getBaseURI().toString();
    	
    	return BNode.createBNode(s, label);
    }
    
    protected String fixupPrefix(String prefix, int line, int column)
    {
        // \ u processing!
        if ( prefix.endsWith(":") )
            prefix = prefix.substring(0, prefix.length()-1) ;
        return prefix ; 
    }

    protected void setPrefix(int line, int column, String prefix, String uri) {
    	_prologue.setPrefix(prefix, uri);
    }

    protected void setBase(String base, int line, int column) {
    	_prologue.setBaseURI(new Resource(base.substring(1, base.length()-1)));
    }

    protected void emitTriple(int line, int column, Nodes triple) {
    	_callback.processStatement(triple.getNodeArray());
    }

    // Utilities to remove escapes in strings.
    
    public static String unescapeStr(String s) throws TurtleParseException
    { return unescape(s, '\\', false, 1, 1) ; }

//    public static String unescapeCodePoint(String s)
//    { return unescape(s, '\\', true, 1, 1) ; }
//
//    protected String unescapeCodePoint(String s, int line, int column)
//    { return unescape(s, '\\', true, line, column) ; }

    
    public static String unescapeStr(String s, int line, int column) throws TurtleParseException
    { return unescape(s, '\\', false, line, column) ; }
    
    // Worker function
    public static String unescape(String s, char escape, boolean pointCodeOnly, int line, int column) throws TurtleParseException
    {
        int i = s.indexOf(escape) ;
        
        if ( i == -1 )
            return s ;
        
        // Dump the initial part straight into the string buffer
        StringBuilder sb = new StringBuilder(s.substring(0,i)) ;
        
        for ( ; i < s.length() ; i++ )
        {
            char ch = s.charAt(i) ;
            // Keep line and column numbers.
            switch (ch)
            {
                case '\n': 
                case '\r':
                    line++ ;
                    column = 1 ;
                    break ;
                default:
                    column++ ;
                    break ;
            }

            if ( ch != escape )
            {
                sb.append(ch) ;
                continue ;
            }
                
            // Escape
            if ( i >= s.length()-1 )
            	throw new TurtleParseException("Illegal escape at end of string", line, column) ;
            char ch2 = s.charAt(i+1) ;
            column = column+1 ;
            i = i + 1 ;
            
            // \\u and \\U
            if ( ch2 == 'u' )
            {
                // i points to the \ so i+6 is next character
                if ( i+4 >= s.length() )
                	throw new TurtleParseException("\\u escape too short", line, column) ;
                int x = hex(s, i+1, 4, line, column) ;
                sb.append((char)x) ;
                // Jump 1 2 3 4 -- already skipped \ and u
                i = i+4 ;
                column = column+4 ;
                continue ;
            }
            if ( ch2 == 'U' )
            {
                // i points to the \ so i+6 is next character
                if ( i+8 >= s.length() )
                	throw new TurtleParseException("\\U escape too short", line, column) ;
                int x = hex(s, i+1, 8, line, column) ;
                // Convert to UTF-16 codepoint pair.
                sb.append((char)x) ;
                // Jump 1 2 3 4 5 6 7 8 -- already skipped \ and u
                i = i+8 ;
                column = column+8 ;
                continue ;
            }
            
            // Are we doing just point code escapes?
            // If so, \X-anything else is legal as a literal "\" and "X" 
            
            if ( pointCodeOnly )
            {
                sb.append('\\') ;
                sb.append(ch2) ;
                i = i + 1 ;
                continue ;
            }
            
            // Not just codepoints.  Must be a legal escape.
            char ch3 = 0 ;
            switch (ch2)
            {
                case 'n': ch3 = '\n' ;  break ; 
                case 't': ch3 = '\t' ;  break ;
                case 'r': ch3 = '\r' ;  break ;
                case 'b': ch3 = '\b' ;  break ;
                case 'f': ch3 = '\f' ;  break ;
                case '\'': ch3 = '\'' ; break ;
                case '\"': ch3 = '\"' ; break ;
                case '\\': ch3 = '\\' ; break ;
                default:
                	throw new TurtleParseException("Unknown escape: \\"+ch2, line, column) ;
            }
            sb.append(ch3) ;
        }
        return sb.toString() ;
    }

    // Line and column that started the escape
    public static int hex(String s, int i, int len, int line, int column) throws TurtleParseException
    {
//        if ( i+len >= s.length() )
//        {
//            
//        }
        int x = 0 ;
        for ( int j = i ; j < i+len ; j++ )
        {
           char ch = s.charAt(j) ;
           column++ ;
           int k = 0  ;
           switch (ch)
           {
               case '0': k = 0 ; break ; 
               case '1': k = 1 ; break ;
               case '2': k = 2 ; break ;
               case '3': k = 3 ; break ;
               case '4': k = 4 ; break ;
               case '5': k = 5 ; break ;
               case '6': k = 6 ; break ;
               case '7': k = 7 ; break ;
               case '8': k = 8 ; break ;
               case '9': k = 9 ; break ;
               case 'A': case 'a': k = 10 ; break ;
               case 'B': case 'b': k = 11 ; break ;
               case 'C': case 'c': k = 12 ; break ;
               case 'D': case 'd': k = 13 ; break ;
               case 'E': case 'e': k = 14 ; break ;
               case 'F': case 'f': k = 15 ; break ;
               default:
            	   throw new TurtleParseException("Illegal hex escape: "+ch, line, column) ;
           }
           x = (x<<4)+k ;
        }
        return x ;
    }
    
    public static String  unescapePName(String s, int line, int column) throws TurtleParseException
    {
        char escape = '\\' ;
        int idx = s.indexOf(escape) ;
        
        if ( idx == -1 )
            return s ;
        
        int len = s.length() ;
        StringBuilder sb = new StringBuilder() ;
        
        for ( int i = 0 ; i < len ; i++ )
        {
            // Copied form unescape abobve - share!
            char ch = s.charAt(i) ;
            // Keep line and column numbers.
            switch (ch)
            {
                case '\n': 
                case '\r':
                    line++ ;
                    column = 1 ;
                    break ;
                default:
                    column++ ;
                    break ;
            }

            if ( ch != escape )
            {
                sb.append(ch) ;
                continue ;
            }

            // Escape
            if ( i >= s.length()-1 )
            	throw new TurtleParseException("Illegal escape at end of string", line, column) ;
            char ch2 = s.charAt(i+1) ;
            column = column+1 ;
            i = i + 1 ;

           switch (ch2)
           {
               case '~' :
               case '.' : 
               case '-' : 
               case '!' : 
               case '$' : 
               case '&' : 
               case '\'' : 
               case '(' :
               case ')' : 
               case '*' : 
               case '+' : 
               case ',' : 
               case ';' : 
               case '=' : 
               case ':' :
               case '/' : 
               case '?' : 
               case '#' : 
               case '@' : 
               case '%' :
                   sb.append(ch2) ;
                   break ;
               default:
                   throw new TurtleParseException("Illegal prefix name escape: "+ch2, line, column) ;
           }
        }
        return sb.toString() ;
    }
    
    
    protected void warnDeprecation(String msg)
    {
    	System.err.println(msg);
    }
    
}