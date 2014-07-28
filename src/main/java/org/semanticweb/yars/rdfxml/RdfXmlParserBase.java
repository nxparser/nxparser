package org.semanticweb.yars.rdfxml;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.nx.util.NxUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * RDF/XML parser base using SAX parsing events
 * @author Aidan Hogan
 *
 */
public class RdfXmlParserBase extends DefaultHandler {
	//state = 0, awaiting opening rdf:RDF
	//state = 1, awaiting open resource element
	//state = 2, awaiting close resource element or open property element
	//state = 3, awaiting text, close property element or open resource element
	//state = 4, awaiting close property element
	//state = 5, awaiting text or close property element
	//state = 6, parsetype collection, awaiting open resource element or close property element
	//state = 7, parsetype literal, awaiting valid XML
	//state = 8, parsetype resource, awaiting open property or close property

	private static Logger _log = Logger.getLogger(RdfXmlParserBase.class.getName());

	private static enum State implements Comparable<State> {
		START, OR, CR_OP, T_CP_OR, CP, T_CP, PTC_OR_CP, PTL_XML, PTR_OP_CP, PTA;

//		public boolean expectCloseProperty(){
//			if(this.equals(T_CP_OR) || 
//					this.equals(CP) || this.equals(T_CP) ||
//					this.equals(PTC_OR_CP) || this.equals(PTL_XML)){
//				return true;
//			}
//			return false;
//		}
//
//		public boolean expectCloseResource(){
//			if(this.equals(CR_OP) || this.equals(PTL_XML)){
//				return true;	
//			}
//			return false;
//		}

		public boolean expectText(){
			if(this.equals(T_CP_OR) || this.equals(T_CP) || this.equals(PTL_XML)){
				return true;
			}
			return false;
		}

		public boolean expectOpenResource(){
			if(this.equals(START) || this.equals(OR)
					|| this.equals(T_CP_OR)
					|| this.equals(PTC_OR_CP)
					|| this.equals(PTL_XML)){
				return true;
			}
			return false;
		}

		public boolean expectOpenProperty(){
			if(this.equals(CR_OP) || this.equals(PTR_OP_CP)
					|| this.equals(PTL_XML) ){
				return true;
			}
			return false;
		}

		public boolean equals(State s){
			if(s==null)
				return false;
			else if(s.ordinal()!=ordinal())
				return false;
			return true;
		}
	}

	private boolean _skolemise = false;
	private boolean _strict = false;
	private String _escapedDocURI = null;

	public static final String BNODE_PREFIX = "bnode";
	private static final String XML_BASE = "xml:base";
	private static final String XML_LANG = "xml:lang";
	private static final String XML_SPACE = "xml:space";
	private static final String XML_ID = "xml:id";
	private static final String XML_PREFIX = "xml:";
	private static final String NULL = "";

	private ArrayList<Node> _fifoS;

	private ScopedThing<URI> _sbq;
//	private ScopedThing<Boolean> _sptr;
	private ScopedThing<String> _slang;


	private TreeSet<Integer> _sptr;
//	private ScopedThing<Boolean> _sptl = null;

	private Resource _sptlp = null;
	private int _sptldepth = 0;

	private HashMap<Integer, Integer> _li;
	private HashMap<Integer, Node> _coll;

	private HashMap<String,HashSet<URI>> _ids;

	private URI _currentBase;
	private int _currentLi = 0;
	private Node _currentColl = null;

	private Node _currentReify = null;

//	private ArrayList<Resource> _fifoP = new ArrayList<Resource>();
//	private ArrayList<Resource> _fifoT = new ArrayList<Resource>();
	private int _depth = 0;

	private int _bnode = 0;
	private String _currentLang = null;
	private Resource _datatype = null;

	//store prefix mappings for stuff inside an XMLLiteral definition
	private HashMap<String, String> _xmllPrefixes = null;
	private StringBuffer _prefixDefinition = null;

	private Resource _con = null;

	//state = 0, awaiting opening rdf:RDF
	//state = 1, awaiting open resource element
	//state = 2, awaiting close resource element or open property element
	//state = 3, awaiting text, close property element or open resource element
	//state = 4, awaiting close property element
	//state = 5, awaiting text or close property element
	//state = 6, parsetype collection, awaiting open resource element or close property element
	//state = 7, parsetype literal, awaiting valid XML
	//state = 8, parsetype resource, awaiting open property
	private State _state = State.START;


	//used for resources encoded within empty property nodes
	private ResourceDescription _currentPRD = null;

	private Node _currentS = null;
	private Resource _currentP = null;
	private Locator _loc = null;

	private StringBuffer _currentL = null;

	private Callback _c;

	//elements from RDF (syntax) namespace disallowed as use in nodes in RDF/XML
	private static final Node[] NOT_ALLOWED_NODE =  {RDF.ABOUT, RDF.DATATYPE, RDF.ID, RDF.NODEID, RDF.PARSETYPE, RDF.RESOURCE};
	private final static HashSet<Node> NOT_ALLOWED_NODE_TS = new HashSet<Node>();
	{
		for(Node n:NOT_ALLOWED_NODE){
			NOT_ALLOWED_NODE_TS.add(n);
		}
	}

