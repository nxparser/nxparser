package org.semanticweb.yars.turtle;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.RDFTestCase;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.NxParser;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Runs the W3C test cases against the Turtle-Parser. Loads them straight from
 * the Web. Only the approved, non-obsolete, non-entailment ones.
 * 
 * @author Tobias Kaefer
 * @author Aidan Hogan
 */
@RunWith(Parameterized.class)
public class TurtleTestSuite {
	
	static Collection<RDFTestCase> _testCases;
	
	public static URI _baseURI = null;

	private URI _uri = null;
	private URI _action = null;
	private URI _result = null;
	private String _name = null;
	@SuppressWarnings("unused")
	private String _comment = null;

	private Class<? extends RDFTestCase> _type = null;


	@Parameters(name = "{4} {1} : {2}")
	public static Collection<Object[]> getTestCaseQuadruples()
			throws IOException, URISyntaxException {
		
		init();

		Collection<Object[]> ret = new HashSet<Object[]>();
		
		ret.add(new Object[]{"a"});

		for (RDFTestCase tcq : _testCases)
			if (tcq instanceof RDFTestCase.TestCaseNegative)
				ret.add(new Object[] { tcq.getUri(), tcq.getName(),
						tcq.getComment(), tcq.getClass(), "-",  new URI[]{tcq.getAction()}});
			else if (tcq instanceof RDFTestCase.TestCaseEval)
				ret.add(new Object[] { tcq.getUri(), tcq.getName(),
						tcq.getComment(), tcq.getClass(), "E",  new URI[]{tcq.getAction(),
						((RDFTestCase.TestCaseEval) tcq).getResult()} });
			else
				ret.add(new Object[] { tcq.getUri(), tcq.getName(),
						tcq.getComment(), tcq.getClass(), "+",  new URI[]{tcq.getAction()}});

		return ret;
	}

