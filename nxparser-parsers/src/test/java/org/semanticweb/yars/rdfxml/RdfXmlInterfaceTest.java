package org.semanticweb.yars.rdfxml;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.InternalParserError;
import org.semanticweb.yars.nx.parser.ParseException;

public class RdfXmlInterfaceTest {

	String goldStandard = "<http://2ex.org/#me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> \"äölü\" .";
	String dataWithoutXmlProlog = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">"
			+ "<rdf:Description rdf:about=\"#me\">"
			+ "<rdf:type>äölü</rdf:type>"
			+ "</rdf:Description>" 
			+ "</rdf:RDF>";

	@Test
	public void matchingCharsetsTest() throws URISyntaxException, InterruptedException, InternalParserError, ParseException, IOException {
		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + dataWithoutXmlProlog;

		RdfXmlParser rxp = new RdfXmlParser(new ByteArrayInputStream(s.getBytes("UTF-8")), new URI("http://2ex.org/"));

		final AtomicInteger tripleCount = new AtomicInteger(0);

		Callback cb = new Callback() {

			@Override
			protected void startDocumentInternal() {

			}

			@Override
			protected void endDocumentInternal() {

			}

			@Override
			protected void processStatementInternal(Node[] nx) {
				tripleCount.incrementAndGet();
				assertEquals(goldStandard, Nodes.toString(nx));
			}

		};
		rxp.parse(cb);

		assertEquals(1, tripleCount.get());

	}

	@Test
	public void noEncodingInPrologProperEncodingInCodeTest()
			throws URISyntaxException, InterruptedException, InternalParserError, ParseException, IOException {
		String s = "<?xml version=\"1.0\" ?>" + dataWithoutXmlProlog;

		RdfXmlParser rxp = new RdfXmlParser(new ByteArrayInputStream(s.getBytes("UTF-8")), new URI("http://2ex.org/"));

		final AtomicInteger tripleCount = new AtomicInteger(0);

		Callback cb = new Callback() {

			@Override
			protected void startDocumentInternal() {

			}

			@Override
			protected void endDocumentInternal() {

			}

			@Override
			protected void processStatementInternal(Node[] nx) {
				tripleCount.incrementAndGet();
				assertEquals(goldStandard, Nodes.toString(nx));
			}

		};
		rxp.parse(cb);

		assertEquals(1, tripleCount.get());
	}

	@Ignore("known issue")
	@Test
	public void noEncodingInPrologImproperEncodingInCodeTest()
			throws URISyntaxException, InterruptedException, InternalParserError, ParseException, IOException {
		String s = "<?xml version=\"1.0\" ?>" + dataWithoutXmlProlog;

		RdfXmlParser rxp = new RdfXmlParser(new ByteArrayInputStream(s.getBytes("ISO-8859-1")),
				new URI("http://2ex.org/"));

		final AtomicInteger tripleCount = new AtomicInteger(0);

		Callback cb = new Callback() {

			@Override
			protected void startDocumentInternal() {

			}

			@Override
			protected void endDocumentInternal() {

			}

			@Override
			protected void processStatementInternal(Node[] nx) {
				tripleCount.incrementAndGet();
				assertEquals(goldStandard, Nodes.toString(nx));
			}

		};
		rxp.parse(cb);

		assertEquals(1, tripleCount.get());
	}

}
