package org.semanticweb.yars.parsers.rdfa.semargl;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;
import org.semarglproject.rdf.NTriplesSerializer;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.rdfa.RdfaParser;
import org.semarglproject.sink.CharOutputSink;
import org.semarglproject.source.StreamProcessor;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class RDFaParserTest {

	@Test
	public void test() throws SAXException, ParseException, IOException, URISyntaxException {
		String uri = "https://creativecommons.org/ns" ; //"http://schema.org/Person";
		URL u = new URL(uri);

		URLConnection connection = u.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		
		RDFaParser rp = new RDFaParser(connection.getInputStream(), u.toURI());
		
		OutputStream os = System.out;
		os = new DevNull(); // comment out for output of the quads.

		final PrintWriter pw = new PrintWriter(os);

		final AtomicInteger count = new AtomicInteger(0);

		rp.parse(new Callback() {

			@Override
			protected void startDocumentInternal() {
				System.err.println("opening document...");
			}

			@Override
			protected void endDocumentInternal() {
				System.err.println("closing document...");
				
			}

			@Override
			protected void processStatementInternal(Node[] nx) {
				pw.println(Nodes.toString(nx));
				count.incrementAndGet();
			}	
			
		});

		pw.close();

		System.err.println("Quads processed: " + count);
		if (count.get() == 0)
			fail();
	}
	
	public void plainSemarglTest() throws ParseException, IOException {

		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.out);

		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(NTriplesSerializer.connect(cos)));

		// String uri = "http://schema.org/Person"; // would kill it
		String uri = "https://creativecommons.org/ns"; 
		URL u = new URL(uri);

		URLConnection connection = u.openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		sp.process(connection.getInputStream(), uri);

	}

	@Test
	public void testRdfa10Prefix() throws ParseException {
		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.out);

		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(NTriplesSerializer.connect(cos)));

		String html = ""//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.1//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd\">"
				+ "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML+RDFa 1.0//EN' 'http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd'>"
//				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml-rdfa-2.xsd\" lang=\"en\" xml:lang=\"en\">" //version=\"XHTML+RDFa 1.1\"
				+ "<html xmlns:wild=\"http://ex.org/wild#\">"
				+ "<body><div>A <a href=\"#Coin\" typeof=\"wild:Workflow\">Coin</a></div><div>A <a href=\"#property\" typeof=\"rdfs:Class\">property</a></div></body></html>";

		sp.process(new StringReader(html), "http://ex.org");

		System.err.println("only prefix wild should be expanded, and not prefix rdfs, as there are no well-known prefixes in RDFa 1.0");

	}

	@Test
	public void testRdfa11Prefix() throws ParseException {
		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.out);

		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(NTriplesSerializer.connect(cos)));

		String html = ""//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.1//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd\">"
//				+ "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML+RDFa 1.0//EN' 'http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd'>"
//				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml-rdfa-2.xsd\" lang=\"en\" xml:lang=\"en\">" //version=\"XHTML+RDFa 1.1\"
				+ "<html xmlns:wild=\"http://ex.org/wild#\">"
				+ "<body><div>A <a href=\"#Coin\" typeof=\"wild:Workflow\">Coin</a></div><div>A <a href=\"#property\" typeof=\"rdfs:Class\">property</a></div></body></html>";

		sp.process(new StringReader(html), "http://ex.org");

		System.err.println("both prefixes, wild and rdfs should get expanded in RDFa1.1");
	}

	@Test
	public void testundefPrefix() throws ParseException {
		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.out);

		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(NTriplesSerializer.connect(cos)));

		String html = ""//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.1//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd\">"
//				+ "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML+RDFa 1.0//EN' 'http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd'>"
//				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml-rdfa-2.xsd\" lang=\"en\" xml:lang=\"en\">" //version=\"XHTML+RDFa 1.1\"
				+ "<html xmlns:wild=\"http://ex.org/wild#\">"
				+ "<body><div>A <a href=\"#Coin\" typeof=\"wild:Workflow\">Coin</a></div><div>A <a href=\"#property\" typeof=\"rdfs:Class\">property</a></div></body></html>";

		sp.process(new StringReader(html), "http://ex.org");

		System.err.println("both prefixes, wild and rdfs should get expanded because RDFa1.1 processing is the default");
	}

	@Test
	public void testTagsoupPrefix() throws ParseException, SAXException {
		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.out);

		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(NTriplesSerializer.connect(cos)));

		XMLReader reader = null;

		SAXParserImpl parser = SAXParserImpl.newInstance(null);

		// If we encounter HTML5 problems, we may have to look into:
		// https://github.com/UniversityofWarwick/tagsoup-html5
		// parser.setProperty(Parser.schemaProperty, new HTML5Schema());
		reader = parser.getXMLReader();

		sp.setProperty(StreamProcessor.XML_READER_PROPERTY, reader);

		String html = ""//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.1//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd\">"
//				+ "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML+RDFa 1.0//EN' 'http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd'>"
//				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml-rdfa-2.xsd\" lang=\"en\" xml:lang=\"en\">" //version=\"XHTML+RDFa 1.1\"
				+ "<html prefix=\"wild: http://ex.org/wild#\">"
				+ "<body><div>A <a href=\"#Coin\" typeof=\"wild:Workflow\">Coin</a></div><div>A <a href=\"#property\" typeof=\"rdfs:Class\">property</a></div></body></html>";

		sp.process(new StringReader(html), "http://ex.org");

		System.err.println("both prefixes, wild and rdfs should get expanded because RDFa1.1 processing is the default");
	}

	@Test
	public void testTagsoupXmlns() throws ParseException, SAXException {
		CharOutputSink cos = new CharOutputSink();
		cos.connect(System.out);

		StreamProcessor sp = new StreamProcessor(RdfaParser.connect(NTriplesSerializer.connect(cos)));

		XMLReader reader = null;

		SAXParserImpl parser = SAXParserImpl.newInstance(null);

		// If we encounter HTML5 problems, we may have to look into:
		// https://github.com/UniversityofWarwick/tagsoup-html5
		// parser.setProperty(Parser.schemaProperty, new HTML5Schema());
		reader = parser.getXMLReader();

		sp.setProperty(StreamProcessor.XML_READER_PROPERTY, reader);

		String html = ""//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.1//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd\">"
//				+ "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML+RDFa 1.0//EN' 'http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd'>"
//				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml-rdfa-2.xsd\" lang=\"en\" xml:lang=\"en\">" //version=\"XHTML+RDFa 1.1\"
				+ "<html xmlns:wild=\"http://ex.org/wild#\">"
				+ "<body xmlns:wild=\"http://ex.org/wild2#\" wild:duck=\"bla\"><div xmlns:wild=\"http://ex.org/wild3#\">A <a xmlns:wild=\"http://ex.org/wild4#\" href=\"#Coin\" typeof=\"wild:Workflow\">Coin</a> <span href=\"#bla\" typeof=\"wild:something\"></span></div><div>A <a href=\"#property\" typeof=\"rdfs:Class\">property</a></div></body></html>";

		sp.process(new StringReader(html), "http://ex.org");

		System.err.println("both prefixes, wild and rdfs should get expanded because RDFa1.1 processing is the default");
	}

	public static class DevNull extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}
}
