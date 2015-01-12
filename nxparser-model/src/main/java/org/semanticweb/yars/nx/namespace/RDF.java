package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.util.ParseException;

public class RDF {
	public static final String NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	//properties
	public static final Resource TYPE = new Resource(NS+"type");
	
	public static final Resource SUBJECT = new Resource(NS+"subject");
	public static final Resource PREDICATE = new Resource(NS+"predicate");
	public static final Resource OBJECT = new Resource(NS+"object");
	
	public static final Resource FIRST = new Resource(NS+"first");
	public static final Resource REST = new Resource(NS+"rest");
	
	public static final Resource VALUE = new Resource(NS+"value");
	
	public static final Resource LANGRANGE = new Resource(NS+"langRange");
	
	//classes
	public static final Resource PROPERTY = new Resource(NS+"Property");
	
	public static final Resource SEQ = new Resource(NS+"Seq");
	public static final Resource BAG = new Resource(NS+"Bag");
	public static final Resource ALT = new Resource(NS+"Alt");
	public static final Resource LIST = new Resource(NS+"List");
	
	public static final Resource STATEMENT = new Resource(NS+"Statement");
	
	public static final Resource PLAINLITERAL = new Resource(NS+"PlainLiteral");
	
	//datatype
	public static final Resource XMLLITERAL = new Resource(NS+"XMLLiteral");
	
	
	//resources
	public static final Resource NIL = new Resource(NS+"nil");
	
	//RDF/XML syntax keywords
	public static final Resource RDF = new Resource(NS+"RDF");
	public static final Resource DESCRIPTION = new Resource(NS+"Description");
	
	public static final Resource ABOUT = new Resource(NS+"about");
	public static final Resource NODEID = new Resource(NS+"nodeID");
	public static final Resource ID = new Resource(NS+"ID");
	public static final Resource RESOURCE = new Resource(NS+"resource");
	
	public static final Resource PARSETYPE = new Resource(NS+"parseType");
	
	public static final Resource DATATYPE = new Resource(NS+"datatype");

	public static final Resource LI = new Resource(NS+"li");
	
	// TODO Move to utilities module
	public static final int parseContainerMembershipProperty(String p) throws ParseException{
		if(!p.startsWith(NS+"_") || p.equals(NS+"_")){
			throw new ParseException("Not a valid container membership property "+p);
		} else{
			int v;
			try{
				v = Integer.parseInt(p.substring((NS+"_").length()));
			} catch(Exception e){
				throw new ParseException("Not a valid container membership property, not a valid numeric value.");
			}
			if(v<1){
				throw new ParseException(v+ " not a valid container membership property value - must be 1 or greater.");
			}
			return v;
		}
	}
}
