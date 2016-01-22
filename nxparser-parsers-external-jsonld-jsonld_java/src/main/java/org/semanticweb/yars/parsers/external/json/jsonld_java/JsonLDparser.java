package org.semanticweb.yars.parsers.external.json.jsonld_java;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.ErrorHandler;
import org.semanticweb.yars.nx.parser.RdfParser;

import com.github.jsonldjava.core.DocumentLoader;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.core.JsonLdTripleCallback;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.core.RDFDataset.Quad;
import com.github.jsonldjava.utils.JsonUtils;

/**
 * Parses JSON-LD.
 *
 * @author Tobias Käfer
 *
 */
public class JsonLDparser implements RdfParser {

	private static final Logger LOG = Logger.getLogger(JsonLDparser.class.getName());

	private static enum Contents {
		reader, inputstreamAndCharset, inputstream
	};

	final Object[] _inputData;
	final Contents _inputDataContents;

	private ErrorHandler _eh;

	static final DocumentLoader _dl = new DocumentLoader();

	private final JsonLdOptions _options;

	private final String _baseURIstring;

	public JsonLDparser(InputStream is, URI baseURI) {
		_options = new JsonLdOptions(baseURI.toString());
		_options.setDocumentLoader(_dl);
		_baseURIstring = baseURI.toString();
		_inputData = new Object[] { is };
		_inputDataContents = Contents.inputstream;
	}

	public JsonLDparser(InputStream is, Charset cs, URI baseURI) {
		_options = new JsonLdOptions(baseURI.toString());
		_options.setDocumentLoader(_dl);
		_baseURIstring = baseURI.toString();
		_inputData = new Object[] { is, cs };
		_inputDataContents = Contents.inputstreamAndCharset;
	}

	public JsonLDparser(Reader r, URI baseURI) {
		_options = new JsonLdOptions(baseURI.toString());
		_options.setDocumentLoader(_dl);
		_baseURIstring = baseURI.toString();
		_inputData = new Object[] { r };
		_inputDataContents = Contents.reader;
	}

	public void parse(final Callback cb) {

		cb.startDocument();

		final Object input;

		try {
			switch (_inputDataContents) {
			case reader:
				input = JsonUtils.fromReader((Reader) _inputData[0]);
				break;
			default:
				// default should never happen
			case inputstream:
				input = JsonUtils.fromInputStream((InputStream) _inputData[0]);
				break;
			case inputstreamAndCharset:
				input = JsonUtils.fromInputStream((InputStream) _inputData[0], ((Charset) _inputData[1]).name());
				break;
			}

			JsonLdProcessor.toRDF(input, new JsonLdTripleCallback() {

				@Override
				public Object call(RDFDataset dataset) {
					for (String graphName : dataset.graphNames()) {
						for (Quad quad : dataset.getQuads(graphName)) {
							cb.processStatement(
									new Node[] { jsonld_java_Node__to__nxparser_Node(quad.getSubject(), _baseURIstring),
											jsonld_java_Node__to__nxparser_Node(quad.getPredicate(), _baseURIstring),
											jsonld_java_Node__to__nxparser_Node(quad.getObject(), _baseURIstring) });
						}
					}
					return null;
				}

			}, _options);

		} catch (IOException e) {
			if (_eh != null) {
				_eh.warning(e);
			} else {
				LOG.warning(e.getMessage());
			}
		} catch (JsonLdError e) {
			if (_eh != null) {
				_eh.warning(e);
			} else {
				LOG.warning(e.getMessage());
			}
		} finally {
			cb.endDocument();
		}
	}

	public static DocumentLoader getDocumentLoader() {
		return _dl;
	}

	@Override
	public void setErrorHandler(ErrorHandler eh) {
		_eh = eh;
	}

	public static Node jsonld_java_Node__to__nxparser_Node(com.github.jsonldjava.core.RDFDataset.Node jsonldNode,
			String baseURIstring) {
		if (jsonldNode.isBlankNode()) {
			return BNode.createBNode(baseURIstring.toString(), jsonldNode.getValue());
		} else if (jsonldNode.isIRI()) {
			return new Resource(jsonldNode.getValue());
		} else if (jsonldNode.isLiteral()) {
			return new Literal(jsonldNode.getValue(), jsonldNode.getLanguage(),
					jsonldNode.getLanguage() == null ? new Resource(jsonldNode.getDatatype()) : null);
		}

		LOG.log(Level.WARNING, "Could not convert: {0}", new Object[] { jsonldNode });
		return null;
	}

}
