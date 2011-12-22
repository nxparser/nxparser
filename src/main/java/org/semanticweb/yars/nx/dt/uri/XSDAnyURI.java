package org.semanticweb.yars.nx.dt.uri;

import java.net.URI;
import java.net.URISyntaxException;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.XSD;

/**
 * xsd:anyURI datatype
 * @author aidhog
 *
 */
public class XSDAnyURI extends Datatype<URI> {
	public static final Resource DT = XSD.ANYURI;
	private URI _u;
	
	public XSDAnyURI(String s) throws DatatypeParseException{
		try{
			s = s.replaceAll(" ", "%20");
			_u = new URI(s);
		}catch(URISyntaxException e){
			throw new DatatypeParseException("Error parsing URI: "+e.getMessage()+".",s,DT,2);
		}
	}
	
	public String getCanonicalRepresentation() {
		return _u.toString();
	}

	public URI getValue() {
		return _u;
	}
	
	public static void main(String args[]) throws DatatypeParseException{
		XSDAnyURI dec = new XSDAnyURI("http://blah.com/asdj asd/aofds");
		System.err.println(dec.getCanonicalRepresentation());
	}
}