	//RDF syntax names and their allowed positions
	private static final String[] RDF_SUBJ_NODE_NAMES =  {"Description", "Bag", "Seq", "Alt", "List", "Statement", "Property"};
	private static final String[] RDF_PROP_NODE_NAMES =  {"type", "subject", "predicate", "object", "first", "rest", "value", "li"};
	private static final String[] RDF_SUBJ_ATTR_NAMES =  {"about", "ID", "nodeID", "type"};
	private static final String[] RDF_PROP_ATTR_NAMES =  {"ID", "nodeID", "datatype", "parseType", "resource"};
	private final static HashSet<String> RDF_SUBJ_NODE_NAMES_TS = new HashSet<String>();
	private final static HashSet<String> RDF_PROP_NODE_NAMES_TS = new HashSet<String>();
	private final static HashSet<String> RDF_SUBJ_ATTR_NAMES_TS = new HashSet<String>();
	private final static HashSet<String> RDF_PROP_ATTR_NAMES_TS = new HashSet<String>();
	{
		for(String s:RDF_SUBJ_NODE_NAMES){
			RDF_SUBJ_NODE_NAMES_TS.add(s);
		}
		for(String s:RDF_PROP_NODE_NAMES){
			RDF_PROP_NODE_NAMES_TS.add(s);
		}
		for(String s:RDF_SUBJ_ATTR_NAMES){
			RDF_SUBJ_ATTR_NAMES_TS.add(s);
		}
		for(String s:RDF_PROP_ATTR_NAMES){
			RDF_PROP_ATTR_NAMES_TS.add(s);
		}
	}

//	public static void main (String argv [])
//	{
//	SAXParserFactory factory = SAXParserFactory.newInstance();
//	factory.setNamespaceAware(true);
//	try {

////	OutputStreamWriter out = new OutputStreamWriter (System.out, "UTF8");
//	SAXParser saxParser = factory.newSAXParser();

//	System.out.println(saxParser.isNamespaceAware()+" "+saxParser.isValidating());
//	saxParser.parse( new File("test/rdfxml/foafsmall.txt"), new RdfXmlParserBase("http://sw.deri.org/~aidanh/_foaf/@-foaf+.rdf", new true) );

//	} catch (Throwable err) {
//	err.printStackTrace ();
//	}
//	}

	public RdfXmlParserBase(String docURI, Callback c) throws SAXException{
		this(docURI, c, true, null);
	}

	public RdfXmlParserBase(String docURI, Callback c, boolean skolemise) throws SAXException{
		this(docURI, c, skolemise, null);
	}

	public RdfXmlParserBase(String docURI, Callback c, boolean skolemise, boolean strict) throws SAXException{
		this(docURI, c, skolemise, strict, null);
	}

	public RdfXmlParserBase(String docURI, Callback c, boolean skolemise, Resource con) throws SAXException{
		this(docURI, c, skolemise, false, con);
	}

	public RdfXmlParserBase(String docURI, Callback c, boolean skolemise, boolean strict, Resource con) throws SAXException{
		_fifoS = new ArrayList<Node>();
		initialiseBaseURI(docURI);
		setDocumentURI(docURI);
		_sbq = new ScopedThing<URI>(_currentBase);
		//_sptr = new ScopedThing<Boolean>(false);
		_sptr = new TreeSet<Integer>();
		_slang = new ScopedThing<String>(NULL);
		_li = new HashMap<Integer, Integer>();
		_coll = new HashMap<Integer, Node>();
		_ids = new HashMap<String,HashSet<URI>>();
		_skolemise = skolemise;
		_c = c;
		_con = con;
		_strict = strict;
	}

	private void setDocumentURI(String docURI){
		_escapedDocURI = BNode.escapeForBNode(docURI);
	}

	public void startDocument () throws SAXException{
		_c.startDocument();
	}

	public void endDocument () throws SAXException{
		_c.endDocument();
	}

	public void startElement (String name, String lname, String qname, Attributes attrs) throws SAXException{
		final Resource uri;
		if(name==null || name.equals("") && !_state.equals(State.PTL_XML)){
			if(lname.equals("RDF")&&_state.equals(State.START)){
				warning("Unqualified use of 'rdf:RDF' is deprecated.");
				uri = RDF.RDF;
			}else if(RDF_SUBJ_NODE_NAMES_TS.contains(lname) && _state.expectOpenResource()){
				warning("Unqualified use of RDF name '"+lname+"' is deprecated.");
				uri = createResource(RDF.NS+lname);
			}else if(RDF_PROP_NODE_NAMES_TS.contains(lname) && _state.expectOpenProperty()){
				warning("Unqualified use of RDF name '"+lname+"' is deprecated.");
				uri = createResource(RDF.NS+lname);
			}else{
				error("Unqualified attribute name '"+lname+"' found.");
				uri = createResource(resolveFullURI(lname, false));
			}
		}else{
			uri = createResource(name+lname);
		}

		nodeIsAllowed(qname, uri);

		if(_state.equals(State.START)){ //expecting open rdf:RDF or top level standalone element
//			for(int i=0; i<attrs.getLength(); i++){
//			String aqname = attrs.getQName(i);
//			String value = attrs.getValue(i);
//			if(aqname.equals("xml:base")){
//			initialiseBaseURI(value);
//			_sbq.addNewScopedElement(_currentBase);
//			}
//			_state = State.OR;
//			}
			checkAndHandleLang(attrs);
			checkAndHandleBaseURI(attrs);

			_state = State.OR;

			if(!uri.equals(RDF.RDF)){
				initialiseCurrentResource(uri, attrs);
				_state = State.CR_OP;
			} 
		} else if(_state.equals(State.OR)){ //expecting top level open resource
			initialiseCurrentResource(uri, attrs);
			_state = State.CR_OP;
		} else if(_state.equals(State.CR_OP)){ //expecting open property
			_state = State.T_CP_OR;
			initialiseCurrentProperty(uri, attrs);
		} else if(_state.equals(State.T_CP_OR)){ //expecting deep open resource
			_currentL = null;
			Node old = _currentS;
			initialiseCurrentResource(uri, attrs);
			handleStatement(old, _currentP, _currentS);
			_state = State.CR_OP;
		} else if(_state.equals(State.CP)){ //error, expecting close property tag
			fatalError("Expecting close property tag, not open element tag '"+qname+"'.");
		} else if(_state.equals(State.T_CP)){ //unless text found is only whitespace, then error, expecting more text or close property.
			fatalError("Expecting close property, not open element tag '"+qname+"'.");
		} else if(_state.equals(State.PTC_OR_CP)){ //within open collection property, expecting open resource elements of collection
			BNode collID = generateBNode();
			if(_currentColl==null){
				handleStatement(_currentS, _currentP, collID);
				_fifoS.add(_currentS);
			} else{
				handleStatement(_currentColl, RDF.REST, collID);
			}

			initialiseCurrentResource(uri, attrs);
			handleStatement(collID, RDF.FIRST, _currentS);

			_currentColl = null;
			_coll.put(_depth, collID);
			_state=State.CR_OP;
		} else if(_state.equals(State.PTL_XML)){ //expecting valid xml
			if(uri.equals(_sptlp)){
				_sptldepth++;
			}
			handleParseTypeLiteralStartElement(name, lname, qname, attrs);
		} else if(_state.equals(State.PTR_OP_CP)){ //parseType resource, expecting open property and creation of virtual resource
			Node old = _currentS;
			_currentS = generateBNode();
			_fifoS.add(_currentS);
			handleStatement(old, _currentP, _currentS);

			_state=State.T_CP_OR;
			initialiseCurrentProperty(uri, attrs);
		}

		_sbq.incrementScope();
		_slang.incrementScope();
	}

