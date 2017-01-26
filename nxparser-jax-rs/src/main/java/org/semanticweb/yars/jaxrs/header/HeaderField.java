package org.semanticweb.yars.jaxrs.header;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
// requires Java 8: @Repeatable(value = InjectHeaders.class)
public @interface HeaderField {
	String name();
	String value();
}
