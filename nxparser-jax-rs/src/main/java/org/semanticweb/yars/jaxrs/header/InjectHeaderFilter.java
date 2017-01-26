package org.semanticweb.yars.jaxrs.header;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * A replacement for Jersey's DeclarativeLinkingFeature, which is currently in beta.
 * @author Tobias Käfer
 *
 */
@Provider
@InjectLinkHeader(value = "")
public class InjectLinkHeaderFilter implements ContainerResponseFilter {

	@Context
	private ResourceInfo rInfo;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		Set<String> links = new HashSet<String>();
		
		InjectLinkHeader ilh_class = rInfo.getResourceClass().getAnnotation(InjectLinkHeader.class);
		InjectLinkHeader ilh_method = rInfo.getResourceMethod().getAnnotation(InjectLinkHeader.class);

		for (InjectLinkHeader ilh : new InjectLinkHeader[] { ilh_class, ilh_method }) {
			if (ilh != null)
				links.add(ilh.value());
		}
		
		for (String link : links)
			responseContext.getHeaders().add("Link", link);
	}
}
