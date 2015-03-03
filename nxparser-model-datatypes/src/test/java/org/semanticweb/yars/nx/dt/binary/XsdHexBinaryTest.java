package org.semanticweb.yars.nx.dt.binary;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdHexBinaryTest {

	@Test
	public void test() throws DatatypeParseException {
		assertEquals("098ACBCDF087123D",
				new XsdHexBinary("098acbcDF087123D")
						.getCanonicalRepresentation());
	}

}
