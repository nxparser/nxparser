package org.semanticweb.yars.turtle;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
/**
 * Turtle Parser test cases.
 * 
 * @author Tobias Käfer
 */
public class TurtleParserTest {

	@Test
	public void relativeURIandRDFtypeAndMultilineTest()
			throws TurtleParseException, ParseException, URISyntaxException {
		TurtleParser tp = new TurtleParser();
		tp.parse(
				new ByteArrayInputStream(("</a> a \"b\"^^<http://ex.org/> .\n"
						+ "</a> a \"b\"^^<http://ex.org/> .\n")
						.getBytes(StandardCharsets.UTF_8)),
				StandardCharsets.UTF_8, new URI("http://example.org/123/"));

		for (Node[] nx : tp) {
			Nodes nodes = new Nodes(nx);
			assertEquals(
					nodes,
					new Nodes(
							new Node[] {
									new Resource("<http://example.org/a>", true),
									new Resource(
											"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",
											true),
									new Literal("\"b\"^^<http://ex.org/>", true) }));
		}

	}

}
