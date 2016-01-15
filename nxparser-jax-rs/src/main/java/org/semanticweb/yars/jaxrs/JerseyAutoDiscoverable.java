package org.semanticweb.yars.jaxrs;

import java.util.Arrays;

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
				RdfXmlMessageBodyWriter.class, TurtleMessageBodyReader.class,
				JsonLdMessageBodyReader.class };

		for (Class<?> clazz : classes) {
			System.err.println("registering" + Arrays.toString(classes));
			if (!config.isRegistered(clazz))
				context.register(clazz);
		}
	}
}
