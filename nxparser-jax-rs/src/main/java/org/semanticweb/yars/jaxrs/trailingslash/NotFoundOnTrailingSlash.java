package org.semanticweb.yars.jaxrs.trailingslash;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * The resource with trailing slash does not exist, but we can't tell this Jersey 2.
 */
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface NotFoundOnTrailingSlash {
}
