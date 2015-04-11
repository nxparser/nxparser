package org.semanticweb.yars.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.kohsuke.MetaInfServices;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.rdfxml.RdfXmlParser;

/**
 * A {@link MessageBodyReader} and {@link MessageBodyWriter} for <a
 * href="http://www.w3.org/TR/rdf-syntax-grammar/">RDF/XML</a>.
 * 
 * @author Tobias Käfer
 * @see AbstractRDFMessageBodyReaderWriter
 *
 */
@Provider
@Produces({ "application/rdf+xml", "application/xml" })
@Consumes({ "application/rdf+xml" })
@MetaInfServices({MessageBodyWriter.class, MessageBodyReader.class})
public class RdfXmlMessageBodyWriter extends AbstractRDFMessageBodyReaderWriter {
	final Logger _log = Logger.getLogger(RdfXmlMessageBodyWriter.class
			.getName());

	@Context
	UriInfo _uriinfo;

	XMLOutputFactory _factory = XMLOutputFactory.newInstance();
	{
		_factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,
				Boolean.TRUE);
	}

	@Override
	public void writeTo(Iterable<Node[]> t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		// inspired by Linked Data-Fu

		XMLStreamWriter xmlwriter;
		try {
			xmlwriter = _factory.createXMLStreamWriter(entityStream,
					StandardCharsets.UTF_8.name());

			xmlwriter.writeStartDocument("UTF-8", "1.0");
			xmlwriter.writeStartElement("rdf:RDF");
			xmlwriter.writeNamespace("rdf",
					"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		} catch (XMLStreamException e) {
			_log.log(Level.WARNING, e.getMessage());
			throw new ServerErrorException(500, e.getCause());
		}

		for (Node[] na : t) {
			// adapted from linked-data-fu
			if (!(na[1] instanceof Resource)) {
				continue;
			}
			if (na[0] instanceof Literal) {
				continue;
			}

			try {
				xmlwriter.writeStartElement("rdf:Description");
				if (na[0] instanceof Resource) {
					xmlwriter.writeAttribute("rdf:about", na[0].getLabel());
				} else if (na[0] instanceof BNode) {
					xmlwriter.writeAttribute("rdf:nodeID", na[0].getLabel());
				}
				String r = ((Resource) na[1]).getLabel();
				String namespace = null, localname = null;
				int i = r.indexOf('#');
				if (i > 0) {
					namespace = r.substring(0, i + 1);
					localname = r.substring(i + 1, r.length());
				} else {
					i = r.lastIndexOf('/');
					if (i > 0) {
						namespace = r.substring(0, i + 1);
						localname = r.substring(i + 1, r.length());
					}
				}
				if (namespace == null || localname == null) {
					_log.log(Level.INFO,
							"couldn't separate namespace and localname {0}",
							na[1]);
					continue;
				}
				xmlwriter.writeStartElement(namespace, localname);
				if (na[2] instanceof BNode) {
					xmlwriter.writeAttribute("rdf:nodeID", na[2].getLabel());
				} else if (na[2] instanceof Resource) {
					xmlwriter.writeAttribute("rdf:resource", na[2].getLabel());
				} else if (na[2] instanceof Literal) {
					Literal l = (Literal) na[2];

					if (l.getLanguageTag() != null) {
						xmlwriter
								.writeAttribute("xml:lang", l.getLanguageTag());
					} else if (l.getDatatype() != null) {
						xmlwriter.writeAttribute("rdf:datatype", l
								.getDatatype().getLabel());
					}

					try {
						xmlwriter.writeCharacters(l.getLabel());
					} catch (RuntimeException e) {
						_log.log(Level.WARNING, e.getMessage());
						throw new ServerErrorException(500, e.getCause());
					}

				}
				xmlwriter.writeEndElement();
				// rdf:Description
				xmlwriter.writeEndElement();
			} catch (XMLStreamException e) {
				_log.log(Level.WARNING, e.getMessage());
				throw new ServerErrorException(500, e.getCause());
			}
		}

		try {
			xmlwriter.writeEndElement();
			xmlwriter.writeEndDocument();
			xmlwriter.close();
		} catch (XMLStreamException e) {
			_log.log(Level.WARNING, e.getMessage());
			throw new ServerErrorException(500, e.getCause());
		}
	}

	@Override
	public Iterable<Node[]> readFrom(Class<Iterable<Node[]>> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		RdfXmlParser nxp = new RdfXmlParser();

		try {
			nxp.parse(entityStream, _uriinfo.getAbsolutePath().toString());
		} catch (ParseException e) {
			_log.log(Level.WARNING, e.getMessage());
			throw new BadRequestException(e.getCause());
		}
		return nxp;
	}

	@Override
	boolean isReadableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return RDF_XML_MEDIATYPE.isCompatible(mt);
	}

	@Override
	boolean isWritableCheckMediatypeAndAnnotations(Annotation[] annotations,
			MediaType mt) {
		return RDF_XML_MEDIATYPE.isCompatible(mt)
				|| MediaType.APPLICATION_XML_TYPE.isCompatible(mt);
	}

	public static final MediaType RDF_XML_MEDIATYPE = new MediaType(
			"application", "rdf+xml", StandardCharsets.UTF_8.name());
}
