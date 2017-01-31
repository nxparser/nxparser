package org.semanticweb.yars.parsers.rdfa.semargl;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

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

		// "http://schema.org/Person" would kill it
		String uri = "https://creativecommons.org/ns"; 
		URL u = new URL(uri);

		URLConnection connection = u.openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		sp.process(connection.getInputStream(), uri);

	}

	public static class DevNull extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}
}
