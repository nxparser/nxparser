package org.semanticweb.yars.jaxrs.trailingslash;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

/**
 * Filter to redirect from resources whose URI does not end in a slash to
 * resources with a slash using a <tt>301 Moved Permanently</tt> HTTP response.
 * 
 * Requires the resources which should exhibit this behaviour to be annotated
 * using {@link RedirectMissingTrailingSlash}.
 * 
 * @author Tobias Käfer
 *
 */
@Provider
@RedirectMissingTrailingSlash
public class RedirectMissingTrailingSlashFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String path = requestContext.getUriInfo().getAbsolutePath().getPath();
		if (path == null || !path.endsWith("/")) {
			requestContext.abortWith(Response.status(Status.MOVED_PERMANENTLY)
					.location(requestContext.getUriInfo().getAbsolutePathBuilder().path("/").build()).build());

		}

	}

}
