package org.semanticweb.yars.nx.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Runs the W3C test cases against the Parser. Loads them straight from
 * the Web.
 * 
 * @author Tobias Kaefer
 * @author Aidan Hogan
 * 
 */
@RunWith(Parameterized.class)
public class NTriplesSyntaxTestSuite extends TestCase {

	static Collection<TestCaseQuadruple> _testCases;

	public static URI baseURI;

	private URI _uri;
	private String _name;
	@SuppressWarnings("unused")
	private String _comment;
	private Boolean _isPositive;

	@Parameters(name = "{4} {1}: {2}")
	public static Collection<Object[]> getTestCaseQuadruples()
			throws IOException, URISyntaxException {
		init();

		Collection<Object[]> ret = new HashSet<Object[]>();

		for (TestCaseQuadruple tcq : _testCases)
			ret.add(new Object[] { tcq.getUri(), tcq.getName(),
					tcq.getComment(), tcq.isPositive(),
					tcq.isPositive() ? "+" : "-" });

		return ret;
	}

	/**
	 * Creates a test.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public NTriplesSyntaxTestSuite(URI uri, String name, String comment,
			Boolean isPositive, String ignored) throws IOException,
			URISyntaxException {

		_uri = uri;
		_name = name;
		_comment = comment;
		_isPositive = isPositive;

	}

	public static void init() throws IOException, URISyntaxException {

		_testCases = new HashSet<TestCaseQuadruple>();

		// reading in the normative document for the N-Triples 1.1 test cases.
		prepareCollections("http://www.w3.org/2013/N-TriplesTests/manifest.ttl");
	}

	@Test
	public void test() throws IOException {
		if (_isPositive) {

			// positive test
			// i.e. output should be equal to gold standard

			URL testDataURL = _uri.toURL();

			System.err.println(" == Positive test: " + _name + " from " + _uri);

			InputStream is = testDataURL.openStream();
			BufferedReader bw = new BufferedReader(new InputStreamReader(is));

			boolean failed = false;

			String line = null;
			while ((line = bw.readLine()) != null) {

				try {
					NxParser.parseNodes(line);
				} catch (ParseException e) {
					System.err
							.println(" -- Failed to parse! " + e.getMessage());
					failed = true;
				}
			}

			assertFalse(failed);
			if (!failed)
				System.err.println(" ++ Success");
		} else {

			// negative test
			// i.e. parser should officially fail

			InputStream is = _uri.toURL().openStream();
			BufferedReader bw = new BufferedReader(new InputStreamReader(is));

			boolean failed = false;

			String line = null;
			while ((line = bw.readLine()) != null) {

				try {
					NxParser.parseNodes(line);
				} catch (ParseException e) {
					failed = true;
				}
			}

			if (!failed) {
				System.err.println(" -- Parsing should not have run through!");
			} else {
				System.err.println(" ++ Exception thrown correctly");
			}

			assertTrue(failed);
		}

	}

	private static void prepareCollections(String s) throws IOException,
			URISyntaxException {

		// extract positive tests

		String prefixes = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
				+ "prefix rdft:   <http://www.w3.org/ns/rdftest#>  \n"
				+ "prefix mf: <http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#> \n";

		String queryString = prefixes
				+ "SELECT ?test ?name ?comment "
				+ "WHERE { ?case rdf:type rdft:TestNTriplesPositiveSyntax . "
				+ "?case mf:action ?test . ?case rdfs:comment ?comment. ?case mf:name ?name. "
				+ " {SELECT ?case WHERE {?x rdf:type mf:Manifest. ?x mf:entries/rdf:rest*/rdf:first ?case. }}"
				+ "} ";

		Query query = QueryFactory.create(queryString);

		Model model = ModelFactory.createDefaultModel();
		model.read(s);

		QueryExecution exec = QueryExecutionFactory.create(query, model);

		Iterator<QuerySolution> results = exec.execSelect();

		QuerySolution solution = null;
		while (results.hasNext()) {
			solution = results.next();
			_testCases.add(new TestCaseQuadruple(new URI(solution.getResource(
					"test").getURI()), solution.getLiteral("name")
					.getLexicalForm(), solution.getLiteral("comment")
					.getLexicalForm(), true));
		}

		exec.close();
		solution = null;
		results = null;

		// extract negative tests

		queryString = prefixes
				+ "SELECT ?test ?name ?comment "
				+ "WHERE { ?case rdf:type rdft:TestNTriplesNegativeSyntax . "
				+ "?case mf:action ?test . ?case rdfs:comment ?comment. ?case mf:name ?name. "
				+ " {SELECT ?case WHERE {?x rdf:type mf:Manifest. ?x mf:entries/rdf:rest*/rdf:first ?case. }}"
				+ "} ";
		query = QueryFactory.create(queryString);
		exec = QueryExecutionFactory.create(query, model);
		results = exec.execSelect();

		while (results.hasNext()) {
			solution = results.next();
			_testCases.add(new TestCaseQuadruple(new URI(solution.getResource(
					"test").getURI()), solution.getLiteral("name")
					.getLexicalForm(), solution.getLiteral("comment")
					.getLexicalForm(), false));
		}
		exec.close();
	}

	public static class DevNull extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}

	public static class TestCaseQuadruple {
		private URI _uri;
		private String _name;
		private String _comment;
		private Boolean _isPositive;

		public URI getUri() {
			return _uri;
		}

		public String getName() {
			return _name;
		}

		public String getComment() {
			return _comment;
		}

		public Boolean isPositive() {
			return _isPositive;
		}

		public TestCaseQuadruple(URI uri, String name, String comment,
				Boolean isPositive) {
			_uri = uri;
			_name = name;
			_comment = comment;
			_isPositive = isPositive;
		}

	}
}
