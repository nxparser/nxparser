package org.semanticweb.yars.nx.dt.datetime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.parser.ParseException;

public class Iso8601Parser {	
	private static String NO_TOKEN = "\n";
	
	//Errors
	//0** Empty string/null passed
	//1** Premature end of string
	//2** Illegal token
	//3** Expecting end of string
	//4** Outside valid value range
	
	/**
	 * Parse an ISO8601 compliant datetime
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601DateTime(String s) throws DatatypeParseException {
		return parseISO8601DateTime(s, false);
	}
	
	/**
	 * Parse an ISO8601 compliant datetime or datetimestamp with required TZ
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601DateTime(String s, boolean tzRequired) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		String t = parseISO8601Date(s, cal);
		if(t==null){
			throw new DatatypeParseException("Premature end of lexical string, expecting token 'T'.",101);
		} else if(!t.startsWith("T")){
			throw new DatatypeParseException("Expecting token 'T', not '"+t+"'.",201);
		} 
		
		String tz = parseISO8601Time(t.substring(1), cal);
		if(tz==null){
			if(tzRequired){
				throw new DatatypeParseException("Premature end of lexical string, expecting token '.*(Z|(\\+|-)[0-9][0-9]:[0-9][0-9])'.",101);
			}
		} else{
			String nul = parseISO8601Timezone(tz, cal);
			if(nul!=null){
				throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",301);
			}
		}
		
		return cal;
	}
	
	
	/**
	 * Parse an ISO8601 compliant date
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601Date(String s) throws DatatypeParseException {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		String nul = parseISO8601Date(s, cal);
		if(nul!=null){
			throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",302);
		}
		return cal;
	}
	
	private static String parseISO8601Date(String s, GregorianCalendar cal) throws DatatypeParseException {
		String mmdd = parseISO8601Year(s, cal);
		if(mmdd==null || !mmdd.startsWith("-")){
			throw new DatatypeParseException("Expecting token '-' not '"+mmdd+"'.",202);
		}
		
		String dd = parseISO8601Month(mmdd.substring(1), cal);
		if(dd==null || !dd.startsWith("-")){
			throw new DatatypeParseException("Expecting token '-' not '"+dd+"'.",203);
		}
		
		String tz = parseISO8601Day(dd.substring(1), cal);
		if(tz==null)
			return null;
		else if(tz.startsWith("T"))
			return tz;
		else{
			String nul = parseISO8601Timezone(tz, cal);
			return nul;
		}
	}
	
	/**
	 * Parse an ISO8601 compliant gYearMonth
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601YearMonth(String s) throws DatatypeParseException {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		String nul = parseISO8601YearMonth(s, cal);
		if(nul!=null){
			throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",303);
		}
		return cal;
	}
	
	private static String parseISO8601YearMonth(String s, GregorianCalendar cal) throws DatatypeParseException{
		String mm = parseISO8601Year(s, cal);
		if(mm==null || !mm.startsWith("-")){
			throw new DatatypeParseException("Expecting token '-' not '"+mm+"'.",204);
		}
		
		String tz = parseISO8601Month(mm.substring(1), cal);
		if(tz==null)
			return null;
		else{
			String nul = parseISO8601Timezone(tz, cal);
			return nul;
		}
	}
	
	/**
	 * Parse an ISO8601 compliant gYear
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601Year(String s) throws DatatypeParseException {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		String tz = parseISO8601Year(s, cal);
		if(tz==null)
			return cal;
		else{
			String nul = parseISO8601Timezone(tz, cal);
			if(nul!=null)
				throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",304);
			return cal;
		}
	}
	
	private static String parseISO8601Year(String s, GregorianCalendar cal) throws DatatypeParseException {
		// parse YYYY[-+Z]
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		StringTokenizer t = new StringTokenizer(s, "-+Z", true);
		
		String tok = t.nextToken();
		if(tok.equals("-")){
			if(!t.hasMoreTokens()){
				throw new DatatypeParseException("Premature end of lexical string, expecting 'YYYY'.",102);
			}
			tok = t.nextToken();
			cal.set(GregorianCalendar.ERA, GregorianCalendar.BC);
		} else if(tok.equals("+")){
			if(!t.hasMoreTokens()){
				throw new DatatypeParseException("Premature end of lexical string, expecting 'YYYY'.",103);
			}
			tok = t.nextToken();
		}
		
		if(tok.length()<4){
			throw new DatatypeParseException("Illegal value for YYYY '"+tok+"'. Min length is 4.",223);
		} else if(tok.length()>4 && tok.startsWith("0")){
			throw new DatatypeParseException("Illegal value for YYYY '"+tok+"'. Leading zeros only allowed for YYYY length of 4.",223);
		}
		int year;
		try {
			year = Integer.parseInt(tok);
		} catch(NumberFormatException e){
			throw new DatatypeParseException("Illegal value for YYYY '"+tok+"'.",205);
		}
		cal.set(Calendar.YEAR, year);
		
		if(!t.hasMoreTokens())
			return null;
		return t.nextToken(NO_TOKEN);
	}
	
	/**
	 * Parse an ISO8601 compliant gYearMonth
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601MonthDay(String s) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		if(!s.startsWith("--")){
			throw new DatatypeParseException("Expecting opening token '--' not '"+s+"'.",206);
		}
		String nul = parseISO8601MonthDay(s.substring(2), cal);
		if(nul!=null){
			throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",305);
		}
		return cal;
	}
	
	private static String parseISO8601MonthDay(String s, GregorianCalendar cal) throws DatatypeParseException{
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		String dd = parseISO8601Month(s, cal);
		if(dd==null || !dd.startsWith("-")){
			throw new DatatypeParseException("Expecting token '-' not '"+dd+"'.",207);
		}
		
		String tz = parseISO8601Day(dd.substring(1), cal);
		if(tz==null)
			return null;
		else{
			String nul = parseISO8601Timezone(tz, cal);
			return nul;
		}
	}
	
	/**
	 * Parse an ISO8601 compliant gYear
	 * @param s Lexical string
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601Month(String s) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		if(!s.startsWith("--")){
			throw new DatatypeParseException("Expecting opening token '--' not '"+s+"'.",208);
		}
		if(!s.endsWith("--")){
			throw new DatatypeParseException("Expecting closing token '--' not '"+s+"'.",209);
		}
		
		String tz = parseISO8601Month(s.substring(2, s.length()-2), cal);
		if(tz==null)
			return cal;
		else{
			String nul = parseISO8601Timezone(tz, cal);
			if(nul!=null)
				throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",306);
			return cal;
		}
	}
	
	private static String parseISO8601Month(String s, GregorianCalendar cal) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		// parse MM[-+z]
		StringTokenizer t = new StringTokenizer(s, "-+Z", true);
		
		String tok = t.nextToken();
		int mon;
		try {
			mon = Integer.parseInt(tok);
		} catch(NumberFormatException e){
			throw new DatatypeParseException("Illegal value for MM: '"+tok+"'.",210);
		}
		if(mon>12 || mon<1){
			throw new DatatypeParseException("Illegal value for MM (>12): "+mon+".",401);
		} 
		cal.set(Calendar.MONTH, mon-1);
		
		if(!t.hasMoreTokens()){
			return null;
		}  	
		return t.nextToken(NO_TOKEN);
	}
	
	/**
	 * Parse an ISO8601 compliant gDay
	 * @param s Lexical string
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601Day(String s) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		
		if(!s.startsWith("---")){
			throw new DatatypeParseException("Expecting opening token '---' not '"+s+"'.",211);
		}
		
		String tz = parseISO8601Day(s.substring(3), cal);
		if(tz==null)
			return cal;
		else{
			String nul = parseISO8601Timezone(tz, cal);
			if(nul!=null)
				throw new DatatypeParseException("Expecting end of lexical string, not '"+nul+"'.",307);
			return cal;
		}
	}
	
	private static String parseISO8601Day(String s, GregorianCalendar cal) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		// parse DD[-+TZ]
		StringTokenizer t = new StringTokenizer(s, "-+TZ", true);
		
		String tok = t.nextToken();
		int day;
		try {
			day = Integer.parseInt(tok);
		} catch(NumberFormatException e){
			throw new DatatypeParseException("Illegal value for DD: '"+tok+"'.",212);
		}
		if(day>31 || day<0){
			throw new DatatypeParseException("Illegal value for DD (>31): "+day+".",402);
		} 
		
		if(cal.isSet(Calendar.YEAR)){
			if(!verifyDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, day))
				throw new DatatypeParseException("Illegal value for DD : "+day+" (YYYY = "+cal.get(Calendar.YEAR)+" MM = "+(cal.get(Calendar.MONTH)+1)+").",412);
		} else if(cal.isSet(Calendar.MONTH)){
			if(!verifyDay(cal.get(Calendar.MONTH)+1, day))
				throw new DatatypeParseException("Illegal value for DD : "+day+" (MM = "+(cal.get(Calendar.MONTH)+1)+").",413);
		}
		
		cal.set(Calendar.DATE, day);
		
		if(!t.hasMoreTokens())
			return null;
		return t.nextToken(NO_TOKEN);
	}
	
	/**
	 * Parse an ISO8601 compliant time
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static GregorianCalendar parseISO8601Time(String s) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
		cal.clear();
		String s1 = parseISO8601Time(s, cal);
		if(s1!=null)
			parseISO8601Timezone(s1, cal);
		return cal;
	}
	
	private static String parseISO8601Time(String s, GregorianCalendar cal) throws DatatypeParseException {
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		//parse HH:MM:SS.sss+hh:mm
		StringTokenizer t = new StringTokenizer(s, "-:.Z+", true);
		
		boolean allZero = false;

		String tok = t.nextToken();
		int hour;
		try {
			hour = Integer.parseInt(tok);
		} catch(NumberFormatException e){
			throw new DatatypeParseException("Illegal value for hh '"+tok+"'.",213);
		}
		if(hour>24 || hour<0){
			throw new DatatypeParseException("Illegal value for hh (>24) "+hour+".",403);
		} else if(hour==24){
			allZero = true;
		}
		cal.set(Calendar.HOUR_OF_DAY, hour);
			
		
		if(!t.hasMoreTokens()){
			throw new DatatypeParseException("Premature end of lexical string, expecting ':mm:ss'.",104);
		}  	
		tok = t.nextToken();
		
		if(!tok.equals(":")){
			throw new DatatypeParseException("Expecting token ':' not '"+tok+"'.",214);
		}
		
		if(!t.hasMoreTokens()){
			throw new DatatypeParseException("Premature end of lexical string, expecting 'mm:ss'.",105);
		}
		tok = t.nextToken();
			
		int min;
		try {
			min = Integer.parseInt(tok);
		} catch(NumberFormatException e){
			throw new DatatypeParseException("Illegal value for mm: '"+tok+"'.",215);
		}
		if(min>60 || min<0){
			throw new DatatypeParseException("Illegal value for mm (>60): "+min+".",404);
		} else if(allZero && min!=0){
			throw new DatatypeParseException("Illegal value for mm (>0): "+min+".",405);
		}
		cal.set(Calendar.MINUTE, min);
			
		if(!t.hasMoreTokens()){
			throw new DatatypeParseException("Premature end of lexical string, expecting ':ss'.",106);
		}  
		tok = t.nextToken();
		
		if(!tok.equals(":")){
			throw new DatatypeParseException("Expecting token ':' not '"+tok+"'.",216);
		}
		
		if(!t.hasMoreTokens()){
			throw new DatatypeParseException("Premature end of lexical string, expecting 'ss'.",107);
		}	
		tok = t.nextToken();
		
		int sec;
		try {
			sec = Integer.parseInt(tok);
		} catch(NumberFormatException e){
			throw new DatatypeParseException("Illegal value for ss: '"+tok+"'.",217);
		}
		if(sec>60 || sec<0){
			throw new DatatypeParseException("Illegal value for ss (>60): "+sec+".",406);
		} else if(allZero && sec!=0){
			throw new DatatypeParseException("Illegal value for ss (>0): "+sec+".",407);
		}
		cal.set(Calendar.SECOND, sec);
			
		if(!t.hasMoreTokens()){
			return null;
		}

		tok = t.nextToken();
		if (tok.equals(".")) {
			tok = t.nextToken();
			Double f;
			try {
				f = Double.parseDouble("."+tok);
			} catch(NumberFormatException e){
				throw new DatatypeParseException("Illegal value for ss: '"+tok+"'.",218);
			} 
			
			int msec = (int) (f*1000);
			if(allZero && msec!=0){
				throw new DatatypeParseException("Illegal value for ms (>0): "+msec+".",408);
			}
			cal.set(Calendar.MILLISECOND, msec);
			if(t.hasMoreTokens()){
				tok = t.nextToken();
			} else return null;
		}
		if(t.hasMoreTokens())	
			return tok+t.nextToken(NO_TOKEN);
		return tok;
	}
	
	private static String parseISO8601Timezone(String s, GregorianCalendar cal) throws DatatypeParseException {
		// parse HH:MM:SS.sss+HH:MM
		if(s==null || s.isEmpty())
			throw new DatatypeParseException("Null value passed.",0);
		
		StringTokenizer t = new StringTokenizer(s, "-:.Z+", true);
		
		String tok = t.nextToken();
		// check for TZ data
		if (!tok.equals("Z")){
			int sign = 0;
			if (tok.equals("+")) {
				sign = 1;
			} else if (tok.equals("-")) {
				sign = -1;
			} else {
				throw new DatatypeParseException("Excepting token '+' or '-' for timezone, not '"+tok+"'.",219);
			}
				
			if(!t.hasMoreTokens()){
				throw new DatatypeParseException("Premature end of lexical string, expecting 'hh:mm' for TZ.",108);
			}  
			tok = t.nextToken();
				
			int h;
			try {
				h = Integer.parseInt(tok);
			} catch(NumberFormatException e){
				throw new DatatypeParseException("Illegal value for TZ hh: '"+tok+"'.",220);
			}
			if(h>14){
				throw new DatatypeParseException("Illegal value for TZ hh (>14): "+h+".",409);
			}
				
			if(!t.hasMoreTokens()){
				throw new DatatypeParseException("Premature end of lexical string, expecting ':mm' for TZ.",109);
			}  
			tok = t.nextToken();
				
			if(!tok.equals(":")){
				throw new DatatypeParseException("Expecting token ':' not '"+tok+"'.",221);
			}
				
			if(!t.hasMoreTokens()){
				throw new DatatypeParseException("Premature end of lexical string, expecting 'mm' for TZ",110);
			}
			tok = t.nextToken();
				
			int m;
			try {
				m = Integer.parseInt(tok);
			} catch(NumberFormatException e){
				throw new DatatypeParseException("Illegal value for TZ mm: '"+tok+"'.",222);
			}
			if(m>60 || m<0){
				throw new DatatypeParseException("Illegal value for TZ mm (>60): "+m+".",410);
			} else if(h==14 && m!=0){
				throw new DatatypeParseException("Illegal value for TZ mm (>0,hh=14): "+m+".",411);
			}
				
			int offset = sign * ((h * 1000 * 3600) + (m * 1000 * 60));
			cal.set(Calendar.ZONE_OFFSET, offset);
				
		} else {
			cal.set(Calendar.ZONE_OFFSET, 0);
		}
		
		if(!t.hasMoreTokens())
			return null;
		return t.nextToken(NO_TOKEN);
	}
	
	public static String getCanonicalRepresentation(GregorianCalendar cal, boolean y, boolean m, boolean d, boolean t, boolean tz) {
		StringBuffer iso8601 = new StringBuffer();
		if(y && !m && d){
			throw new IllegalArgumentException("Must set MM if YY and DD are set");
		} else if((y!=m || y!=d) && t){
			throw new IllegalArgumentException("If set time, must set or unset all YY MM DD");
		} else if(t){
			GregorianCalendar cal2 = new GregorianCalendar(TimeZone.getTimeZone("Etc/UTC"));
			cal2.setTimeInMillis(cal.getTimeInMillis());
			cal = cal2;
		}
		
		if(y){
			int year = cal.get(Calendar.YEAR);
			if(cal.get(GregorianCalendar.ERA)==GregorianCalendar.BC){
				iso8601.append("-"+leadZeros(year,4));
			} else if(year>9999){
				iso8601.append("+"+year);
			} else{
				iso8601.append(leadZeros(year,4));
			}
			if(m){
				iso8601.append("-");
			}
		}
		if(m){
			if(!y){
				iso8601.append("--");
			}
			iso8601.append(leadZeros(cal.get(Calendar.MONTH)+1,2));
			if(d){
				iso8601.append("-");
			} else {
				iso8601.append("--");
			}
		}
		if(d){
			if(!m){
				iso8601.append("---");
			}
			iso8601.append(leadZeros(cal.get(Calendar.DATE),2));
			if(t)
				iso8601.append("T");
		}
		if(t){
			iso8601.append(leadZeros(cal.get(Calendar.HOUR_OF_DAY),2)+":");
			iso8601.append(leadZeros(cal.get(Calendar.MINUTE),2)+":");
			iso8601.append(leadZeros(cal.get(Calendar.SECOND),2));
			int milli = cal.get(Calendar.MILLISECOND);
			if(milli!=0){
				iso8601.append(XsdTime.removeTrailingZeros(Double.toString((double)milli/1000).substring(1)));
			}
		}
		if(tz && t){
			iso8601.append("Z");
		} else if(tz){
			int milli = cal.get(Calendar.ZONE_OFFSET);
			if(milli==0){
				iso8601.append("Z");
			}
			else iso8601.append(getHoursMinutes(milli));
		}
		
		return iso8601.toString();
	}
	
	public static String getHoursMinutes(int milli){
		int sign = 1;
		if(milli<0){
			sign = -1;
			milli *= -1;
		}
		int mins = milli/(60*1000);
		int hours = mins/60;
		mins = mins%60;
		if(sign==-1){
			return "-"+leadZeros(hours,2)+":"+leadZeros(mins,2);
		}else{
			return "+"+leadZeros(hours,2)+":"+leadZeros(mins,2);
		}
	}
	
	/**
	 * Add leading zeros
	 * @return
	 */
	public static String leadZeros(int v, int l){
		String val = Integer.toString(v);
		while(val.length()<l)
			val = "0"+val;
		
		return val;
	}
	
