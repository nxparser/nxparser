package org.semanticweb.yars.parsers.external.json.jsonld_java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.semanticweb.yars.nx.Node;

import com.github.jsonldjava.core.DocumentLoader;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
/**
 * Parses JSON-LD.
 *
 * @author Tobias Käfer
 *
 */
public class JsonLDparser implements Iterable<Node[]> {

	JsonLDtripleCallback _callback;

	static final DocumentLoader _dl = new DocumentLoader();

	public void parse(final InputStream in, final String sourceURI)
			throws JsonLdError, IOException {
		_callback = new JsonLDtripleCallback(sourceURI);

		final JsonLdOptions options = new JsonLdOptions(sourceURI);

		options.setDocumentLoader(_dl);

		JsonLdProcessor
				.toRDF(JsonUtils.fromInputStream(in), _callback, options);
	}

	@Override
	public Iterator<Node[]> iterator() {
		return _callback.getNx().iterator();
	}

	public static DocumentLoader getDocumentLoader() {
		return _dl;
	}

}
