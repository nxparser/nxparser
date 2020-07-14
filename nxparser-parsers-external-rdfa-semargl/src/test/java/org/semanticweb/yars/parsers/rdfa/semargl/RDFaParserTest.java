package org.semanticweb.yars.parsers.rdfa.semargl;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
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

		RDFaParser rp = new RDFaParser();

		rp.parse(new ByteArrayInputStream(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\"\n" + 
				"    \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" + 
				"    xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n" + 
				"    version=\"XHTML+RDFa 1.0\" xml:lang=\"en\">\n" + 
				"  <body about=\"http://example.org/#me\">\n" + 
				"    <p>Hi, I am <span property=\"foaf:name\">John</span>.</p>\n" + 
				"  </body>\n" + 
				"</html>").getBytes("utf-8")), "http://example.org/");

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
