package org.semanticweb.yars.jaxrs.header;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.NameBinding;

/**
 * Annotation to resources that are supposed to return an Accept-Post header.
 * Per default, the header ist returned for HTTP-GET and HTTP-OPTIONS request to
 * annotated resources.
 * 
 * @author Tobias Käfer
 * @see <a href="http://www.w3.org/TR/ldp/#header-accept-post">The Linked Data
 *      Platform specification on the Accept-Post header</a>
 *
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface AcceptPostNxPrdfSerialisations {
	String[] value() default { HttpMethod.OPTIONS, HttpMethod.GET };
}
