package org.semanticweb.yars.jaxrs;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.internal.spi.AutoDiscoverable;
import org.kohsuke.MetaInfServices;

/**
 * To automatically register the classes with Jersey.
 * 
 * @author Tobias Käfer
 *
 */
@MetaInfServices
public class JerseyAutoDiscoverable implements AutoDiscoverable {

	@Override
	public void configure(FeatureContext context) {
		Configuration config = context.getConfiguration();

		Class<?>[] classes = { NxMessageBodyReaderWriter.class,
				RdfXmlMessageBodyWriter.class, TurtleMessageBodyReader.class };

		for (Class<?> clazz : classes) {
			if (!config.isRegistered(clazz))
				context.register(clazz);
		}
	}
}
