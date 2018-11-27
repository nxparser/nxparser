package org.semanticweb.yars.jaxrs;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

import org.kohsuke.MetaInfServices;

/**
 * A {@link ClientResponseFilter} that stores the URI of the request in a custom
 * header. Seems to be required for determining the request URI in a
 * {@link MessageBodyReader} if used from a JAX-RS client.
 * 
 * @author Tobias Käfer
 * @see AbstractRDFMessageBodyReaderWriter.REQUEST_URI_HEADER
 */
@Provider
@MetaInfServices(ClientResponseFilter.class)
public class URIinjectingClientResponseFilter implements ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		responseContext.getHeaders().putSingle(AbstractRDFMessageBodyReaderWriter.REQUEST_URI_HEADER, requestContext.getUri().toString());
	}

}
