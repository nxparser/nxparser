package org.semanticweb.yars.jaxrs.header;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderField {
	String name();
	String value();
}
