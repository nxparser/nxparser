package org.semanticweb.yars.jaxrs.trailingslash;

import java.io.IOException;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter to tell that a resource ending in slash does not exist. Jersey 2 does
 * not get this otherwise. Annotate the resource using
 * {@link NotFoundOnTrailingSlash}.
 * 
 * @author Tobias Käfer
 *
 */
@Provider
@NotFoundOnTrailingSlash
public class NotFoundOnTrailingSlashFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String path = requestContext.getUriInfo().getAbsolutePath().getPath();
		if (path != null && path.endsWith("/")) {
			throw new NotFoundException();

		}

	}

}
