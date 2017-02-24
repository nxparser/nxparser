package org.semanticweb.yars.jaxrs.header;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.semanticweb.yars.jaxrs.JsonLdMessageBodyReaderWriter;
import org.semanticweb.yars.jaxrs.NxMessageBodyReaderWriter;
import org.semanticweb.yars.jaxrs.RdfXmlMessageBodyWriter;

/**
 * This {@link ContainerResponseFilter} adds the <code>Accept-Post</code> header
 * to responses to a requests of a supplied list of HTTP methods. The header is
 * only added if the resource actually supports the POST request. The filter
 * reports the list of MediaTypes currently supported by NxParser Jax-RS.
 * Unfortunately, the list of MediaTypes is not determined dynamically.
 * 
 * @author Tobias Käfer
 * @see <a href="http://www.w3.org/TR/ldp/#header-accept-post">The Linked Data
 *      Platform specification on the Accept-Post header</a>
 *
 */
@Provider
@AcceptPostNxPrdfSerialisations
public class AcceptPostNxPSupportedRdfMediaTypesContainerResponseFilter implements ContainerResponseFilter {
	
	@Context
	private ResourceInfo rInfo;

	private static String mediaTypes = JsonLdMessageBodyReaderWriter.JSONLD_MEDIATYPE + ","
			+ NxMessageBodyReaderWriter.NTRIPLES_MEDIATYPE + "," + NxMessageBodyReaderWriter.TURTLE_MEDIATYPE + ","
			+ RdfXmlMessageBodyWriter.RDF_XML_MEDIATYPE;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		Set<String> httpMethods = new HashSet<String>();
		AcceptPostNxPrdfSerialisations annotation = rInfo.getResourceMethod().getAnnotation(AcceptPostNxPrdfSerialisations.class);
		if (annotation != null)
		httpMethods.addAll(Arrays
				.asList(annotation.value()));
		annotation = rInfo.getResourceClass().getAnnotation(AcceptPostNxPrdfSerialisations.class);
		httpMethods.addAll(Arrays
				.asList(annotation.value()));

		if (httpMethods.contains(requestContext.getMethod())
				&& responseContext.getAllowedMethods().contains(HttpMethod.POST))
			responseContext.getHeaders().add("Accept-Post", mediaTypes);
	}
}
