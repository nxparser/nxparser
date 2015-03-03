package org.semanticweb.yars.nx.dt.bool;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdBooleanTest {

	@Test
	public void positiveParsingTest() throws DatatypeParseException {
		new XsdBoolean("true");
		new XsdBoolean("false");
		new XsdBoolean("0");
		new XsdBoolean("1");
	}

	@Test(expected = DatatypeParseException.class)
	public void negativeParsingTest1() throws DatatypeParseException {
		new XsdBoolean("True");
	}

	@Test(expected = DatatypeParseException.class)
	public void negativeParsingTest2() throws DatatypeParseException {
		new XsdBoolean("False");
	}

	@Test
	public void canonicalisationTest() throws DatatypeParseException {
		assertEquals("true",
				new XsdBoolean("true").getCanonicalRepresentation());
		assertEquals("false",
				new XsdBoolean("false").getCanonicalRepresentation());
		assertEquals("true", new XsdBoolean("1").getCanonicalRepresentation());
		assertEquals("false", new XsdBoolean("0").getCanonicalRepresentation());
	}
}