	private void handleParseTypeLiteralStartElement(final String name, final String lname, final String qname, final Attributes attrs){
		checkXMLLiteralPrefix(name, lname, qname);
		_currentL.append("<"+qname);

		for(int i=0; i<attrs.getLength(); i++){
			_currentL.append(" "+attrs.getQName(i)+"=\""+attrs.getValue(i)+"\"");
			checkXMLLiteralPrefix(attrs.getURI(i), attrs.getQName(i), attrs.getLocalName(i));
		}

		if(_prefixDefinition!=null){
			_currentL.append(_prefixDefinition.toString());
			_prefixDefinition = null;
		}

		_currentL.append(">");
	}

	private void checkAndHandleBaseURI(final Attributes attrs) throws SAXException{
		int b = attrs.getIndex(XML_BASE);
		if(b==-1){
			return;
		} else{
			initialiseBaseURI(attrs.getValue(b));
			_sbq.addNewScopedElement(_currentBase);
		}
	}

	private void checkAndHandleLang(final Attributes attrs) throws SAXException{
		int b = attrs.getIndex(XML_LANG);
		if(b==-1){
			return;
		} else{
			initialiseLang(attrs.getValue(b));
			if(_currentLang==null) _slang.addNewScopedElement(NULL);
			else _slang.addNewScopedElement(_currentLang);
		}
	}

	private void initialiseLang(final String lang) throws SAXException{
		//should probably check string here to see whether it is a valid language string
		if(lang.isEmpty())
			_currentLang = null;
		else _currentLang = lang.toLowerCase(Locale.ENGLISH);
	}

	private void initialiseBaseURI(String base) throws SAXException{
		if(base.contains(" ")){
			warning("Base uri '"+base+"' contains a space.");
			base = base.replaceAll(" ", "+");
		}

		try {
			_currentBase = new URI(base);
		} catch (URISyntaxException e) {
			fatalError(new SAXException(e));
		}
		if(!_currentBase.isAbsolute()){
			fatalError(new SAXException(new RDFXMLParseException("Cannot have relative xml:base value: '"+base+"'.")));
		}
	}

	private void initialiseCurrentResource(final Resource rq, final Attributes attrs) throws SAXException{
		_currentLi=0;
		_currentS = null;
		ResourceDescription temp = new ResourceDescription();

		if(!rq.equals(RDF.DESCRIPTION)){
			temp.addEdge(RDF.TYPE, rq);
		}

		checkAndHandleBaseURI(attrs);
		checkAndHandleLang(attrs);
		for(int i=0; i<attrs.getLength(); i++){
			Node[] edge = handleSubjectAttributePair(attrs.getURI(i), attrs.getLocalName(i), attrs.getQName(i), attrs.getValue(i));
			if(edge!=null){
				temp.addEdge(edge);
			}
		}

		if(_currentS==null){
			_currentS = generateBNode();
		}
		_fifoS.add(_currentS);

		for(Node[] edge:temp.getEdges()){
			handleStatement(_currentS, edge[0], edge[1]);
		}
	}

	private Node[] handleSubjectAttributePair(final String name, final String lname, final String qname, final String o) throws SAXException{
		final Resource p;
		if(name==null || name.equals("")){
			if(RDF_SUBJ_ATTR_NAMES_TS.contains(lname)){
				warning("Unqualified use of RDF name '"+lname+"' is deprecated.");
				p = createResource(RDF.NS+lname);
			}else{
				error("Unqualified attribute name '"+lname+"' found.");
				p = createResource(resolveFullURI(lname, false));
			}
		}else{
			p = createResource(name+lname);
		}

		if(p.equals(RDF.ABOUT)){
			if(_currentS!=null){
				fatalError("rdf:about used for resource already identified as '"+_currentS+"'.");
			}
			_currentS = createResource(resolveFullURI(o, false));
			return null;
		} else if(p.equals(RDF.ID)){
			if(_currentS!=null){
				fatalError("rdf:ID used for resource already identified as '"+_currentS+"'.");
			}
			_currentS = createResource(resolveFullURI(o, true));
			return null;
		} else if(p.equals(RDF.NODEID)){
			if(_currentS!=null){
				fatalError("rdf:nodeID used for resource already identified as '"+_currentS+"'.");
			}
			_currentS = generateBNode(o);
			return null;
		} else if(p.equals(RDF.TYPE)){
			return new Node[]{RDF.TYPE, createResource(o)};
		} else if(qname.equals("xml:base")){
			return null; //handled already by checkAndHandleBaseURI()
		} else if(qname.equals("xml:lang")){
			return null; //handled already by checkAndHandleLang()
		} else{
			return new Node[]{p, createLiteral(o)};
		}
	}

