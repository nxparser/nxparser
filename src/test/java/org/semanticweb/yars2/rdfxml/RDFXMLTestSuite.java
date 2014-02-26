package org.semanticweb.yars2.rdfxml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Runs the W3C test cases against the RDFXML-Parser. Loads them straight from
 * the Web. Only the approved, non-obsolete, non-entailment ones.
 * 
 * @author Tobias Kaefer
 * 
 */
@RunWith(Parameterized.class)
public class RDFXMLTestSuite extends TestCase {

	static Map<URI, URI> _positiveTestCasesURIs;
	static Collection<URI> _negativeTestCasesURIs;

	public static URI baseURI;

	private URI[] _uris;

	@Parameters(name = "{0}")
	public static Collection<URI[]> getPositiveTestCasesURIs()
			throws IOException, URISyntaxException {
		init();

		List<URI[]> l = new LinkedList<URI[]>();
		for (Entry<URI, URI> e : _positiveTestCasesURIs.entrySet()) {
			l.add(new URI[] { e.getKey(), e.getValue() });
		}
		for (URI u : _negativeTestCasesURIs) {
			l.add(new URI[] { u, null });
		}
		return l;
	}

	/**
	 * Creates a test. Supply null for uri2 for negative parser test.
	 * 
	 * @param uri1
	 *            the URI of the test data
	 * @param uri2
	 *            the gold standard or null
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public RDFXMLTestSuite(URI uri1, URI uri2) throws IOException,
			URISyntaxException {
		_uris = new URI[] { uri1, uri2 };
	}

	public static void init() throws IOException, URISyntaxException {
		baseURI = new URI("http://www.w3.org/2000/10/rdf-tests/rdfcore/");

		_positiveTestCasesURIs = new HashMap<URI, URI>();
		_negativeTestCasesURIs = new HashSet<URI>();

		// reading in the normative document for the RDF/XML 1.0 test cases.
		prepareCollections("http://www.w3.org/2000/10/rdf-tests/rdfcore/Manifest.rdf");
	}

	@Test
	public void test() throws IOException {
		if (_uris.length == 2 && _uris[1] != null) {

			// positive test
			// i.e. output should be equal to gold standard

			URL testDataURL = _uris[0].toURL();
			URL goldStandardURL = _uris[1].toURL();

			System.err.println(" == Positive test: " + _uris[0]
					+ " with n-triples at " + _uris[1]);

			InputStream is = testDataURL.openStream();
			RDFXMLParser rxp = null;
			try {
				rxp = new RDFXMLParser(is, _uris[0].toString());
			} catch (ParseException e) {
				System.err.println(" -- Failed to parse!" + e.getMessage());
				fail();
			}

			boolean containsBnode = false;
			Collection<Nodes> testData = new HashSet<Nodes>();
			Collection<Nodes> goldStandard = new HashSet<Nodes>();

			for (Node[] nx : rxp) {
				if (nx[0] instanceof BNode || nx[2] instanceof BNode)
					containsBnode = true;
				testData.add(new Nodes(nx));
			}

			NxParser nxp = new NxParser(goldStandardURL.openStream());

			for (Node[] nx : nxp) {
				goldStandard.add(new Nodes(nx));
			}

			boolean sameAsGold = false;
			if (!containsBnode)
				sameAsGold = goldStandard.equals(testData);
			else
				sameAsGold = createModelFromNodesCollection(goldStandard)
						.isIsomorphicWith(
								createModelFromNodesCollection(testData));

			if (!sameAsGold) {
				System.err.println(" -- Not the same as gold standard!");
				System.err.println(" -- Results data:");
				for (Nodes nx : testData) {
					System.err.println(" t- " + nx.toN3());
				}
				System.err.println(" -- Gold-standard data:");
				for (Nodes nx : goldStandard) {
					System.err.println(" g- " + nx.toN3());
				}
			} else {
				System.err.println(" ++ Success");
			}

			assertTrue(sameAsGold);

		} else if (_uris.length == 2 && _uris[1] == null) {

			// negative test
			// i.e. parser should officially fail

			System.err.println(" == Negative test: " + _uris[0]);

			URL testDataURL = _uris[0].toURL();
			boolean failed = false;
			InputStream is = testDataURL.openStream();
			RDFXMLParser rxp = null;
			try {
				rxp = new RDFXMLParser(is, _uris[0].toString());
				PrintStream ps = new PrintStream(new DevNull());
				for (Node[] nx : rxp) {
					for (Node n : nx) {
						ps.print(n.toN3());
					}
				}
				ps.close();
			} catch (ParseException e) {
				failed = true;
			}
			failed = failed || !rxp.isSuccess();

			if (!failed) {
				System.err.println(" -- Parsing should not have run through!");
			} else {
				System.err.println(" ++ Exception thrown correctly");
			}

			assertTrue(failed);
		} else
			// shouldn't happen
			throw new IllegalArgumentException("wrong number of arguments");
	}

	private static void prepareCollections(String s) throws IOException,
			URISyntaxException {

		// extract positive tests

		String prefixes = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
				+ "prefix test: <http://www.w3.org/2000/10/rdf-tests/rdfcore/testSchema#> \n";

		String queryString = prefixes + "SELECT ?test ?result "
				+ "WHERE { ?case rdf:type test:PositiveParserTest . "
				+ "?case test:inputDocument ?test . "
				+ "?case test:status \"APPROVED\" . "
				+ "?case test:outputDocument ?result . } ";
		Query query = QueryFactory.create(queryString);

		Model model = ModelFactory.createDefaultModel();
		model.read(s);

		QueryExecution exec = QueryExecutionFactory.create(query, model);

		Iterator<QuerySolution> results = exec.execSelect();

		QuerySolution solution = null;
		Resource testResource, resultResource = null;
		while (results.hasNext()) {
			solution = results.next();
			testResource = solution.getResource("test");
			resultResource = solution.getResource("result");
			_positiveTestCasesURIs.put(new URI(testResource.getURI()), new URI(
					resultResource.getURI()));
		}

		exec.close();
		solution = null;
		testResource = null;
		resultResource = null;

		// extract negative tests

		queryString = prefixes + "SELECT ?test "
				+ "WHERE { ?case a test:NegativeParserTest . "
				+ "?case test:status \"APPROVED\" . "
				+ "?case test:inputDocument ?test . } ";
		query = QueryFactory.create(queryString);
		exec = QueryExecutionFactory.create(query, model);
		results = exec.execSelect();

		while (results.hasNext()) {
			solution = results.next();
			testResource = solution.getResource("test");
			_negativeTestCasesURIs.add(new URI(testResource.getURI()));
		}
		exec.close();
	}

	public static class DevNull extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}

	public static Model createModelFromNodesCollection(Collection<Nodes> cns)
			throws IOException {
		StringWriter sw = new StringWriter();

		Model modelTest = ModelFactory.createDefaultModel();

		for (Nodes ns : cns) {
			sw.write(ns.toN3());
			sw.write('\n');
		}
		sw.close();

		InputStream is2 = new ByteArrayInputStream(sw.getBuffer().toString()
				.getBytes());
		modelTest.read(is2, "", "N-TRIPLES");
		is2.close();

		return modelTest;

	}
}
