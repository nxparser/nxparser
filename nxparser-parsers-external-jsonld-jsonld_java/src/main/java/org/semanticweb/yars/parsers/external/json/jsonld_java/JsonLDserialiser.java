package org.semanticweb.yars.parsers.external.json.jsonld_java;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.github.jsonldjava.core.Context;
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
	
	private JsonLDdocumentForm _documentForm;
	private Object _context;
	
	public static final JsonLDdocumentForm defaultJsonLDdocumentForm = JsonLDdocumentForm.flattened;
	private static Object defaultContextFromPrefixCC;

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
		_documentForm = defaultJsonLDdocumentForm;
		_context = null;
	}
	
	/**
	 * The JSON-LD specification defines three document forms:
	 * <a href="https://www.w3.org/TR/json-ld/#expanded-document-form">expanded
	 * </a>, <a href="https://www.w3.org/TR/json-ld/#flattened-document-form">
	 * flattened</a> and
	 * <a href="https://www.w3.org/TR/json-ld/#compacted-document-form">
	 * compacted</a>. To request a JSON-LD document in a form, the profile
	 * parameter in the HTTP Accept header can be used. Here we define the
	 * correspondence between the form and the profile URIs.
	 * 
	 * @author Tobias Käfer
	 */
	public enum JsonLDdocumentForm {
		expanded("http://www.w3.org/ns/json-ld#expanded"),
		flattened("http://www.w3.org/ns/json-ld#flattened"),
		compacted("http://www.w3.org/ns/json-ld#compacted");

		JsonLDdocumentForm(String uriString) {
			_uriString = uriString;
		}
		
		private final String _uriString;
		
		private static final Map<String, JsonLDdocumentForm> uristringsANDprofiles = new HashMap<String, JsonLDdocumentForm>();

		static {
			for (JsonLDdocumentForm jlp : JsonLDdocumentForm.values()) {
				uristringsANDprofiles.put(jlp.getUriString(), jlp);
			}
		}

		public String getUriString() {
			return _uriString;
		}
		
		public static boolean isJsonLDprofileUriString(String uriString) {
			return uristringsANDprofiles.containsKey(uriString);
		}
		
		public static JsonLDdocumentForm getJsonLDdocumentFormForProfileURIstring(String uriString) {
			return uristringsANDprofiles.get(uriString);
		}

	};
	
	public void setDocumentForm(JsonLDdocumentForm documentForm) {
		_documentForm = documentForm;
	}
	
	/**
	 * The jsonld-java code reads like that a context object can be (not
	 * exhaustive):
	 * <ul>
	 * <li>A <code>Map&lt;String, Map&gt;</code> object with a "@context" entry
	 * that contains context information, e.g. a remote context URI straight, or
	 * something like the following bullet</li>
	 * <li>A <code>Map&lt;String, String&gt;</code> containing the data from a
	 * JSON-LD context</li>
	 * </ul>
	 * 
	 * @param context The context you dare to supply
	 * @see https://www.w3.org/TR/json-ld/
	 * @see {@link JsonLdProcessor}
	 */
	public void setContext(Object context) {
		_context = context;
	}
	
	/**
	 * Get the current context. Defaults to a cached version of the context of
	 * <a href="http://prefix.cc">prefix.cc</a>.
	 * 
	 * @return the current context.
	 * @throws JsonLdError
	 *             in case something goes wrong when loading the cached context
	 */
	private Object getContext() throws JsonLdError {
		if (_context == null) {
			if (defaultContextFromPrefixCC == null) {
				log.log(Level.INFO, "Loading a cached version of the context provided by prefix.cc");
				Context ctx = new Context();
				ctx.remove("@base");
				ctx = ctx.parse("http://prefix.cc/context.jsonld");
				defaultContextFromPrefixCC = ctx;
			} 
			log.log(Level.FINE, "Using a cached version of the context provided by prefix.cc");
			_context = defaultContextFromPrefixCC;
		}
		return _context;
	}

	@Override
	protected void startDocumentInternal() {
		;
	}

	@Override
	protected void endDocumentInternal() {
		try {
			Object processor = JsonLdProcessor.fromRDF(_triples, _options, new NxParser2JsonLDjava());
			switch (_documentForm) {
			case expanded:
				JsonLdProcessor.expand(processor, _options);
				break;
			case compacted:
				processor = JsonLdProcessor.compact(processor, getContext(), _options);
				break;
			case flattened:
			default:
				processor = JsonLdProcessor.flatten(processor, getContext(), _options);
				break;
			}
			
			JsonUtils.writePrettyPrint(_writer, processor);
			//TODO: What should we do with the exceptions?
		} catch (JsonLdError e) {
			log.log(Level.WARNING, "An Error occurred:", e);
		} catch (JsonGenerationException e) {
			log.log(Level.WARNING, "An Error occurred:", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "An Error occurred:", e.getMessage());
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
							Resource dt = objectLit.getDatatype();
							result.addTriple(subject, nx[1].getLabel(), nx[2].getLabel(),
									dt == null ? null : dt.getLabel(), objectLit.getLanguageTag());
						} else {
							String object;
							if (nx[2] instanceof BNode)
								object = nx[2].toString();
							else
								object = nx[2].getLabel();
							result.addTriple(subject, nx[1].getLabel(), object);
						}
					} else {
						log.log(Level.SEVERE, "Did not get a Node[], but {0}", o.getClass());
						break;
					}
				}
			} else {
				log.log(Level.SEVERE, "Did not get an Iterable, but {0}", input.getClass());
			}
			return result;
		}
	}
}