	private String resolveFullURI( String id, final boolean rdfID) throws SAXException{
		URI uri = null;
		int oldLength = id.length();
		id = id.trim();
		if(oldLength!=id.length()){
			warning("Leading or trailing whitespace in id "+id+".");
		}

		if(id.contains(" ")){
			warning("ID "+id+" contains a space.");
			id = id.replaceAll(" ", "+");
		}

		try {
			if(rdfID){
				uri = new URI("#"+id);
			} else uri = new URI(id);
		} catch (URISyntaxException e) {
			fatalError(new SAXException(e));
		}
		
		if(uri.isAbsolute()){
			if(rdfID){
				error("Absolute URI provided for rdf:ID. Not resolving against base URI.");
			}
			return id;
		} else{
			if(id.isEmpty()){
				if(_currentBase.getFragment()!=null)
					return removeFragment(_currentBase);
				else
					return _currentBase.toString();
			}
			if((_currentBase.getPath()==null ||_currentBase.getPath().length()==0) && !_currentBase.toString().endsWith("/")){
					try {
						return new URL(_currentBase.toURL(),id).toString();
					} catch (MalformedURLException e) {
						error("MalformedURLException resolving base:"+_currentBase+" id: "+id);
					}
			}
			
			URI full = _currentBase.resolve(uri); 
			
			if(rdfID){
				if(trackDuplicateIds(id,full)){
					warning("Duplicate value '"+id+"' for rdf:ID attribute.");
				}
				if (!Pattern.matches(XmlRegex.NC_NAME, id)){
					warning("ID value '"+id+"' is not a valid XML NCName.");
				}
			}
			return full.toString();
		}
	}

	/**
	 * Check to see if an rdf:ID is duplicate ... indexes current ID.
	 * 
	 * Duplication is allowed if the resulting URIs are different (i.e., the base has been changed).
	 * 
	 * @param id
	 * @param uri
	 * @return true if duplicate, false otherwise
	 */
	private boolean trackDuplicateIds(String id, URI uri) {
		HashSet<URI> urisForId = _ids.get(id);
		if(urisForId==null){
			urisForId = new HashSet<URI>();
			_ids.put(id, urisForId);
		}
		return !urisForId.add(uri);
	}

	private  String removeFragment(URI u)throws SAXException{
		// remove fragment
		try {
			
			return new URI(u.getScheme().toLowerCase(),
					u.getUserInfo(), u.getHost().toLowerCase(), u.getPort(),
					u.getPath(), u.getQuery(), null).toString();
		} catch (URISyntaxException e) {
			error("URISyntaxException removing fragment from base:"+u);
			return u.toString();
		}
	}
	
	private Literal createLiteral(final String s){
		Literal l;
		if(_datatype!=null){
			l = new Literal(NxUtil.escapeForNx(s), _datatype);
			_datatype = null;
		} else if(_currentLang!=null){
			l = new Literal(NxUtil.escapeForNx(s), _currentLang);
		}  else{
			l = new Literal(NxUtil.escapeForNx(s));
		}
		return l;
	}

	private void initialiseCurrentProperty(final Resource uri, final Attributes attrs) throws SAXException{
		_currentP = uri;

		if(_currentP.equals(RDF.LI)){
			_currentLi++;
			_li.put(_depth, _currentLi);
			_currentP = createResource(RDF.NS+"_"+_currentLi);
		}

		_depth++;
		checkAndHandleBaseURI(attrs);
		checkAndHandleLang(attrs);
		for(int i=0; i<attrs.getLength(); i++){
			handlePropertyAttributePair(attrs.getURI(i), attrs.getLocalName(i), attrs.getQName(i), attrs.getValue(i));
		}

		if(_currentPRD!=null){
			Node id = _currentPRD.getIdentifier();
			if(id==null){
				id =  generateBNode();
			}
			handleStatement(_currentS, _currentP, id);
			for(Node[] edge:_currentPRD.getEdges()){
				handleStatement(id, edge[0], edge[1]);
			}
			_currentPRD=null;
		}
	}

	/**
	 * Used exclusively for XMLLiteral
	 */
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException{
		if(_state.equals(State.PTL_XML)){
			handleXMLLiteralPrefixMapping(prefix, uri);
		}
	}

	/**
	 * Used exclusively for XMLLiteral.
	 */
	private void handleXMLLiteralPrefixMapping(final String prefix, final String uri){
		_xmllPrefixes.put(prefix, uri);

		if(_prefixDefinition==null){
			_prefixDefinition = new StringBuffer();
		}
		_prefixDefinition.append(" "+createPrefixDefinition(prefix, uri));
	}

	/**
	 * Used exclusively for XMLLiteral
	 */
	private String createPrefixDefinition(final String prefix, final String uri){
		if(prefix==null || prefix.isEmpty()){
			return "xmlns=\""+uri+"\"";
		}
		return "xmlns:"+prefix+"=\""+uri+"\"";
	}

	/**
	 * Used exclusively for XMLLiteral
	 */
	private String getPrefix(final String qname, final String lname){
		if(qname.length()==lname.length())
			return "";
		return qname.substring(0, (qname.length()-(lname.length()+1)));
	}

