package org.semanticweb.yars.jaxrs.header;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

@Retention(RetentionPolicy.RUNTIME)
@NameBinding
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface InjectHeaders {
	HeaderField[] value();
}
