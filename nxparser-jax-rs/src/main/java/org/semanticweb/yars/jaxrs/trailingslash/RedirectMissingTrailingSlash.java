package org.semanticweb.yars.jaxrs.trailingslash;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Annotation to cause the following behaviour:
 * The resource without a slash does not exist, but a resource with a slash
 * would. Assuming the requester meant the latter, we fail gently by answering
 * with a redirect to the resource with slash.
 */
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface RedirectMissingTrailingSlash {
}
