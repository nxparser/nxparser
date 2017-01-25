package org.semanticweb.yars.jaxrs.header;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface InjectLinkHeader {
	String value();
}
