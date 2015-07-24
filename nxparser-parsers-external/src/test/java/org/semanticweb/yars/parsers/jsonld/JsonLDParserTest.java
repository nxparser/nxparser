package org.semanticweb.yars.parsers.jsonld;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;
import org.semarglproject.rdf.ParseException;

public class JsonLDParserTest {

	@Test
	public void test() throws ParseException {

		String s = "{" + " \"@context\": \"http://schema.org/\", "
				+ " \"@type\": \"Person\", " + " \"name\": \"Jane Doe\", "
				+ " \"jobTitle\": \"Professor\", "
				+ " \"telephone\": \"(425) 123-4567\", "
				+ " \"url\": \"http://www.janedoe.com\"" + "}";
		JsonLDParser jlp = new JsonLDParser();
		jlp.parse(new ByteArrayInputStream(s.getBytes()), "http://example.org/");
		
	}

}