	/**
	 * Used exclusively for XMLLiteral
	 */
	private void checkXMLLiteralPrefix(final String name, final String lname, final String qname){
		if(name.equals("")){
			return; 
		}
		String prefix = getPrefix(qname, lname);
		if(_xmllPrefixes.get(prefix)==null){
			handleXMLLiteralPrefixMapping(prefix, name);
		}
	}

	/**
	 * Used exclusively for XMLLiteral
	 */
	public void endPrefixMapping(final String prefix) throws SAXException{
		if(_state.equals(State.PTL_XML)){
			_xmllPrefixes.remove(prefix);
		}
	}

	private void handlePropertyAttributePair(final String name, final String lname, final String qname, final String o) throws SAXException{
		final Resource p;
		if(name==null || name.equals("")){
			if(RDF_PROP_ATTR_NAMES_TS.contains(lname)){
				warning("Unqualified use of RDF name "+lname+" is deprecated.");
				p = createResource(RDF.NS+lname);
			} else if(lname.startsWith("xml")){
				//silently ignore according to this weird W3C testcase
				 //http://www.w3.org/2000/10/rdf-tests/rdfcore/unrecognised-xml-attributes/test002.rdf
				
//				warning("Unrecognised unqualified attribute starting with XML: "+lname+".");	
				return;
			} else {
				error("Unqualified attribute name "+lname+" found.");
				p = createResource(resolveFullURI(lname, false));
			}
		} else{
			p = createResource(name+lname);
		}

		if(p.equals(RDF.RESOURCE)){
			if(_currentPRD!=null && _currentPRD.getIdentifier()!=null){
				fatalError("Cannot have more than one rdf:resource/rdf:nodeID attached as attributes to a property.");
			} else if(_state.equals(State.T_CP)){
				fatalError("Cannot have both rdf:datatype and rdf:resource attached as attributes to a property.");
			} else if(_state.equals(State.PTC_OR_CP)){
				fatalError("Cannot have both rdf:parseType='Collection' and rdf:resource attached as attributes to a property.");
			} else if(_state.equals(State.PTL_XML)){
				fatalError("Cannot have both rdf:parseType='Literal' and rdf:resource attached as attributes to a property.");
			} else if(_state.equals(State.PTR_OP_CP)){
				fatalError("Cannot have both rdf:parseType='Resource' and rdf:resource attached as attributes to a property.");
			}

			Resource id = createResource(resolveFullURI(o, false));
			if(_currentPRD==null){
				_currentPRD = new ResourceDescription(id);
			} else{
				_currentPRD.setIdentifier(id);
			}
			_state = State.CP;
		} else if(p.equals(RDF.NODEID)){
			if(_currentPRD!=null && _currentPRD.getIdentifier()!=null){
				fatalError("Cannot have more than one rdf:resource/rdf:nodeID attached as attributes to a property.");
			} else if(_state.equals(State.T_CP)){
				fatalError("Cannot have both rdf:datatype and rdf:nodeID attached as attributes to a property.");
			} else if(_state.equals(State.PTC_OR_CP)){
				fatalError("Cannot have both rdf:parseType='Collection' and rdf:nodeID attached as attributes to a property.");
			} else if(_state.equals(State.PTL_XML)){
				fatalError("Cannot have both rdf:parseType='Literal' and rdf:nodeID attached as attributes to a property.");
			} else if(_state.equals(State.PTR_OP_CP)){
				fatalError("Cannot have both rdf:parseType='Resource' and rdf:nodeID attached as attributes to a property.");
			}

			BNode id = generateBNode(o);
			if(_currentPRD==null){
				_currentPRD = new ResourceDescription(id);
			} else{
				_currentPRD.setIdentifier(id);
			}
			_state = State.CP;
		} else if(p.equals(RDF.TYPE)){
			if(_state.equals(State.T_CP)){
				fatalError("Cannot have both rdf:datatype and rdf:type attached as attributes to a property.");
			} else if(_state.equals(State.PTC_OR_CP)){
				fatalError("Cannot have both rdf:parseType='Collection' and rdf:type attached as attributes to a property.");
			} else if(_state.equals(State.PTL_XML)){
				fatalError("Cannot have both rdf:parseType='Literal' and rdf:type attached as attributes to a property.");
			} else if(_state.equals(State.PTR_OP_CP)){
				fatalError("Cannot have both rdf:parseType='Resource' and rdf:type attached as attributes to a property.");
			}

			if(_currentPRD==null){
				_currentPRD = new ResourceDescription();
			}
			_currentPRD.addEdge(RDF.TYPE, createResource(o));
			_state = State.CP;
		} else if(qname.equals(XML_BASE)){
			;//handled already by checkAndHandleBaseURI()
		} else if(qname.equals(XML_LANG)){
			;//handled already by checkAndHandleBaseURI()
		} else if(qname.equals(XML_SPACE)){
			;/**@todo do nothing???**/	
		} else if(qname.equals(XML_ID)){
			;/**@todo do nothing???**/	
		} else if(qname.startsWith(XML_PREFIX)){
			// just ignore it according to W3C test-cases
			// warning("Unrecognised xml qname "+qname+".");	
		} else if(p.equals(RDF.DATATYPE)){
			if(_state.equals(State.CP)){
				fatalError("Cannot have both rdf:datatype and rdf:resource attached as attributes to a property.");
			} else if(_state.equals(State.PTC_OR_CP)){
				fatalError("Cannot have both rdf:datatype and rdf:parseType='Collection' attached as attributes to a property.");
			} else if(_state.equals(State.PTL_XML)){
				fatalError("Cannot have both rdf:datatype and rdf:parseType='Literal' attached as attributes to a property.");
			} else if(_state.equals(State.PTR_OP_CP)){
				fatalError("Cannot have both rdf:datatype and rdf:parseType='Resource' attached as attributes to a property.");
			}
			_datatype = createResource(resolveFullURI(o, false));
			_state = State.T_CP; //will be incremented outside of call to 5 :(
		} else if(p.equals(RDF.PARSETYPE)){
			if(o.equals("Collection")){
				if(_state.equals(State.CP)){
					fatalError("Cannot have both rdf:resource and rdf:parseType='Collection' attached as attributes to a property.");
				} else if(_state.equals(State.T_CP)){
					fatalError("Cannot have both rdf:datatype and rdf:parseType='Collection' attached as attributes to a property.");
				} else if(_state.equals(State.PTL_XML)){
					fatalError("Cannot have both rdf:parseType='Literal' and rdf:parseType='Collection' attached as attributes to a property.");
				} else if(_state.equals(State.PTR_OP_CP)){
					fatalError("Cannot have both rdf:parseType='Resource' and rdf:parseType='Collection' attached as attributes to a property.");
				}
				_state=State.PTC_OR_CP; //will be increment outside to 6 :(
			}
			else if(o.equals("Literal")){
				if(_state.equals(State.CP)){
					fatalError("Cannot have both rdf:resource and rdf:parseType='Literal' attached as attributes to a property.");
				} else if(_state.equals(State.T_CP)){
					fatalError("Cannot have both rdf:datatype and rdf:parseType='Literal' attached as attributes to a property.");
				} else if(_state.equals(State.PTC_OR_CP)){
					fatalError("Cannot have both rdf:parseType='Collection' and rdf:parseType='Literal' attached as attributes to a property.");
				} else if(_state.equals(State.PTR_OP_CP)){
					fatalError("Cannot have both rdf:parseType='Resource' and rdf:parseType='Literal' attached as attributes to a property.");
				}
				_state=State.PTL_XML; //will be incremented outside of call to 7 :(
				_sptlp = _currentP;
				_sptldepth = 0;
				_xmllPrefixes = new HashMap<String, String>();
				_currentL = new StringBuffer();
			} else if(o.equals("Resource")){
				if(_state.equals(State.CP)){
					fatalError("Cannot have both rdf:resource and rdf:parseType='Resource' attached as attributes to a property.");
				} else if(_state.equals(State.T_CP)){
					fatalError("Cannot have both rdf:datatype and rdf:parseType='Resource' attached as attributes to a property.");
				} else if(_state.equals(State.PTC_OR_CP)){
					fatalError("Cannot have both rdf:parseType='Collection' and rdf:parseType='Resource' attached as attributes to a property.");
				} else if(_state.equals(State.PTL_XML)){
					fatalError("Cannot have both rdf:parseType='Literal' and rdf:parseType='Resource' attached as attributes to a property.");
				}
				_currentL = null;
				_sptr.add(_depth);
				_state=State.PTR_OP_CP; //will be incremented outside of call to 8 :(
			}
		} else if(p.equals(RDF.ID)){
			_currentReify = createResource(resolveFullURI(o, true));  
		} else{
			if(_state.equals(State.T_CP)){
				fatalError("Cannot have both rdf:datatype and "+qname+" attached as attributes to a property.");
			} else if(_state.equals(State.PTC_OR_CP)){
				fatalError("Cannot have both rdf:parseType='Collection' and "+qname+" attached as attributes to a property.");
			} else if(_state.equals(State.PTL_XML)){
				fatalError("Cannot have both rdf:parseType='Literal' and "+qname+" attached as attributes to a property.");
			} else if(_state.equals(State.PTR_OP_CP)){
				fatalError("Cannot have both rdf:parseType='Resource' and "+qname+" attached as attributes to a property.");
			}

			if(_currentPRD==null){
				_currentPRD = new ResourceDescription();
			}
			_currentPRD.addEdge(p, createLiteral(o));
			_state = State.CP;
		}
	}

