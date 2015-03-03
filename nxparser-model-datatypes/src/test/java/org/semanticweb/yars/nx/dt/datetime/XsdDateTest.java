package org.semanticweb.yars.nx.dt.datetime;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdDateTest {

	@Test
	public void test() throws DatatypeParseException {
		assertEquals("-1543-12-12-05:41",
				new XsdDate("-1543-12-12-5:41").getCanonicalRepresentation());
	}

}
