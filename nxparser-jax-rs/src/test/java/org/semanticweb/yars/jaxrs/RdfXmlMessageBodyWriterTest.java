package org.semanticweb.yars.jaxrs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.uri.UriTemplate;
import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.RDFS;
import org.semanticweb.yars.nx.parser.NxParser;

public class RdfXmlMessageBodyWriterTest {
	
	final Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	@Test
	public void test() throws WebApplicationException, IOException {
		
		log.log(Level.INFO, "Testing behaviour when having non-http uris or broken http uris as predicates.");
		
		String[] tripleStrings = new String[] {
				"_:bn1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.org/123#Class> .",
				"_:bn2 <hmi-vr:x1> \"1.7726401064446645E-1\"^^<xsd:float> ." };
		
		List<String> tripleStringList = Arrays.asList(tripleStrings);
		
		NxParser nxp = new NxParser(tripleStringList);
		
		RdfXmlMessageBodyWriter rxmbr = new RdfXmlMessageBodyWriter();
		
		rxmbr.uriinfo = new UriInfo() {

			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPath(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<PathSegment> getPathSegments() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<PathSegment> getPathSegments(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getRequestUri() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public UriBuilder getRequestUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getAbsolutePath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public UriBuilder getAbsolutePathBuilder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getBaseUri() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public UriBuilder getBaseUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getPathParameters() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getPathParameters(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getQueryParameters() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getMatchedURIs() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getMatchedURIs(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Object> getMatchedResources() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI resolve(URI uri) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI relativize(URI uri) {
				return URI.create("http://example.org/").relativize(uri);
			}};
		
		rxmbr.writeTo(nxp, null, null, null, null, null, System.out);
		
		
		
		
		

	}

	@Test
	public void numberTwoTest() throws WebApplicationException, IOException {
		
		log.log(Level.INFO, "Testing weird empty string resources.");

		Node[][] triples = new Node[][]{{new Resource(""), RDF.TYPE, RDFS.CLASS}};
	
		RdfXmlMessageBodyWriter rxmbr = new RdfXmlMessageBodyWriter();
		
		rxmbr.uriinfo = new UriInfo() {

			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPath(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<PathSegment> getPathSegments() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<PathSegment> getPathSegments(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getRequestUri() {
				return URI.create("http://webserver.i-vision.local:8080/roest/sim/whatever");
			}

			@Override
			public UriBuilder getRequestUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getAbsolutePath() {
				return URI.create("http://webserver.i-vision.local:8080/roest/sim/whatever");
			}

			@Override
			public UriBuilder getAbsolutePathBuilder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getBaseUri() {
				return URI.create("http://webserver.i-vision.local:8080/roest/");
			}

			@Override
			public UriBuilder getBaseUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getPathParameters() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getPathParameters(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getQueryParameters() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getMatchedURIs() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getMatchedURIs(boolean decode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Object> getMatchedResources() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
		    public URI resolve(URI uri) {
		        return UriTemplate.resolve(getBaseUri(), uri);
		    }

		    @Override
		    public URI relativize(URI uri) {
		        if (!uri.isAbsolute()) {
		            uri = resolve(uri);
		        }

		        return UriTemplate.relativize(getRequestUri(), uri);
		    }
			
		
		};
		
		final Resource emptyStringResource = new Resource("");
		
		Resource requestResource = new Resource("<" + rxmbr.uriinfo.getAbsolutePath().toString() + ">", true);
		for (Node[] nx : triples) {
			if (emptyStringResource.equals(nx[0]))
				nx[0] = requestResource;
		}
		
		rxmbr.writeTo(Arrays.asList(triples), null, null, null, null, null, System.out);

	}
	
}
