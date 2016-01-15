package org.semanticweb.yars.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.kohsuke.MetaInfServices;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.parsers.external.json.jsonld_java.JsonLDparser;
import org.semanticweb.yars.turtle.TurtleParser;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdError.Error;

/**
 * A {@link MessageBodyReader} and {@link MessageBodyWriter} for <a
 * href="http://www.w3.org/TR/turtle/">Turtle</a>.
 *
 * @author Tobias
 * @see AbstractRDFMessageBodyReaderWriter
 *
 */
@Consumes({ "application/ld+json" })
@Produces({ "application/ld+json" })
@Provider
@MetaInfServices({ MessageBodyReader.class, MessageBodyWriter.class })
public class JsonLdMessageBodyReader extends AbstractRDFMessageBodyReaderWriter {

	@Context
	UriInfo _uriinfo;
	
	static final MediaType JSONLD_MEDIATYPE = new MediaType("application", "ld+json", UTF_8.name());

	@Override
	boolean isReadableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return JSONLD_MEDIATYPE.isCompatible(mt);
	}

	@Override
	boolean isWritableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		// return JSONLD_MEDIATYPE.isCompatible(mt);
		// not implemented yet...
		return false;

	}

	@Override
	public Iterable<Node[]> readFrom(Class<Iterable<Node[]>> arg0,
			Type genericType, Annotation annotations[], MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		JsonLDparser jlp = new JsonLDparser();
		try {
			jlp.parse(entityStream, getBaseURIdependingOnPutPost().toString());
		} catch (JsonLdError e) {
			System.err.println(e);
			if (e.getType() == Error.LOADING_DOCUMENT_FAILED)
				throw new IOException(e);
			else
				throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
		return jlp;
	}

	@Override
	public void writeTo(Iterable<Node[]> arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream arg6) throws IOException, WebApplicationException {
		throw new WebApplicationException("{ \"message\":\"Not yet implemented...\"}",
				Response.Status.UNSUPPORTED_MEDIA_TYPE);
		// TODO Auto-generated method stub
		
	}

}
