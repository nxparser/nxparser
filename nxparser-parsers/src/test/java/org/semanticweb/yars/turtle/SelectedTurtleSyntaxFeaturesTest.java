package org.semanticweb.yars.turtle;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.ParseException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SelectedTurtleSyntaxFeaturesTest {

	static Charset UTF_8 = Charset.forName("utf-8"); // StandardCharsets.UTF_8

	@Test
	public void test() throws IOException, TurtleParseException, org.semanticweb.yars.turtle.ParseException, URISyntaxException {
		String turtleString = "@prefix ex: <http://example.org/> .\n"
				+ "<> a ex:document , [ a ex:bla ] ; ex:lsdf ex:123.\n"
				+ "</213> ex:hasMembers ( ex:SnoopDogg ex:NateDogg ex:WarrenG ) .\n"
				+ "<todo> ex:hasList () .\n"
				+ "<> ex:label \"lskdfsdf\" \n"
				+ ", \"lskdfsdf\"@de-de  \n"
				+ ", \"lskdäääääfsdf\"^^ex:datatype , \n \"\"\"dsfsdf\nsdfsdfsdf\ndfsd sdfsdf\nsdfsdf\"\"\""
				+ " .\n";

		System.err.println("========= INPUT =============");
		System.err.println(turtleString);
		System.err.println("======= END INPUT ===========");

		String baseURI = "http://base.uri/";

		InputStream is = new ByteArrayInputStream(
				turtleString.getBytes(UTF_8));

		TurtleParser tp = new TurtleParser();
		tp.parse(is, UTF_8, new URI(baseURI));

		Model nxparserModel = createModelFromNodesCollection(tp);

		Model turtleModel = ModelFactory.createDefaultModel();

		is = new ByteArrayInputStream(
				turtleString.getBytes(UTF_8));
		turtleModel.read(is, baseURI, "TURTLE");

		assertTrue(nxparserModel.isIsomorphicWith(turtleModel));

	}

	public static Model createModelFromNodesCollection(Iterable<Node[]> cns)
			throws IOException {
		StringWriter sw = new StringWriter();

		Model modelTest = ModelFactory.createDefaultModel();

		for (Node[] ns : cns) {
			String s = Nodes.toString(new Node[] { ns[0], ns[1], ns[2] });
			System.err.println(s);
			sw.write(s);
			sw.write('\n');
		}
		sw.close();

		InputStream is2 = new ByteArrayInputStream(sw.getBuffer().toString()
				.getBytes(UTF_8));
		modelTest.read(is2, "", "N-TRIPLES");
		is2.close();

		return modelTest;

	}

	@Test
	public void testBnodeIds() throws ParseException, org.semanticweb.yars.turtle.ParseException, URISyntaxException {
		String turtleWithManyBnodes = "[] a [], [], [], [], [], [], [], [], [], [], [], [], [], []. [] a [a []] .";
		String baseURI = "http://ex.org/";

		InputStream is = new ByteArrayInputStream(
				turtleWithManyBnodes.getBytes(UTF_8));

		TurtleParser tp = new TurtleParser();
		tp.parse(is, UTF_8, new URI(baseURI));

		int i = 0;
		int j = 0;

		for (Node[] nx : tp) {
			++i;
			for (Node n : nx) {
				if (n instanceof BNode) {
					++j;
					BNode bn = (BNode) n;

					String[] parts = bn.parseContextualBNode();

					for (String s : parts) {
						char firstChar = s.charAt(0);
						assertFalse(
								"RDFXML does not like bnode labels with numbers in the beginning like " + bn.getLabel()
										+ " in triple #" + i + ": "
										+ Nodes.toString(nx), firstChar <= 57
										&& firstChar >= 48);

					}
				}
			}
		}
		System.err.println("tested " + j + " blank nodes in " + i + " triples.");

	}
}
