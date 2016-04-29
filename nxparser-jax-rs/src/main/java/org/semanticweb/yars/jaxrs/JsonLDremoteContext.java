package org.semanticweb.yars.jaxrs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used to specify remote contexts that will be used when serialising a
 * RDF graph to JSON-LD in a HTTP response. The inclined coder is pointed to the
 * <a href=
 * "http://github.com/jsonld-java/jsonld-java#loading-contexts-from-classpathjar">
 * remote context caching capabilities of jsonld_java</a>.
 * 
 * @author Tobias Käfer
 *
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonLDremoteContext {
	String[] value();
}
