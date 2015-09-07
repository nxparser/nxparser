package org.semanticweb.yars.parsers.jsonld.semargl;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semarglproject.jsonld.JsonLdParser;
import org.semarglproject.rdf.NQuadsSerializer;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.sink.CharOutputSink;
import org.semarglproject.sink.DataSink;
import org.semarglproject.sink.QuadSink;
import org.semarglproject.source.StreamProcessor;

public class JsonLDParserTest {

	@Test
	public void test() throws ParseException {

		String s = "{"
				+ " \"@context\": \"http://schema.org/\", "
				+ "\"@id\":\"sdf\","
				+ " \"@type\": \"Person\", "
				+ " \"name\": \"Jane Doe\", "
				+ " \"jobTitle\": \"Professor\", "
				+ " \"telephone\": \"(425) 123-4567\", "
				+ " \"url\": \"http://www.janedoe.com\""
				+ "}";

		System.err.println("Testing using:");
		System.err.println(s);

		System.err.println();
		System.err.println("Result:");

		JsonLDParser jlp = new JsonLDParser();
		jlp.parse(new ByteArrayInputStream(s.getBytes()), "http://example.org/");

		for (Node[] nx:jlp) {
			System.err.println(Nodes.toString(nx));
		}

	}

	@Test
	public void testSemargl() throws ParseException {

		String s = "{"
				+ " \"@context\": \"http://schema.org/\", "
				//+ "\"@id\":\"sdf\","
				+ " \"@type\": \"Person\", "
				+ " \"name\": \"Jane Doe\", "
				+ " \"jobTitle\": \"Professor\", "
				+ " \"telephone\": \"(425) 123-4567\", "
				+ " \"url\": \"http://www.janedoe.com\""
				+ "}";

		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.err);

		StreamProcessor sp = new StreamProcessor(JsonLdParser.connect(NQuadsSerializer.connect(cos) ));

		sp.process(new ByteArrayInputStream(s.getBytes()), "http://ex.org/");

	}

	@Test
	public void test2() throws ParseException {
		// official test case http://json-ld.org/test-suite/tests/toRdf-0042-in.jsonld


		String s = "{  "
				+ "\"@context\": "
				//+ "{   "
				//+ "\"@base\":"
				+ "\"http://ex.org/\", "
				//+ "\"t1\": \"http://example.com/t1\",  "
				//+ "\"t2\": \"http://example.com/t2\",    "
				//+ "\"term1\": \"http://example.com/term1\",    "
				//+ "\"term2\": \"http://example.com/term2\",    "
				//+ "\"term3\": \"http://example.com/term3\",    "
				//+ "\"term4\": \"http://example.com/term4\",    "
				//+ "\"term5\": \"http://example.com/term5\"  "
				//+ "}"
				//+ ",  "
				+ "\"@id\": \"http://example.com/id1\",  \"@type\": \"t1\",  \"term1\": \"v1\",  \"term2\": {\"@value\": \"v2\", \"@type\": \"t2\"},  \"term3\": {\"@value\": \"v3\", \"@language\": \"en\"},  \"term4\": 4,  \"term5\": [50, 51]}";

		System.err.println("Testing using:");
		System.err.println(s);

		System.err.println();
		System.err.println("Result:");

		JsonLDParser jlp = new JsonLDParser();
		jlp.parse(new ByteArrayInputStream(s.getBytes()), "http://example.org/");

		for (Node[] nx:jlp) {
			System.err.println(Nodes.toString(nx));
		}

	}

}
