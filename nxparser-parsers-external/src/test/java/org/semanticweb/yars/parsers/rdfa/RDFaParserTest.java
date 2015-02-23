package org.semanticweb.yars.parsers.rdfa;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semarglproject.rdf.ParseException;
import org.xml.sax.SAXException;

public class RDFaParserTest {

	@Test
	public void test() throws SAXException, ParseException, IOException {
		String uri = "http://schema.org/Person";
		URL u = new URL(uri);

		RDFaParser rp = new RDFaParser();

		rp.parse(u.openStream(), uri);

		int count = 0;

		OutputStream os = System.out;
		os = new DevNull(); // comment out for output of the quads.
		
		PrintWriter pw = new PrintWriter(os);
		
		for (Node[] nx : rp) {
			++count;
			pw.println(Nodes.toString(nx));
		}
		pw.close();
		
		System.err.println("Quads processed: " + count);
		if (count == 0)
			fail();
	}

	public static class DevNull extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}
}
