package org.semanticweb.yars.nx;

import org.junit.Test;

public class BNodeTest {

	@Test
	public void test() throws Exception {
		String unescaped = "http://asdj.com/-xx42xxx/%20thing/";
		System.err.println(BNode.escapeForBNode(unescaped));
		System.err.println(BNode.unescapeForBNode(BNode.escapeForBNode(unescaped)));

		System.err.println(BNode.createBNode(unescaped,"xx78x"));
		for(String s: BNode.parseContextualBNode(BNode.createBNode(unescaped,"xx78x"))){
			System.err.println(s);
		}
	}

}
