package org.semanticweb.yars.nx.dt.datetime;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdGDayTest {

	@Test
	public void test() throws DatatypeParseException {
		assertEquals("---12+05:00",
				new XsdGDay("---12+05:00").getCanonicalRepresentation());
	}

}
