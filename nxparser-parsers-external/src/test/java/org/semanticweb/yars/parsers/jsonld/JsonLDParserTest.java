package org.semanticweb.yars.parsers.jsonld;

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

		String s = "{" + " \"@context\": \"http://schema.org/\", "
				+ " \"@type\": \"Person\", " 
				+ " \"name\": \"Jane Doe\", "
				+ " \"jobTitle\": \"Professor\", "
				+ " \"telephone\": \"(425) 123-4567\", "
				+ " \"url\": \"http://www.janedoe.com\"" + "}";
		
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
		
		String s = "{" + " \"@context\": \"http://schema.org/\", "
				+ " \"@type\": \"Person\", " 
				+ " \"name\": \"Jane Doe\", "
				+ " \"jobTitle\": \"Professor\", "
				+ " \"telephone\": \"(425) 123-4567\", "
				+ " \"url\": \"http://www.janedoe.com\"" + "}";
		
		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.err);
		
		StreamProcessor sp = new StreamProcessor(JsonLdParser.connect(NQuadsSerializer.connect(cos) ));
		
		sp.process(new ByteArrayInputStream(s.getBytes()), "http://ex.org/");
		
	}

}