	/**
	 * Creates a test.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public TurtleTestSuite(
			
			URI uri, String name, String comment,
			Class<? extends RDFTestCase> clazz, String ignored, URI[] uris)
			throws IOException, URISyntaxException {

		_uri = uri;
		_name = name;
		_type = clazz;
		_comment = comment;
		_action = uris[0];

		if (uris.length > 1)
			_result = uris[1];

	}

	public static void init() throws IOException, URISyntaxException {

		System.err.println("initialising");
		_testCases = new HashSet<org.semanticweb.yars.nx.RDFTestCase>();

		// reading in the normative document for the N-Quads 1.1 test cases.
		prepareCollections("http://www.w3.org/2013/TurtleTests/manifest.ttl");
	}

	@Test
	public void test() throws IOException, ParseException, InterruptedException {
		if (_result != null) {

			// test w/ gold standard

			if (_type.isAssignableFrom(RDFTestCase.TestCasePositive.class))
				System.err.println(" == Positive test: " + _action);
			else if (_type.isAssignableFrom(RDFTestCase.TestCaseNegative.class))
				System.err.println(" == Negative test: " + _action);
			else if (_type.isAssignableFrom(RDFTestCase.TestCaseEval.class))
				System.err.println(" == Eval test: " + _action);

			URL testDataURL = _action.toURL();
			URL goldStandardURL = _result.toURL();
			
			URLConnection urlc = testDataURL.openConnection();
			TurtleParser rxp = new TurtleParser();
			try {
				rxp.parse(urlc.getInputStream(), Charset.forName(urlc
						.getContentEncoding() == null ? "utf-8" : urlc
						.getContentEncoding()), _action);
			} catch (ParseException e) {
				System.err.println(" -- Failed to parse!" + e.getMessage());
				fail();
			} catch (TurtleParseException e) {
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

			NxParser nxp = new NxParser();
			nxp.parse(goldStandardURL.openStream());

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
					if (nx != null)
					System.err.println(" t- " + nx.toString());
					else
						System.err.println("Null Pointer!");
				}
				System.err.println(" -- Gold-standard data:");
				for (Nodes nx : goldStandard) {
					System.err.println(" g- " + nx.toString());
				}
			} else {
				System.err.println(" ++ Success");
			}

			assertTrue(sameAsGold);

		} else {

			// test w/o gold standard

			if (_type.isAssignableFrom(RDFTestCase.TestCasePositive.class))
				System.err.println(" == Positive test: " + _action);
			else if (_type.isAssignableFrom(RDFTestCase.TestCaseNegative.class))
				System.err.println(" == Negative test: " + _action);
			else if (_type.isAssignableFrom(RDFTestCase.TestCaseEval.class))
				System.err.println(" == Eval test: " + _action);

			URL testDataURL = _action.toURL();
			boolean failed = false;
			Exception exc = null;
			URLConnection urlc = testDataURL.openConnection();
			TurtleParserInternal rxp = new TurtleParserInternal(
					urlc.getInputStream(),
					urlc.getContentEncoding() == null ? "utf-8" : urlc
							.getContentEncoding());
			try {
				rxp.parse(new Callback() {
					@Override
					protected void startDocumentInternal() {;}

					@Override
					protected void endDocumentInternal() {;}

					@Override
					protected void processStatementInternal(Node[] nx) {
						if (nx.length != 3)
							throw new RuntimeException ("Expected a triple. Got something else.");
						}
					},
					
					_action);
				
			} catch (Exception e) {
				failed = true;
				exc = e;
			}

			if (_type.isAssignableFrom(RDFTestCase.TestCaseNegative.class))
				if (!failed)
					System.err
							.println(" -- Parsing should not have run through!");
				else
					System.err.println(" ++ Exception thrown correctly");
			else
				if (failed) {
					System.err
							.println(" -- Parsing should have run through!");

					// To have the exceptions in JUnit nicely presented:
					if (exc instanceof IOException)
						throw (IOException) exc;
					else if (exc instanceof ParseException)
						throw (ParseException) exc;
				else
						throw new RuntimeException(exc);
				} else
					System.err.println(" ++ Test Case handled correctly");
			if (_type.isAssignableFrom(RDFTestCase.TestCaseNegative.class))
				assertTrue(failed);
			else
				assertFalse(failed);
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
				+ "SELECT ?case ?name ?comment ?action ?result "
				+ "WHERE { ?case rdf:type rdft:TestTurtlePositiveSyntax ; "
				+ " rdft:approval rdft:Approved ;"
				+ " mf:name ?name ; "
				+ " rdfs:comment ?comment ;"
				+ " mf:action ?action ;"
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
			_testCases.add(new RDFTestCase.TestCasePositive(new URI(solution
					.getResource("case").getURI()), solution.getLiteral("name")
					.getLexicalForm(), solution.getLiteral("comment")
					.getLexicalForm(), new URI(solution.getResource("action")
					.getURI())));
		}
		
		exec.close();
		solution = null;
		results = null;
		

		// extract evaluation tests
		
		queryString = prefixes
				+ "SELECT ?case ?name ?comment ?action ?result "
				+ "WHERE { ?case rdf:type rdft:TestTurtleEval ; "
				+ " rdft:approval rdft:Approved ;"
				+ " mf:action ?action ;"
				+ " mf:result ?result ;"
				+ " rdfs:comment ?comment ;"
				+ " mf:name ?name. "
				+ " {SELECT ?case WHERE {?x rdf:type mf:Manifest. ?x mf:entries/rdf:rest*/rdf:first ?case. }}"
				+ "} ";
		query = QueryFactory.create(queryString);
		exec = QueryExecutionFactory.create(query, model);
		results = exec.execSelect();

		while (results.hasNext()) {
			solution = results.next();
			_testCases.add(new RDFTestCase.TestCaseEval(new URI(solution
					.getResource("case").getURI()), solution.getLiteral("name")
					.getLexicalForm(), solution.getLiteral("comment")
					.getLexicalForm(), new URI(solution.getResource("action")
					.getURI()),
					new URI(solution.getResource("result").getURI())));
		}
		
		
		// extract negative tests

		queryString = prefixes
				+ "SELECT ?case ?name ?comment ?action "
				+ "WHERE { ?case rdf:type rdft:TestTurtleNegativeSyntax ; "
				+ " rdft:approval rdft:Approved ;"
				+ " mf:action ?action ;"
				+ " rdfs:comment ?comment ;"
				+ " mf:name ?name. "
				+ " {SELECT ?case WHERE {?x rdf:type mf:Manifest. ?x mf:entries/rdf:rest*/rdf:first ?case. }}"
				+ "} ";
		query = QueryFactory.create(queryString);
		exec = QueryExecutionFactory.create(query, model);
		results = exec.execSelect();

		while (results.hasNext()) {
			solution = results.next();
			_testCases.add(new RDFTestCase.TestCaseNegative(new URI(solution
					.getResource("case").getURI()), solution.getLiteral("name")
					.getLexicalForm(), solution.getLiteral("comment")
					.getLexicalForm(), new URI(solution.getResource("action")
					.getURI())));
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
			sw.write(ns.toString());
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
