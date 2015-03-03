package org.semanticweb.yars.nx.dt.datetime;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;
import org.semanticweb.yars.nx.dt.DatatypeParseException;

public class XsdGMonthTest {

	@Test
	public void test() throws DatatypeParseException {

		// all the three following instructions are necessary to reproduce the
		// behaviour:
		GregorianCalendar gc = new GregorianCalendar(
				TimeZone.getTimeZone("Etc/UTC"));
		gc.clear();
		gc.set(Calendar.MONTH, 4);

		assertEquals(gc, new XsdGMonth("--05--").getValue());
	}

}
