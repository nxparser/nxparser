package org.semanticweb.yars.parsers.rdfa.semargl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;
import org.semarglproject.rdf.ParseException;
import org.xml.sax.SAXException;

public class RDFaParserTest {

	@Test
	public void test() throws SAXException, ParseException, IOException, URISyntaxException {
		String uri = "http://schema.org/Person";
		URL u = new URL(uri);

		RDFaParser rp = new RDFaParser(u.openStream(), u.toURI());
		
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

	public static class DevNull extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}
}
