package org.semanticweb.yars.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.semanticweb.yars.nx.Node;

/**
 * An abstract {@link MessageBodyReader} and {@link MessageBodyWriter} for RDF
 * in NxParser's data model. Checks the {@link Iterable}s supplied and leaves
 * the implementation of the parsing to subclasses. Note that for writing
 * {@link Iterable}s of arrays of {@link Node}, which involves Generics, the
 * {@link Iterable}s have to be wrapped in an {@link GenericEntity}, see below.
 * This wrapping is required for Java to find out of what class the objects in
 * the {@link Iterable} are. Note that you can use any subclass of
 * {@link Iterable}, eg. Java's collections.
 * 
 * <p>
 * Example usage (writing RDF):<br />
 * {@code Iterable<Node[]> l = new ArrayList<Node[]>();}<br />
 * ...<br />
 * {@code GenericEntity<Iterable<Node[]>> ge = new GenericEntity<Iterable<Node[]>>(l); }
 * <br />
 * {@code return Response.ok(ge).build();}
 * </p>
 * 
 * @author Tobias Käfer
 *
 */
public abstract class AbstractRDFMessageBodyReaderWriter implements
		MessageBodyWriter<Iterable<Node[]>>,
		MessageBodyReader<Iterable<Node[]>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2,
			MediaType arg3) {
		boolean firstCheck = isReadableCheckMediatypeAndAnnotations(arg2, arg3);
		firstCheck &= arg0.isAssignableFrom(Iterable.class);
		if (firstCheck) {
			if (arg1 instanceof ParameterizedType) {
				for (Type t : ((ParameterizedType) arg1)
						.getActualTypeArguments()) {
					if (t instanceof Class
							&& ((Class) t).isAssignableFrom(Node[].class)) {
						return true;
					}
				}

			}
		}

		return false;

	}

	/**
	 * The method {@link #isReadable(Class, Type, Annotation[], MediaType)}
	 * checks the first two parameters, here the last two are to be checked.
	 * 
	 * @see {@link #isReadable(Class, Type, Annotation[], MediaType)}
	 */
	abstract boolean isReadableCheckMediatypeAndAnnotations(
			Annotation[] annotations, MediaType mt);

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2,
			MediaType arg3) {
		if (isWritableCheckMediatypeAndAnnotations(arg2, arg3))
			if (Iterable.class.isAssignableFrom(arg0)) {
				if (arg1 instanceof ParameterizedType) {
					for (Type t : ((ParameterizedType) arg1)
							.getActualTypeArguments()) {
						if (t instanceof Class
								&& Node[].class.isAssignableFrom((Class<?>) t)) {
							return true;
						}
					}
				}
			}
		return false;
	}

	/**
	 * The method {@link #isWriteable(Class, Type, Annotation[], MediaType)}
	 * checks the first two parameters, here the last two are to be checked.
	 * 
	 * @see {@link #isWriteable(Class, Type, Annotation[], MediaType)}
	 */
	abstract boolean isWritableCheckMediatypeAndAnnotations(Annotation[] arg2,
			MediaType arg3);

	@Override
	public long getSize(Iterable<Node[]> t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// As per the recommendation in the JavaDoc:
		return -1l;
	}

	static final Type NODE_ARRAY_ITERABLE_TYPE = new ParameterizedType() {

		final Type[] actualTypeArguments = new Type[] { Node[].class };

		@Override
		public Type[] getActualTypeArguments() {
			return actualTypeArguments;
		}

		@Override
		public Type getRawType() {
			return Iterable.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

	};

	/**
	 * Determines the {@link Charset} for a given {@link MediaType} defaulting
	 * to {@link StandardCharsets#UTF_8}.
	 * 
	 * @param m
	 *            The {@link MediaType} to be parsed
	 * @return The found {@link Charset}, or {@link StandardCharsets#UTF_8}.
	 */
	public static Charset getCharset(MediaType m) {
		if (m == null)
			return StandardCharsets.UTF_8;
		else {
			String cp = m.getParameters().get(MediaType.CHARSET_PARAMETER);
			if (cp == null)
				return StandardCharsets.UTF_8;
			else
				return Charset.forName(cp);

		}
	}

}