	public void endElement (String name, String lname, String qname) throws SAXException{
		Resource rq = createResource(name+lname);

		URI base = _sbq.decrementScope();
		if(base!=null){
			_currentBase = base;
		}

		boolean ptr = _sptr.remove(_depth);
		boolean endptl = _sptldepth==0 && _sptlp!=null && (_sptlp.equals(rq) || (isCMP(_sptlp) && isCMP(rq)));

		if(rq.equals(RDF.RDF)){
			//error handled by SAX, not valid XML;
		} else if(_state.equals(State.OR)){
			//error handled by SAX, not valid XML;
		} else if(_state.equals(State.CR_OP)){ //closing resource or closing parseType="Resource" property
			_fifoS.remove(_fifoS.size()-1);
			_li.remove(_depth);

			if(ptr){ //closing parseType="Resource" property
				//close 'virtual' resource
				_currentS = _fifoS.get(_fifoS.size()-1);
				Integer li = _li.get(_depth-1);
				if(li!=null)
					_currentLi = li;

				//then close property, leave in state 2
				_depth--;
			}
			else{
				if(_depth>0){ //not top level resource (not including rdf:RDF)
					_currentS = _fifoS.get(_fifoS.size()-1);
					Integer li = _li.get(_depth-1);
					if(li!=null)
						_currentLi = li;
					_state=State.CP;

					Node cc = _coll.get(_depth);
					if(cc!=null){
						_currentColl = cc;
						_state=State.PTC_OR_CP;
					}
				} else{ //close top level resource (not including rdf:RDF) -- end of document
					_state=State.START;
				}
			}
		} else if(_state.equals(State.T_CP_OR) || _state.equals(State.T_CP)){ //closing empty property or property after some text
			String s = "";
			if(_currentL!=null)
				s = _currentL.toString();

			Literal l = createLiteral(s);
			handleStatement(_currentS, _currentP, l);
			_currentL=null;

			_depth--;
			_state=State.CR_OP;
		} else if(_state.equals(State.CP)){ //closing property after closing resource
			_depth--;
			_state=State.CR_OP;
		} else if(_state.equals(State.PTC_OR_CP)){ //closing parseType="Collection" property
			_coll.remove(_depth);
			if(_currentColl!=null){
				handleStatement(_currentColl, RDF.REST, RDF.NIL);
				_currentColl=null;
			}else{
				handleStatement(_currentS, _currentP, RDF.NIL);
			}

			_depth--;
			_state=State.CR_OP;
		} else if(_state.equals(State.PTL_XML)){ //closing parseType="Literal" property
			if(endptl){
				String s = "";

				_state=State.CR_OP;
				if(_currentL!=null)
					s = _currentL.toString();

				_datatype = RDF.XMLLITERAL;
				Literal l = createLiteral(s);
				handleStatement(_currentS, _currentP, l);
				_currentL=null;
				_sptldepth = 0;
				_sptlp = null;
				_depth--;
			}else{
				if(rq.equals(_sptlp)){
					_sptldepth--;
				}
				handleParseTypeLiteralEndElement(name, lname, qname);
			}

		} else if(_state.equals(State.PTR_OP_CP)){ //closing empty parseType resource property
			handleStatement(_currentS, _currentP, generateBNode());
			_depth--;
			_state=State.CR_OP;
		}

		String lang = _slang.decrementScope();
		if(lang!=null){
			if(lang.equals(""))
				_currentLang = null;
			else
				_currentLang = lang.toLowerCase();
		}
	}

