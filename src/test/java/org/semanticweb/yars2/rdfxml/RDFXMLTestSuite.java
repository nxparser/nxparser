package org.semanticweb.yars2.rdfxml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Runs the W3C test cases agains the RDFXML-Parser. Loads them straight from
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

		URL url = new URL(
				"http://www.w3.org/2000/10/rdf-tests/rdfcore/all_files");

		InputStream is = url.openStream();

		_positiveTestCasesURIs = new HashMap<URI, URI>();
		_negativeTestCasesURIs = new HashSet<URI>();

		prepareCollections(is);
	}

	@Test
	public void test() throws IOException {
		if (_uris.length == 2 && _uris[1] != null) {

			// positive test
			// i.e. output should be equal to gold standard

			URL testDataURL = _uris[0].toURL();
			URL goldStandardURL = _uris[1].toURL();
			
			System.err.println(" == Positive test: "+_uris[0]+ " with n-triples at "+_uris[1]);

			InputStream is = testDataURL.openStream();
			RDFXMLParser rxp = null;
			try {
				rxp = new RDFXMLParser(is, _uris[0].toString());
			} catch (ParseException e) {
				System.err.println(" -- Failed to parse!"+e.getMessage());
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
			
			if(!sameAsGold){
				System.err.println(" -- Not the same as gold standard!");
				System.err.println(" -- Results data:");
				for(Nodes nx:testData){
					System.err.println(" t- "+nx.toN3());
				}
				System.err.println(" -- Gold-standard data:");
				for(Nodes nx:goldStandard){
					System.err.println(" g- "+nx.toN3());
				}
			} else{
				System.err.println(" ++ Success");
			}
			
			assertTrue(sameAsGold);

		} else if (_uris.length == 2 && _uris[1] == null) {

			// negative test
			// i.e. parser should officially fail
			
			System.err.println(" == Negative test: "+_uris[0]);

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
			
			if(!failed){
				System.err.println(" -- Parsing should not have run through!");
			} else{
				System.err.println(" ++ Exception thrown correctly");
			}
			
			assertTrue(failed);
		} else
			// shouldn't happen
			throw new IllegalArgumentException("wrong number of arguments");
	}

	private static void prepareCollections(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = null;
		String[] splits = null;
		String path = null;

		Set<String> positiveRdfFiles = new HashSet<String>();
		Set<String> positiveNtFiles = new HashSet<String>();

		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#") || line.equals(""))
				continue;

			splits = line.split("\t");

			if (splits[0].startsWith("entailment")
					|| splits[2].equals("NOT_APPROVED")
					|| (splits.length > 3 && splits[3].equals("NOT_APPROVED"))
					|| splits[2].startsWith("OBSOLETE")
					|| splits[2].startsWith("PENDING")
					|| splits[0].endsWith("html"))
				continue;

			path = splits[0];

			splits = path.split("/");

			if (splits[1].startsWith("error"))
				_negativeTestCasesURIs.add(baseURI.resolve(path));
			else if (splits[1].endsWith(".rdf"))
				positiveRdfFiles.add(path);
			else if (splits[1].endsWith(".nt"))
				positiveNtFiles.add(path);
			else
				System.err.println("didn't categorise " + path);

			splits = null;
		}

		for (String s : positiveRdfFiles) {
			String correspondingingNtFile = s.substring(0,
					s.length() - ".rdf".length())
					+ ".nt";
			if (positiveNtFiles.contains(correspondingingNtFile)) {
				_positiveTestCasesURIs.put(baseURI.resolve(s),
						baseURI.resolve(correspondingingNtFile));
				positiveNtFiles.remove(correspondingingNtFile);
			} else {
				System.err
						.println("didn't find a nt file for positive rdf file "
								+ s);
			}
		}

		for (String s : positiveNtFiles) {
			System.err.println("positive nt file " + s
					+ " has not been assigned its counterpart.");
		}

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
