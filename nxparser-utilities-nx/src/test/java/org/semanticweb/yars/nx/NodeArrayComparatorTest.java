package org.semanticweb.yars.nx;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeArrayComparatorTest {

	@Test
	public void test() {
		NodeArrayComparator.NodeArrayComparatorArgs nca = new NodeArrayComparator.NodeArrayComparatorArgs();

		nca.setNumeric(new boolean[] { true });

		NodeArrayComparator nc = new NodeArrayComparator(nca);

		int i = nc.compare(new Node[] { new Literal(
				"\"1\"^^<http://www.w3.org/2001/XMLSchema#int>", true) },
				new Node[] { new Literal(
						"\"2.0\"^^<http://www.w3.org/2001/XMLSchema#double>",
						true) });
		assertEquals((int) Math.signum(new Double(1).compareTo(new Double(2))),
				(int) Math.signum(i));

	}

}
