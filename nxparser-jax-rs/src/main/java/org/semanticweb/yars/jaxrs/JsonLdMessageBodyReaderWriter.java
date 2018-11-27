package org.semanticweb.yars.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.parsers.external.json.jsonld_java.JsonLDparser;
import org.semanticweb.yars.parsers.external.json.jsonld_java.JsonLDserialiser;
import org.semanticweb.yars.parsers.external.json.jsonld_java.JsonLDserialiser.JsonLDdocumentForm;
import org.semanticweb.yars.utils.CallbackIterator;
import org.semanticweb.yars.utils.ErrorHandlerImpl;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdError.Error;

/**
 * A {@link MessageBodyReader} and {@link MessageBodyWriter} for <a
 * href="https://www.w3.org/TR/json-ld/">JSON-LD</a>.
 *
 * @author Tobias Käfer
 * @see AbstractRDFMessageBodyReaderWriter
 *
 */
@Consumes({ "application/ld+json", "application/json" })
@Produces({ "application/ld+json", "application/json" })
@Provider
@MetaInfServices({ MessageBodyReader.class, MessageBodyWriter.class })
public class JsonLdMessageBodyReaderWriter extends AbstractRDFMessageBodyReaderWriter {
	
	private static final Logger log = Logger.getLogger(JsonLdMessageBodyReaderWriter.class.getName());

	@Context
	UriInfo _uriinfo;
	
	static final String JSONLD_MEDIATYPE_PROFILE_PARAMETER = "profile";
	
	public static final MediaType JSONLD_MEDIATYPE = new MediaType("application", "ld+json", UTF_8.name());
	public static final MediaType JSONLD_MEDIATYPE_WITH_DEFAULT_PROFILE;

	static {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(MediaType.CHARSET_PARAMETER, UTF_8.name());
		parameters.put(JSONLD_MEDIATYPE_PROFILE_PARAMETER, JsonLDserialiser.defaultJsonLDdocumentForm.getUriString());
		JSONLD_MEDIATYPE_WITH_DEFAULT_PROFILE = new MediaType("application", "ld+json", parameters);
	}

	@Override
	boolean isReadableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return JSONLD_MEDIATYPE.isCompatible(mt);
	}

	@Override
	boolean isWritableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return JSONLD_MEDIATYPE.isCompatible(mt);
	}

	@Override
	public Iterable<Node[]> readFrom(Class<Iterable<Node[]>> arg0,
			Type genericType, Annotation annotations[], MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		JsonLDparser jlp = new JsonLDparser(entityStream, getBaseURIdependingOnPostOrNot(httpHeaders));
		ErrorHandlerImpl eh = new ErrorHandlerImpl();

		jlp.setErrorHandler(eh);

		CallbackIterator cs = new CallbackIterator();

		jlp.parse(cs);

		if (eh.getFatalError() != null) {
			Exception ex = eh.getFatalError();
			if (ex instanceof JsonLdError) {
				JsonLdError e = (JsonLdError)ex;
				if (e.getType() == Error.LOADING_DOCUMENT_FAILED)
					throw new IOException(e);
				else
					throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
			}
		}
		return cs;
	}

	@Override
	public void writeTo(Iterable<Node[]> t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		JsonLDserialiser jls = new JsonLDserialiser(entityStream, UTF_8, getBaseURIdependingOnPostOrNot(httpHeaders));
		String profileUriString = mediaType.getParameters().get(JSONLD_MEDIATYPE_PROFILE_PARAMETER);
		if (profileUriString == null) {
			profileUriString = JsonLDserialiser.defaultJsonLDdocumentForm.getUriString();
		}

		JsonLDdocumentForm jlp;
		
		// Checking if the servlet code is annotated to use a custom context
		for (Annotation annotation : annotations) {
			List<String> remoteContexts = null; 
			if (JsonLDremoteContext.class.isAssignableFrom(annotation.annotationType())) {
				if (remoteContexts == null)
					remoteContexts = new LinkedList<String>();
				remoteContexts.addAll(Arrays.asList(((JsonLDremoteContext) annotation).value()));
			}
			if (remoteContexts != null)
				jls.setContext(remoteContexts);
		}

		if (JsonLDserialiser.JsonLDdocumentForm.isJsonLDprofileUriString(profileUriString))
			jlp = JsonLDdocumentForm.getJsonLDdocumentFormForProfileURIstring(profileUriString);
		else {
			log.log(Level.WARNING, "Using default JsonLDdocumentForm \"{1}\", because I got a bad profile URI: {0}",
					new Object[] { profileUriString, JsonLDserialiser.defaultJsonLDdocumentForm.name() });
			jlp = JsonLDserialiser.defaultJsonLDdocumentForm;
			httpHeaders.putSingle("Content-Type", JSONLD_MEDIATYPE_WITH_DEFAULT_PROFILE);
		}
		
		jls.setDocumentForm(jlp);

		jls.startDocument();
		for (Node[] nx : t)
			jls.processStatement(nx);
		jls.endDocument();
	}

}