	private static boolean isCMP(Resource p1) {
		return p1.equals(RDF.LI) || p1.toString().startsWith(RDF.NS+"_");
	}

	private void handleParseTypeLiteralEndElement(final String name, final String lname, final String qname){
		_currentL.append("</"+qname+">");
	}

	private BNode generateBNode(){
		_bnode++;
		if(_skolemise){
			return new BNode(_escapedDocURI+"xx"+BNODE_PREFIX+(_bnode-1));
		} else{
			return new BNode(BNODE_PREFIX+(_bnode-1));
		}
	}

	private BNode generateBNode(String nodeID) throws SAXException{
		int oldLength = nodeID.length();
		nodeID = nodeID.trim();

		if(oldLength!=nodeID.length())
			warning("Leading or trailing whitespace in rdf:nodeID "+nodeID+".");

		if (!Pattern.matches(XmlRegex.NC_NAME, nodeID))
			warning("nodeID value '"+nodeID+"' is not a valid XML NCName.");

		nodeID = BNode.escapeForBNode(nodeID);
		if(_skolemise){
			return new BNode(_escapedDocURI+"xx"+nodeID);
		} else{
			return new BNode(nodeID);
		}
	}

	/**Create and handle warning exception
	 * @throws SAXException 
	 */
	private void warning(final String msg) throws SAXException{
		SAXException e = new SAXException(new RDFXMLParseException("WARNING: "+msg+" "+getLocation()));

		warning(e);
	}

	/**Create and handle error exception
	 * @throws SAXException 
	 */
	private void error(final String msg) throws SAXException{
		SAXException e = new SAXException(new RDFXMLParseException("ERROR: "+msg+" "+getLocation()));

		error(e);
	}

	/**Create and handle fatal error exception
	 * @throws SAXException 
	 * @throws
	 */
	private void fatalError(final String msg) throws SAXException{
		SAXException e = new SAXException(new RDFXMLParseException("FATAL ERROR: "+msg+" "+getLocation()));

		fatalError(e);
	}

	/**Warning
	 * @throws SAXException
	 */
	public void warning(SAXException e) throws SAXException{
		_log.warning(e.getMessage());
		if(_strict)
			throw e;
	}

	/**
	 * Recoverable error
	 * @throws SAXException
	 */
	public void error(SAXException e) throws SAXException{
		_log.warning(e.getMessage());
		if(_strict)
			throw e;
	}

	/**
	 * Unrecoverable error
	 * @throws SAXException
	 */
	public void fatalError(SAXException e) throws SAXException{
		_log.severe(e.getMessage());
		throw e;
	}

	public void ignorableWhitespace(char[] buf, int offset, int len) throws SAXException{
		char[] cs = new char[len];
		System.arraycopy(buf, offset, cs, 0, len);
		String s = new String(cs);

		if(_state.expectText()){
			if(_currentL==null){
				_currentL = new StringBuffer();
			}

			_currentL.append(s);
		}
	}

	private boolean nodeIsAllowed(final String qname, final Resource r) throws SAXException {
		if(NOT_ALLOWED_NODE_TS.contains(r)){
			error("Attribute '"+qname+"' not allowed to be used as a node element.");
			return false;
		}
		return true;
	}

	public void characters (char buf [], int offset, int len) throws SAXException{
		char[] cs = new char[len];
		System.arraycopy(buf, offset, cs, 0, len);
		String v = new String(cs);
		boolean ws = v.trim().equals("");
		if(ws){
			ignorableWhitespace(buf, offset, len);
			return;
		}

		if(_state.expectText()){
			if(_currentL==null){
//				if(_state.equals(State.PTL_XML)){
//				fatalError("Dangling text '"+v+"' found outside of enclosing tags, inside parseType=\"Literal\" tags.");
//				}
				_currentL = new StringBuffer();
			}

			for(int i=offset; i<(len+offset); i++){
				char c = buf[i];

				if(_state.equals(State.PTL_XML)){
					if (c == '"')
						_currentL.append("&quot;");
					else if (c == '&')
						_currentL.append("&amp;");
					else if (c == '<')
						_currentL.append("&lt;");
					else if (c == '>')
						_currentL.append("&gt;");
					else {
						int ci = 0xffff & c;
						if (ci < 160 )
							// nothing special only 7 Bit
							_currentL.append(c);
						else {
							// Not 7 Bit use the unicode system
							_currentL.append("&#");
							_currentL.append(new Integer(ci).toString());
							_currentL.append(';');
						}
					}
				}
				else _currentL.append(c);
			}

			if(_state.equals(State.T_CP_OR))
				_state=State.T_CP;
		} else{
			fatalError("Dangling text '"+v+"' found outside of enclosing property tags.");
		}
	}
	