	/**
	 * Remove trailing zeros
	 * @return
	 */
	public static String removeTrailingZeros(String s){
		while(s.endsWith("0"))
			s = s.substring(0, s.length()-1);
		return s;
	}
	
	/**
	 * Quick testcase
	 * @return
	 * @throws ParseException
	 */
	public static void main(String args[]) throws DatatypeParseException{
		GregorianCalendar cl = parseISO8601Date("1242-04-14");
		System.err.println("1242-04-14");
		System.err.println(cl.get(Calendar.ERA));
		System.err.println(cl.get(Calendar.YEAR));
		System.err.println(cl.get(Calendar.MONTH));
		System.err.println(cl.get(Calendar.DATE));
		
		cl = parseISO8601Time("23:34:45.001Z");
		System.err.println("23:34:45.001Z");
		System.err.println(cl.get(Calendar.HOUR_OF_DAY));
		System.err.println(cl.get(Calendar.MINUTE));
		System.err.println(cl.get(Calendar.SECOND));
		System.err.println(cl.get(Calendar.MILLISECOND));
		System.err.println(cl.get(Calendar.ZONE_OFFSET));
		
		cl = parseISO8601DateTime("1242-04-14T23:34:45.2015+13:30");
		System.err.println("1242-04-14T23:34:45.2015+14:30");
		System.err.println(cl.get(Calendar.ERA));
		System.err.println(cl.get(Calendar.YEAR));
		System.err.println(cl.get(Calendar.MONTH));
		System.err.println(cl.get(Calendar.DATE));
		System.err.println(cl.get(Calendar.HOUR_OF_DAY));
		System.err.println(cl.get(Calendar.MINUTE));
		System.err.println(cl.get(Calendar.SECOND));
		System.err.println(cl.get(Calendar.MILLISECOND));
		System.err.println(cl.get(Calendar.ZONE_OFFSET));
		
		System.err.println(verifyDay(100,03,32));
		System.err.println(verifyDay(100,03,31));
		System.err.println(verifyDay(100,04,31));
		System.err.println(verifyDay(104,02,29));
		System.err.println(verifyDay(100,02,29));
		System.err.println(verifyDay(100,03,29));
		System.err.println(verifyDay(100,04,29));
		System.err.println(verifyDay(104,05,31));
	}
	
	/**
	 * 
	 * @param year
	 * @param month 1-12
	 * @param day
	 * @return
	 */
			
	public static boolean verifyDay(int year, int month, int day){
		if(!verifyDay(month, day)){
			return false;
		} else if(day==29){
			if(month==2){
				if(!(year%400==0 || (year%4==0 && year%100!=0)))
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param year
	 * @param month 1-12
	 * @param day
	 * @return
	 */	
	public static boolean verifyDay(int month, int day){
		if(!verifyDay(day))
			return false;
		else if(day==31){
			if(month!=1&&month!=3&&month!=5&&month!=7&&month!=8&&month!=10&&month!=12)
				return false;
		} else if(day==30){
			if(month==2)
				return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param year
	 * @param month 1-12
	 * @param day
	 * @return
	 */	
	public static boolean verifyDay(int day){
		if(day>31){
			return false;
		} 
		
		return true;
	}
}
