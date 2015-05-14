package org.semanticweb.yars.nx.parser;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.Variable;

/**
 * 
 * Test cases for whitespace between RDF terms in triples.
 * 
 * @author Tobias Käfer
 *
 */
public class NxParserSpaceInTriplesTest {

	@Test
	public void testLiterals() throws ParseException {

		Node[] goldStandard = new Node[] {
				new Resource(
						"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey>",
						true),
				new Resource("<http://dbpedia.org/ontology/postalCode>", true),
				new Literal("\"07726-Englishtown\"@en", true) };

		String[] lines1 = new String[] {
				"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en.",
				"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en. ",
				"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en . ",
				"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en .",
				"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en .                   " };
		List<String> lines = Arrays
				.asList(new String[] {
						"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en.",
						"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en. ",
						"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en . ",
						"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en .",
						"<http://dbpedia.org/resource/Manalapan_Township,_New_Jersey> <http://dbpedia.org/ontology/postalCode> \"07726-Englishtown\"@en .                   " });
		lines = new LinkedList<String>(lines);
		for (String line : lines1)
			lines.add(line.replaceAll(" ", "\t"));

		for (String line : lines)
			assertArrayEquals(goldStandard, NxParser.parseNodesInternal(line));

		for (String line : lines)
			assertArrayEquals(goldStandard, NxParser.parseNodesInternal(line));
	}

	@Test
	public void testResources() throws ParseException {

		Node[] goldStandard;
		List<String> lines1;
		goldStandard = new Node[] { new Resource("<http://ex.org/s>", true),
				new Resource("<http://ex.org/p>", true),
				new Resource("<http://ex.org/o>", true) };
		lines1 = Arrays.asList(new String[] {
				"<http://ex.org/s> <http://ex.org/p> <http://ex.org/o> .",
				"<http://ex.org/s> <http://ex.org/p> <http://ex.org/o>.",
				"<http://ex.org/s> <http://ex.org/p> <http://ex.org/o>. " });

		List<String> lines = new LinkedList<String>(lines1);
		for (String line : lines1)
			lines.add(line.replaceAll(" ", "\t"));

		for (String line : lines)
			assertArrayEquals(goldStandard, NxParser.parseNodesInternal(line));

	}

	@Test
	public void testBNodes() throws ParseException {

		Node[] goldStandard;
		List<String> lines1;

		goldStandard = new Node[] { new Resource("<http://ex.org/s>", true),
				new Resource("<http://ex.org/p>", true),
				new BNode("_:bn123", true) };
		lines1 = Arrays.asList(new String[] {
				"<http://ex.org/s> <http://ex.org/p> _:bn123. ",
				"<http://ex.org/s> <http://ex.org/p> _:bn123 .",
				"<http://ex.org/s> <http://ex.org/p> _:bn123." });

		List<String> lines = new LinkedList<String>(lines1);
		for (String line : lines1)
			lines.add(line.replaceAll(" ", "\t"));

		for (String line : lines)
			assertArrayEquals(goldStandard, NxParser.parseNodesInternal(line));

	}

	@Test
	public void testVariables() throws ParseException {
		Node[] goldStandard;
		List<String> lines1;

		goldStandard = new Node[] { new Variable("?x", true),
				new Variable("?x", true), new Variable("?x", true) };
		lines1 = Arrays.asList(new String[] { "?x ?x ?x. ", "?x ?x ?x .",
				"?x ?x ?x." });

		List<String> lines = new LinkedList<String>(lines1);
		for (String line : lines1)
			lines.add(line.replaceAll(" ", "\t"));

		for (String line : lines)
			assertArrayEquals(goldStandard, NxParser.parseNodesInternal(line));

	}

}
