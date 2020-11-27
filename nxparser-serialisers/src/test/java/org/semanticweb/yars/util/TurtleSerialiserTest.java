package org.semanticweb.yars.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;

public class TurtleSerialiserTest {

	@Test
	public void test() {
		Node[][] nodes = new Node[][] {
				{ new Resource("http://ex.org/a"), new Resource("http://ex.org/hasList"), new BNode("bn1") },
				{ new BNode("bn1"), RDF.FIRST, new Resource("http://ex.org/element1") },
				{ new BNode("bn1"), RDF.REST, new BNode("bn2") },
				{ new BNode("bn2"), RDF.FIRST, new Resource("http://ex.org/element2") },
				{ new BNode("bn2"), RDF.REST, new BNode("bn3") },
				{ new BNode("bn3"), RDF.FIRST, new Resource("http://ex.org/element3") },
				{ new BNode("bn3"), RDF.REST, RDF.NIL } };
		TurtleSerialiser ts = new TurtleSerialiser(System.out);
		
		ts.startDocument();
		for (Node[]nx:nodes){
			ts.processStatement(nx);
		}
		ts.endDocument();
	}

}
