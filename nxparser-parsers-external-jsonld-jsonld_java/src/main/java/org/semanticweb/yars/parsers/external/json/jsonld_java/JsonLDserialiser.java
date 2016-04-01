package org.semanticweb.yars.parsers.external.json.jsonld_java;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.core.RDFParser;
import com.github.jsonldjava.utils.JsonUtils;

/**
 * A serialiser from NxParser to JSON-LD.
 * 
 * @author Tobias Käfer
 *
 */

public class JsonLDserialiser extends Callback {

	static final Logger log = Logger.getLogger(JsonLDserialiser.class.getName());

	private final Writer _writer;
	private final List<Node[]> _triples;
	private final JsonLdOptions _options;

	public JsonLDserialiser(OutputStream os, URI baseURI) {
		this(os, Charset.forName("utf-8"), baseURI);
	}

	public JsonLDserialiser(OutputStream os, Charset cs, URI baseURI) {
		this(new OutputStreamWriter(os, cs), baseURI);
	}

	public JsonLDserialiser(Writer writer, URI baseURI) {
		_writer = writer;
		_triples = new LinkedList<Node[]>();

		_options = new JsonLdOptions(baseURI.toString());
	}

	@Override
	protected void startDocumentInternal() {
		;
	}

	@Override
	protected void endDocumentInternal() {
		try {
			Object processor = JsonLdProcessor.fromRDF(_triples, _options, new NxParser2JsonLDjava());
			processor = JsonLdProcessor.flatten(processor, _options);
			JsonUtils.writePrettyPrint(_writer, processor);
		} catch (JsonLdError e) {
			log.log(Level.WARNING, "An Error occurred: {}", e);
		} catch (JsonGenerationException e) {
			log.log(Level.WARNING, "An Error occurred: {}", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "An Error occurred: {}", e);
		}
	}

	@Override
	protected void processStatementInternal(Node[] nx) {
		_triples.add(nx);
	}

	public class NxParser2JsonLDjava implements RDFParser {

		@Override
		public RDFDataset parse(Object input) throws JsonLdError {
			RDFDataset result = new RDFDataset();

			if (input instanceof Iterable<?>) {
				Iterable<?> it = (Iterable<?>) input;
				for (Object o : it) {
					if (o instanceof Node[]) {
						Node[] nx = (Node[]) o;
						String subject;
						if (nx[0] instanceof BNode)
							subject = nx[0].toString();
						else
							subject = nx[0].getLabel();
						
						if (nx[2] instanceof Literal) {
							Literal objectLit = (Literal) nx[2];
							result.addTriple(subject, nx[1].getLabel(), nx[2].getLabel(),
									objectLit.getDatatype().getLabel(), objectLit.getLanguageTag());
						} else {
							String object;
							if (nx[2] instanceof BNode)
								object = nx[2].toString();
							else
								object = nx[2].getLabel();
							result.addTriple(subject, nx[1].getLabel(), object);
						}
					} else {
						log.log(Level.SEVERE, "Did not get a Node[], but {}", o.getClass());
						break;
					}
				}
			} else {
				log.log(Level.SEVERE, "Did not get an Iterable, but {}", input.getClass());
			}
			return result;
		}
	}
}
