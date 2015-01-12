package org.semanticweb.yars.nx.dt;

import org.junit.Test;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.XSD;

public class DatatypeMapTest {

	@Test
	public void test() throws Exception {
		System.err.println(XsdDatatypeMap.areDisjoint(XSD.BOOLEAN, new Resource("http://www.w3.org/2001/XMLSchema#boolean")));
	}

}
