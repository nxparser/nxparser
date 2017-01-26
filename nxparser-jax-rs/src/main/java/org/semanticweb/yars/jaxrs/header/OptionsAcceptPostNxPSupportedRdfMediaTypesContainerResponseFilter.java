package org.semanticweb.yars.jaxrs.header;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.semanticweb.yars.jaxrs.JsonLdMessageBodyReaderWriter;
import org.semanticweb.yars.jaxrs.NxMessageBodyReaderWriter;
import org.semanticweb.yars.jaxrs.RdfXmlMessageBodyWriter;

/**
 * For POST requests, this {@link ContainerResponseFilter} reports a list of
 * MediaTypes currently supported by NxParser Jax-RS. Unfortunately, the
 * list of MediaTypes is not determined dynamically.
 * 
 * @author Tobias Käfer
 *
 */
@Provider
@OptionsAcceptPostNxPrdfSerialisations
public class OptionsAcceptPostNxPSupportedRdfMediaTypesContainerResponseFilter implements ContainerResponseFilter {

	private static String mediaTypes = JsonLdMessageBodyReaderWriter.JSONLD_MEDIATYPE + ","
			+ NxMessageBodyReaderWriter.NTRIPLES_MEDIATYPE + "," + NxMessageBodyReaderWriter.TURTLE_MEDIATYPE + ","
			+ RdfXmlMessageBodyWriter.RDF_XML_MEDIATYPE;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if (requestContext.getMethod().equals(HttpMethod.OPTIONS)
				&& responseContext.getAllowedMethods().contains(HttpMethod.POST))
			responseContext.getHeaders().add("Accept-Post", mediaTypes);
	}
}
