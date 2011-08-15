package org.semanticweb.yars.nx;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.semanticweb.yars.nx.parser.ParseException;

/**
 * @deprecated : Use org.semanticweb.yars.nx.dt package
 */
public class DateLiteral extends Literal {
	private static final long serialVersionUID = 1L;
	
	public static final Resource DATE = new Resource(XSD+"date");
	
	Date _date;
	
	public DateLiteral(String date) throws ParseException, NumberFormatException {
		super(date, DATE);
		
		_date = parseISO8601(date);
	}
	
	public Date getDate() {
		return _date;
	}
	
	public int compareTo(DateLiteral dtl){
		return toString().compareTo(dtl.toString());
	}
	
	public int hashCode(){
		return super.hashCode();
	}
	
	/**
	 * Parse dates as defined in http://www.w3.org/TR/NOTE-datetime}.
	 * This format (also specified in ISO8601) allows different "precisions".
	 * The following lower precision versions for the complete date 
	 * "2007-12-19T10:20:30.567+0300" are allowed:<br>
	 * "2007"<br>
	 * "2007-12"<br>
	 * "2007-12-19"<br>
	 * "2007-12-19T10:20+0300<br>
	 * "2007-12-19T10:20:30+0300<br>
	 * "2007-12-19T10:20:30.567+0300<br>
	 * Additionally a timezone offset of "+0000" can be substituted as "Z".<br>
	 * Parsing is done in a fuzzy way. If there is an illegal character somewhere in
	 * the String, the date parsed so far will be returned, e.g. the input
	 * "2007-12-19FOO" would return a date that represents "2007-12-19".
	 * 
	 * @param s
	 * @return
	 */
	public static Date parseISO8601(String s) throws ParseException {
		// split 2007-12-19T10:20:30.789+0500 into its parts
		// correct: yyyy['-'MM['-'dd['T'HH':'MM[':'ss['.'SSS]]('Z'|ZZZZZ)]]]
		StringTokenizer t = new StringTokenizer(s, "-T:.Z+", true);
		if (t == null || t.countTokens() == 0) {
			throw new ParseException("parseISO8601: Cannot parse '" + s + "'");
		}
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();

		try {
			// year
			String tok = t.nextToken();
			try {
				Integer year = Integer.parseInt(tok);
				if (year/2000 > 1) {
					throw new ParseException("parseISO8601: year seems bogus '" + year + "'");
				}
				cal.set(Calendar.YEAR, year);
			} catch (NumberFormatException nfe) {
				throw new ParseException("parseISO8601: Cannot parse '" + s + "'");
			}
			if (!t.hasMoreTokens()) {
				return cal.getTime();
			}
				
			// month
			if (t.nextToken().equals("-")) {
				cal.set(Calendar.MONTH, Integer.parseInt(t.nextToken()) - 1);
			} else {
				return cal.getTime();
			}
			
			if (!t.hasMoreTokens()) {
				return cal.getTime();
			}
			
			// day
			if (t.nextToken().equals("-")) {
				cal.set(Calendar.DAY_OF_MONTH, 
						Integer.parseInt(t.nextToken()));
			} else {
				return cal.getTime();
			}
			
			if (!t.hasMoreTokens()) {
				return cal.getTime();
			}
			
			// The standard says: if there is an hour there has to be a minute and a
			// timezone token, too.
			// hour
			if (t.nextToken().equals("T")) {
				int hour = Integer.parseInt(t.nextToken());
				// no error, got hours
				int min = 0;
				int sec = 0;
				int msec = 0;
				if (t.nextToken().equals(":")) {
					min = Integer.parseInt(t.nextToken());
					// no error, got minutes
					// need TZ or seconds
					String token = t.nextToken();
					if (token.equals(":")) {
						sec = Integer.parseInt(t.nextToken());
						// need millisecs or TZ
						token = t.nextToken();
						if (token.equals(".")) {
							msec = Integer.parseInt(t.nextToken());
							// need TZ
							token = t.nextToken();
						}
					}

					// check for TZ data
					int offset;
					if (token.equals("Z")) {
						offset = 0;
					} else {
						int sign = 0;
						if (token.equals("+")) {
							sign = 1;
						} else if (token.equals("-")) {
							sign = -1;
						} else {
							// no legal TZ offset found
							return cal.getTime();
						}
						offset = sign * Integer.parseInt(t.nextToken()) * 10 * 3600;
					}
					cal.set(Calendar.ZONE_OFFSET, offset);
				}
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, sec);
				cal.set(Calendar.MILLISECOND, msec);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}

		// in case we couldn't even parse a year
		if (!cal.isSet(Calendar.YEAR))
			throw new ParseException("parseISO8601: Cannot parse '" + s + "'");

		return cal.getTime();
	}
}
