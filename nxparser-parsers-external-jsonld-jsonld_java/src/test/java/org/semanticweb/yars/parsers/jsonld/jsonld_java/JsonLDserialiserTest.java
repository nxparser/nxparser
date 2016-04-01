package org.semanticweb.yars.parsers.jsonld.jsonld_java;

import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.parsers.external.json.jsonld_java.JsonLDserialiser;
/**
 * @author Tobias Käfer
 *
 */
public class JsonLDserialiserTest {

	@Test
	public void test() throws URISyntaxException, ParseException {
		Writer w = new PrintWriter(System.err);
		Callback cb = new JsonLDserialiser(w, new URI("http://base.uri/"));
		
		cb.startDocument();
		
		String[] goldStandardStrings = new String[] {
				// added xsd:string to the literals such that we can compare on
				// the rdf term level.
				"_:b0 <http://schema.org/jobTitle> \"Professor\"^^<http://www.w3.org/2001/XMLSchema#string> .",
				"_:b0 <http://schema.org/name> \"Jane Doe\"^^<http://www.w3.org/2001/XMLSchema#string> .",
				"_:b0 <http://schema.org/telephone> \"(425) 123-4567\"^^<http://www.w3.org/2001/XMLSchema#string> .",
				"_:b0 <http://example.org/numberOfPhDs> \"5\"^^<http://www.w3.org/2001/XMLSchema#Integer> .",
				"_:b0 <http://schema.org/url> <http://www.janedoe.com> .",
				"_:b0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://schema.org/Person> ." ,
				"_:b1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://schema.org/Person> ."};

		for (String gsString : goldStandardStrings) {
			Node[] nx = NxParser.parseNodes(gsString);
			cb.processStatement(nx);
		}
		
		cb.endDocument();
		
		
	}

}
