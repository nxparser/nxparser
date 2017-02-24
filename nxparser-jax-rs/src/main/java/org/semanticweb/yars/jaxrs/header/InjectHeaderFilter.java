package org.semanticweb.yars.jaxrs.header;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * A replacement for Jersey's DeclarativeLinkingFeature, which is currently in
 * beta.
 * 
 * @author Tobias Käfer
 *
 */
@Provider
@InjectHeaders(value = {})
public class InjectHeaderFilter implements ContainerResponseFilter {

	@Context
	private ResourceInfo rInfo;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		InjectHeaders ihs_class = rInfo.getResourceClass().getAnnotation(InjectHeaders.class);
		if (ihs_class != null)
			addAllHeaders(responseContext, ihs_class.value());
		InjectHeaders ihs_method = rInfo.getResourceMethod().getAnnotation(InjectHeaders.class);
		if (ihs_method != null)
			addAllHeaders(responseContext, ihs_method.value());

	}

	private void addAllHeaders(ContainerResponseContext responseContext, HeaderField[] ihs) {
		for (HeaderField hf : ihs)
			responseContext.getHeaders().add(hf.name(), hf.value());
	}
}
