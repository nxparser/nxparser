package org.semanticweb.yars.parsers.jsonld.jsonld_java;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.parsers.external.json.jsonld_java.JsonLDparser;

import com.github.jsonldjava.core.JsonLdError;
/**
 * @author Tobias Käfer
 * @author Leonard Lausen
 *
 */
public class JsonLDParserTest {

	@Test
	public void test() throws JsonLdError, IOException, ParseException {

		String s = "{\n" +
				"  \"@context\": { \"foaf\":\"http://xmlns.com/foaf/0.1/\", \"name\":\"foaf:name\" },\n" +
				"  \"@type\": \"foaf:Person\",\n" +
				"  \"name\": \"John\"\n" +
				"}";

		System.err.println("Testing using:");
		System.err.println(s);

		System.err.println();
		System.err.println("Result:");

		JsonLDparser jlp = new JsonLDparser();
		jlp.parse(new ByteArrayInputStream(s.getBytes()), "http://example.org/");

		Collection<Nodes> actual = new HashSet<Nodes>();

		for (Node[] nx : jlp) {
			System.err.println(Nodes.toString(nx));

			// such that we don't need isomorphism check and can compare on the
			// rdf term level.
			actual.add(new Nodes(new Node[] { new BNode("b0"), nx[1], nx[2] }));
		}

		String[] goldStandardStrings = new String[] {
				// added xsd:string to the literals such that we can compare on
				// the rdf term level.
				"_:b0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .",
				"_:b0 <http://xmlns.com/foaf/0.1/name> \"John\"^^<http://www.w3.org/2001/XMLSchema#string> ." };

		System.err.println();
		System.err.println("Gold Standard:");
		Collection<Nodes> goldStandard = new HashSet<Nodes>();
		for (String gsString : goldStandardStrings) {
			Node[] nx = NxParser.parseNodes(gsString);
			Nodes ns = new Nodes(nx);
			goldStandard.add(ns);
			System.err.println(ns);
		}

		assertEquals(goldStandard, actual);

	}

	@Test
	public void test2() throws JsonLdError, IOException, ParseException {
		// official test case
		// http://json-ld.org/test-suite/reports/index.html#test_81b36000b509b1b4dd7fe3e4163b344d

		String s = "{\n  \"@context\": {\n    \"t1\": \"http://example.com/t1\",\n    \"t2\": \"http://example.com/t2\",\n    \"term1\": \"http://example.com/term1\",\n    \"term2\": \"http://example.com/term2\",\n    \"term3\": \"http://example.com/term3\",\n    \"term4\": \"http://example.com/term4\",\n    \"term5\": \"http://example.com/term5\"\n  },\n  \"@id\": \"http://example.com/id1\",\n  \"@type\": \"t1\",\n  \"term1\": \"v1\",\n  \"term2\": {\"@value\": \"v2\", \"@type\": \"t2\"},\n  \"term3\": {\"@value\": \"v3\", \"@language\": \"en\"},\n  \"term4\": 4,\n  \"term5\": [50, 51]\n}";

		JsonLDparser jlp = new JsonLDparser();
		jlp.parse(new ByteArrayInputStream(s.getBytes()), "http://example.org/");

		Collection<Nodes> actual = new HashSet<Nodes>();

		for (Node[] nx : jlp) {
			Nodes ns = new Nodes(new Node[] { nx[0], nx[1], nx[2] });
			actual.add(ns);
			System.err.println(ns);
		}

		Collection<Nodes> goldStandard = new HashSet<Nodes>();

		String[] goldStandardStrings = new String[] {
				// added xsd:string to the literals such that we can compare on
				// the rdf term level.
				"<http://example.com/id1> <http://example.com/term1> \"v1\"^^<http://www.w3.org/2001/XMLSchema#string> .",
				"<http://example.com/id1> <http://example.com/term2> \"v2\"^^<http://example.com/t2> .",
				"<http://example.com/id1> <http://example.com/term3> \"v3\"@en .",
				"<http://example.com/id1> <http://example.com/term4> \"4\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				"<http://example.com/id1> <http://example.com/term5> \"50\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				"<http://example.com/id1> <http://example.com/term5> \"51\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				"<http://example.com/id1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/t1> ." };

		for (String gsString : goldStandardStrings) {
			Node[] nx = NxParser.parseNodes(gsString);
			Nodes ns = new Nodes(nx);
			goldStandard.add(ns);
			System.err.println(ns);
		}

		assertEquals(goldStandard, actual);

	}

	@Test
	public void test3() throws JsonLdError, IOException, InitializationError {
		String s = "{  "
				+ "\"@context\": {\"s\":\"http://schema.org/\", \"term1\" : \"s:term1\", \"term2\" : \"s:term2\", \"term3\" : \"s:term3\", \"term4\" : \"s:term4\", \"term5\" : \"s:term5\"},"
				+ "\"@id\": \"http://schema.org/id1\",  "
				+ "\"@type\": \"t1\",  "
				+ "\"term1\": \"v1\",  "
				+ "\"term2\": {\"@value\": \"v2\", \"@type\": \"t2\"},  "
				+ "\"term3\": {\"@value\": \"v3\", \"@language\": \"en\"},  "
				+ "\"term4\": 4,  " + "\"term5\": [50, 51]}";

		String[] goldStandardStrings = new String[] {
				// added xsd:string to the literals such that we can compare on
				// the rdf term level.
				"<http://schema.org/id1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://schema.org/t1> <http://schema.org/> .",
				"<http://schema.org/id1> <http://schema.org/term1> \"v1\"^^<http://www.w3.org/2001/XMLSchema#string> <http://schema.org/> .",
				"<http://schema.org/id1> <http://schema.org/term2> \"v2\"^^<http://schema.org/t2> <http://schema.org/> .",
				"<http://schema.org/id1> <http://schema.org/term3> \"v3\"@en <http://schema.org/> .",
				"<http://schema.org/id1> <http://schema.org/term4> \"4\"^^<http://www.w3.org/2001/XMLSchema#integer> <http://schema.org/> .",
				"<http://schema.org/id1> <http://schema.org/term5> \"50\"^^<http://www.w3.org/2001/XMLSchema#integer> <http://schema.org/> .",
				"<http://schema.org/id1> <http://schema.org/term5> \"51\"^^<http://www.w3.org/2001/XMLSchema#integer> <http://schema.org/> ." };

		Collection<Nodes> goldStandard = new HashSet<Nodes>();
		for (String line : goldStandardStrings) {
			try {
				goldStandard.add(new Nodes(NxParser.parseNodes(line)));
			} catch (ParseException e) {
				throw new InitializationError(e);
			}
		}

		JsonLDparser jlp = new JsonLDparser();
		jlp.parse(new ByteArrayInputStream(s.getBytes()), "http://schema.org/");

		Collection<Nodes> actual = new HashSet<Nodes>();

		for (Node[] nx : jlp) {
			actual.add(new Nodes(nx));
		}

		assertEquals(goldStandard, actual);

	}

}
