package org.semanticweb.yars.nx.dt;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.dt.datetime.XsdDateTime;
import org.semanticweb.yars.nx.namespace.XSD;
import org.semanticweb.yars.nx.parser.ParseException;

public class DatatypeFactoryTest extends DatatypeFactory {

	@Test
	public void test() throws ParseException {
		String dtstring = "2005-03-04T00:00:00";
		XsdDateTime datetime = new XsdDateTime(dtstring);

		assertEquals(datetime, getDatatype(dtstring, XSD.DATETIME));
		assertEquals(datetime, getDatatype(new Literal("\"" + dtstring + "\"^^"
				+ XSD.DATETIME, true)));

	}
}
