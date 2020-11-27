package org.semanticweb.yars.jaxrs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import org.kohsuke.MetaInfServices;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.ExceptionCollectingNxParser;

/**
 * A {@link MessageBodyReader} and {@link MessageBodyWriter} for <a
 * href="http://www.w3.org/TR/n-triples/">N-Triples</a>, N-Quads, and other Nx.
 * 
 * @author Tobias Käfer
 * @see AbstractRDFMessageBodyReaderWriter
 */
@Produces({ "application/n-triples", "application/n-quads" })// , "text/turtle" })
@Consumes({ "application/n-triples", "application/n-quads" })
@Provider
@MetaInfServices({MessageBodyWriter.class, MessageBodyReader.class})
public class NxMessageBodyReaderWriter extends
		AbstractRDFMessageBodyReaderWriter {

	public static final MediaType NTRIPLES_MEDIATYPE = new MediaType("application",
			"n-triples", UTF_8.name());
	public static final MediaType NQUADS_MEDIATYPE = new MediaType("application",
			"n-quads", UTF_8.name());
	public static final MediaType TURTLE_MEDIATYPE = new MediaType("text", "turtle",
			UTF_8.name());

	@Override
	boolean isReadableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return NTRIPLES_MEDIATYPE.isCompatible(mt) || NQUADS_MEDIATYPE.isCompatible(mt);
	}

	@Override
	boolean isWritableCheckMediatypeAndAnnotations(Annotation[] arg2,
			MediaType arg3) {
		return arg3.isCompatible(NTRIPLES_MEDIATYPE) || arg3.isCompatible(NQUADS_MEDIATYPE)
				|| arg3.isCompatible(TURTLE_MEDIATYPE);
	}

	@Override
	public void writeTo(Iterable<Node[]> arg0, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		Charset cs = getCharset(mediaType);
		byte[] newLineBytes = System.getProperty("line.separator").getBytes(cs);
		for (Node[] nx : arg0) {
			if (nx.length > 3 && mediaType.equals(NTRIPLES_MEDIATYPE))
				nx = new Node[] { nx[0], nx[1], nx[2] };
			entityStream.write(Nodes.toString(nx).getBytes(cs));
			entityStream.write(newLineBytes);
		}
	}

	@Override
	public Iterable<Node[]> readFrom(Class<Iterable<Node[]>> arg0,
			Type genericType, Annotation annotations[], MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		ExceptionCollectingNxParser nxp = new ExceptionCollectingNxParser();
		nxp.parse(new BufferedReader(new InputStreamReader(entityStream,
				getCharset(mediaType))));

		// check if already at the beginning, something went wrong
		nxp.hasNext();
		if (!nxp.getExceptions().isEmpty())
			throw new BadRequestException(nxp.getExceptions().iterator().next());
		else
			return nxp;
	}

}
