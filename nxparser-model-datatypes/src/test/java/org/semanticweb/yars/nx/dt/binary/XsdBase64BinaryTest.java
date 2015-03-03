package org.semanticweb.yars.nx.dt.binary;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdBase64BinaryTest {

	@Test(expected = DatatypeParseException.class)
	public void testThatShouldFail() throws DatatypeParseException {

		new XsdBase64Binary("098a" + "bzxc" + "bvks" + "/ajg+"
				+ "r--cDF087123D");

	}

	@Test
	public void testThatShouldPass() throws DatatypeParseException {
		// bla Bla bLa blä ßls Ƀ£@
		assertNotNull(new XsdBase64Binary("YmxhIEJsYSBiTGEgYmzDpCDDn2xzIMmDwqNADQo="));
	}

}
