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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.kohsuke.MetaInfServices;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.parsers.turtle.TurtleParser;
import org.semarglproject.rdf.TurtleSerializer;
import org.semarglproject.sink.CharOutputSink;
import org.semarglproject.sink.CharSink;
import org.semarglproject.sink.TripleSink;

/**
 * A {@link MessageBodyReader} and {@link MessageBodyWriter} for <a
 * href="http://www.w3.org/TR/turtle/">Turtle</a>.
 * 
 * @author Tobias
 * @see AbstractRDFMessageBodyReaderWriter
 *
 */
@Consumes({ "text/turtle" })
@Produces({ "text/turtle" })
@Provider
@MetaInfServices({MessageBodyWriter.class, MessageBodyReader.class})
public class TurtleMessageBodyReader extends AbstractRDFMessageBodyReaderWriter {

	@Context
	UriInfo _uriinfo;

	@Override
	boolean isReadableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return NxMessageBodyReaderWriter.TURTLE_MEDIATYPE.isCompatible(mt);
	}

	@Override
	boolean isWritableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return NxMessageBodyReaderWriter.TURTLE_MEDIATYPE.isCompatible(mt);
	}

	@Override
	public void writeTo(Iterable<Node[]> arg0, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		CharOutputSink cs = new CharOutputSink(getCharset(mediaType));
		cs.setBaseUri(_uriinfo.getAbsolutePath().toString());
		cs.connect(entityStream);
		TripleSink ts = TurtleSerializer.connect((CharSink) cs);

		try {
			ts.startStream();

			for (Node[] nx : arg0) {
				
				String subject;
				if (nx[0] instanceof BNode)
					// it's a blank node
					subject = nx[0].toString();
				else
					// it's a resource
					subject = nx[0].getLabel();
				
				if (!(nx[2] instanceof Literal))
					// it's not a literal
					ts.addNonLiteral(subject, nx[1].getLabel(),
							nx[2].getLabel());
				else {
					// it's a literal
					Literal l = (Literal) nx[2];
					if (l.getDatatype() != null)
						ts.addTypedLiteral(subject, nx[1].getLabel(),
								nx[2].getLabel(), l.getDatatype().getLabel());
					else
						ts.addPlainLiteral(subject, nx[1].getLabel(),
								nx[2].getLabel(), l.getLanguageTag());

				}
			}

			ts.endStream();

		} catch (org.semarglproject.rdf.ParseException e) {
			throw new WebApplicationException(e.getCause());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new WebApplicationException(e.getCause());
		}

	}

	@Override
	public Iterable<Node[]> readFrom(Class<Iterable<Node[]>> arg0,
			Type genericType, Annotation annotations[], MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		TurtleParser nxp = new TurtleParser();
		nxp.parse(entityStream, _uriinfo.getAbsolutePath().toString(),
				getCharset(mediaType));

		try {
			nxp.hasNext();
		} catch (Exception e) {
			throw new WebApplicationException(e.getCause());
		}

		return nxp;
	}

}
