package org.semanticweb.yars.nx.dt.datetime;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdDateTimeStampTest {

	@Test
	public void test() throws DatatypeParseException {
		assertEquals("1600-02-29T07:17:55Z", new XsdDateTimeStamp(
				"1600-02-29T7:17:55Z").getCanonicalRepresentation());

	}
}