	public void skippedEntity(String name) throws SAXException{
		;
	}
	
	public InputSource resolveEntity(String publicId,
            String systemId)
     throws SAXException{
		return null;
	}
	
	public void unparsedEntityDecl(String name,
			String publicId,
            String systemId,
            String notationName)
     throws SAXException{
		;
	}

	public void setDocumentLocator(Locator loc) {
		_loc = loc;
	}

	private String getLocation(){
		if(_loc!=null)
			return "Line "+_loc.getLineNumber()+" column "+_loc.getColumnNumber()+".";
		return "";
	}
	
	public static Resource createResource(String raw){
		return new Resource(NxUtil.escapeForNx(raw));
	}
	
	private void handleStatement(final Node... triple){
		processStatement(triple);
		if(_currentReify!=null){
			processStatement(_currentReify, RDF.TYPE, RDF.STATEMENT);
			processStatement(_currentReify, RDF.SUBJECT, triple[0]);
			processStatement(_currentReify, RDF.PREDICATE, triple[1]);
			processStatement(_currentReify, RDF.OBJECT, triple[2]);
			_currentReify = null;
		}
	}

	private void processStatement(final Node... triple){
		if(_con!=null){
			_c.processStatement(new Node[]{triple[0],triple[1],triple[2],_con});
		}
		else _c.processStatement(triple);
	}

	public static class ResourceDescription{
		private Node _id = null;
		private ArrayList<Node[]> _edges;

		public ResourceDescription(){
			_edges = new ArrayList<Node[]>();
		}

		public ResourceDescription(Node id){
			_edges = new ArrayList<Node[]>();
			_id = id;
		}

		public void setIdentifier(Node id){
			_id = id;
		}

		public Node getIdentifier(){
			return _id;
		}

		public void addEdge(Node... po){
			_edges.add(po);
		}

		public ArrayList<Node[]> getEdges(){
			return _edges;
		}

		public String toString(){
			StringBuffer buf = new StringBuffer();
			for(Node[] edge:_edges){
				buf.append(_id.toN3()+" "+Nodes.toN3(edge)+"\n");
			}

			return buf.toString();
		}
	}

	public static class RDFXMLParseException extends ParseException{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RDFXMLParseException(){
			super();
		}

		public RDFXMLParseException(String msg){
			super(msg);
		}
	}

//	public static class NodeIntPair{
//	private Node _n;
//	private int _i;

//	public NodeIntPair(Node n, int i){
//	_n = n;
//	_i = i;
//	}

//	public boolean equals(Object o){
//	if(o instanceof NodeIntPair){
//	return equals((NodeIntPair)o);
//	} return false;
//	}

//	public boolean equals(NodeIntPair nip){
//	if(nip._i==_i){
//	return nip._n.equals(_n);
//	} return false;
//	}

//	public Node getNode() {
//	return _n;
//	}

//	public void setNode(Node n) {
//	_n = n;
//	}

//	public int getInt() {
//	return _i;
//	}

//	public void setInt(int i) {
//	_i = i;
//	}

//	public String toString(){
//	return(_n.toN3()+" "+_i);
//	}

//	public int hashCode(){
//	return _n.hashCode() + _i;
//	}
//	}

	public static class ScopedThing<E>{
		private LinkedList<Integer> _skip;
		private LinkedList<E> _scoped;
		private int _currentSkip = -1;
		private E _current = null;
		private E _global;

		public ScopedThing(){
			this(null);
		}

		/**
		 * Checks if an element is in-scope. Not used at the moment/not optimised.
		 */
		public boolean inScope(E element){
			if(_current!=null && _current.equals(element)){
				return true;
			} else if(_global!=null && _global.equals(element)){
				return true;
			} else if(_scoped.contains(element)){
				return true;
			}
			return false;
		}

		public ScopedThing(E global){
			_currentSkip = -1;
			_skip = new LinkedList<Integer>();
			_scoped = new LinkedList<E>();
			_global = global;
		}

		/**
		 * Increments scope of all objects in stack.
		 */
		public void incrementScope(){
			if(_currentSkip!=-1)
				_currentSkip++;
		}

		/**
		 * Decrements scope, removes out of scope objects as applicable and checks if in-scope object has changed.
		 * @return New in-scope object iff the current in-scope object changes, otherwise null.
		 */
		public E decrementScope(){
			if(_currentSkip==-1){
				return null;
			} 
			else{
				_currentSkip--;
				if(_currentSkip==0){
					if(_skip.size()>0){
						_currentSkip = _skip.removeLast();
						_current = _scoped.removeLast();
						return _current;
					} else{
						_currentSkip=-1;
						return _global;
					}
				}
				return null;
			}
		}

		/**
		 * Adds a new scoped element to the stack.
		 */
		public void addNewScopedElement(E element){
			if(_currentSkip!=-1){
				_skip.add(_currentSkip);
				_scoped.add(_current);
			}

			_currentSkip=0;
			_current = element;
		}

		/**
		 * Sets current scoped element.
		 */
		public void setCurrent(E element){
			_current = element;
		}
	}
}
