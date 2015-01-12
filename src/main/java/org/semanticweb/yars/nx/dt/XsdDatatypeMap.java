package org.semanticweb.yars.nx.dt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.XSD;
import org.semanticweb.yars.nx.parser.ParseException;

/**
 * Map to represent subtypes of built-in datatypes
 * @author aidhog
 *
 */
public class XsdDatatypeMap {
	public static final HashMap<Resource, DatatypeElement> MAP = new HashMap<Resource, DatatypeElement>();
	static{
		DatatypeElement anyuri = new DatatypeElement(XSD.ANYURI, null);
		MAP.put(XSD.ANYURI, anyuri);
		
		DatatypeElement base64binary = new DatatypeElement(XSD.BASE64BINARY, null);
		MAP.put(XSD.BASE64BINARY, base64binary);
		
		DatatypeElement booleaN = new DatatypeElement(XSD.BOOLEAN, null);
		MAP.put(XSD.BOOLEAN, booleaN);
		
		DatatypeElement date = new DatatypeElement(XSD.DATE, null);
		MAP.put(XSD.DATE, date);
		
		DatatypeElement datetime = new DatatypeElement(XSD.DATETIME, null);
		MAP.put(XSD.DATETIME, datetime);
			DatatypeElement datetimestamp = new DatatypeElement(XSD.DATETIMESTAMP, datetime);
			MAP.put(XSD.DATETIMESTAMP, datetimestamp);
			datetime.addSubtype(datetimestamp);
		
		DatatypeElement decimal = new DatatypeElement(XSD.DECIMAL, null);
		MAP.put(XSD.DECIMAL, decimal);
			DatatypeElement integer = new DatatypeElement(XSD.INTEGER, decimal);
			MAP.put(XSD.INTEGER, integer);
			decimal.addSubtype(integer);
				DatatypeElement lonG = new DatatypeElement(XSD.LONG, decimal);
				MAP.put(XSD.LONG, lonG);
				integer.addSubtype(lonG);
					DatatypeElement inT= new DatatypeElement(XSD.INT, decimal);
					MAP.put(XSD.INT, inT);
					lonG.addSubtype(inT);
						DatatypeElement shorT= new DatatypeElement(XSD.SHORT, decimal);
						MAP.put(XSD.SHORT, shorT);
						inT.addSubtype(shorT);
							DatatypeElement bytE= new DatatypeElement(XSD.BYTE, decimal);
							MAP.put(XSD.BYTE, bytE);
							shorT.addSubtype(bytE);
				DatatypeElement nonnegativeinteger = new DatatypeElement(XSD.NONNEGATIVEINTEGER, decimal);
				MAP.put(XSD.NONNEGATIVEINTEGER, nonnegativeinteger);
				integer.addSubtype(nonnegativeinteger);
					DatatypeElement positiveinteger = new DatatypeElement(XSD.POSITIVEINTEGER, decimal);
					MAP.put(XSD.POSITIVEINTEGER, positiveinteger);
					nonnegativeinteger.addSubtype(positiveinteger);
					DatatypeElement unsignedlong = new DatatypeElement(XSD.UNSIGNEDLONG, decimal);
					MAP.put(XSD.UNSIGNEDLONG, unsignedlong);
					nonnegativeinteger.addSubtype(unsignedlong);
						DatatypeElement unsignedint = new DatatypeElement(XSD.UNSIGNEDINT, decimal);
						MAP.put(XSD.UNSIGNEDINT, unsignedint);
						unsignedlong.addSubtype(unsignedint);
							DatatypeElement unsignedshort = new DatatypeElement(XSD.UNSIGNEDSHORT, decimal);
							MAP.put(XSD.UNSIGNEDSHORT, unsignedshort);
							unsignedint.addSubtype(unsignedshort);
								DatatypeElement unsignedbyte = new DatatypeElement(XSD.UNSIGNEDBYTE, decimal);
								MAP.put(XSD.UNSIGNEDBYTE, unsignedbyte);
								unsignedshort.addSubtype(unsignedbyte);
				DatatypeElement nonPositiveInteger = new DatatypeElement(XSD.NONPOSITIVEINTEGER, decimal);
				MAP.put(XSD.NONPOSITIVEINTEGER, nonPositiveInteger);
				integer.addSubtype(nonPositiveInteger);
					DatatypeElement negativeinteger = new DatatypeElement(XSD.NEGATIVEINTEGER, decimal);
					MAP.put(XSD.NEGATIVEINTEGER, negativeinteger);
					nonPositiveInteger.addSubtype(negativeinteger);
		
		DatatypeElement floaT = new DatatypeElement(XSD.FLOAT, null);
		MAP.put(XSD.FLOAT, floaT);
		
		DatatypeElement gday = new DatatypeElement(XSD.GDAY, null);
		MAP.put(XSD.GDAY, gday);
		
		DatatypeElement gmonth = new DatatypeElement(XSD.GMONTH, null);
		MAP.put(XSD.GMONTH, gmonth);
		
		DatatypeElement gmonthday = new DatatypeElement(XSD.GMONTHDAY, null);
		MAP.put(XSD.GMONTHDAY, gmonthday);
		
		DatatypeElement gyear = new DatatypeElement(XSD.GYEAR, null);
		MAP.put(XSD.GYEAR, gyear);
		
		DatatypeElement gyearmonth = new DatatypeElement(XSD.GYEARMONTH, null);
		MAP.put(XSD.GYEARMONTH, gyearmonth);
		
		DatatypeElement hexbinary = new DatatypeElement(XSD.HEXBINARY, null);
		MAP.put(XSD.HEXBINARY, hexbinary);
		
//		DatatypeElement notation = new DatatypeElement(XSD.NOTATION, null);
//		MAP.put(XSD.NOTATION, notation);
		
//		DatatypeElement precisiondecimal = new DatatypeElement(XSD.PRECISIONDECIMAL, null);
//		MAP.put(XSD.PRECISIONDECIMAL, precisiondecimal);
		
//		DatatypeElement qname = new DatatypeElement(XSD.QNAME, null);
//		MAP.put(XSD.QNAME, qname);
		
		DatatypeElement string = new DatatypeElement(XSD.STRING, null);
		MAP.put(XSD.STRING, string);
			DatatypeElement normalizedstring = new DatatypeElement(XSD.NORMALIZEDSTRING, string);
			MAP.put(XSD.NORMALIZEDSTRING, string);
			string.addSubtype(normalizedstring);
				DatatypeElement token = new DatatypeElement(XSD.TOKEN, string);
				MAP.put(XSD.TOKEN, token);
				normalizedstring.addSubtype(token);
					DatatypeElement language = new DatatypeElement(XSD.LANGUAGE, string);
					MAP.put(XSD.LANGUAGE, language);
					token.addSubtype(language);
					DatatypeElement name = new DatatypeElement(XSD.NAME, string);
					MAP.put(XSD.NAME, name);
					token.addSubtype(name);
						DatatypeElement ncname = new DatatypeElement(XSD.NCNAME, string);
						MAP.put(XSD.NCNAME, ncname);
						name.addSubtype(ncname);
//							DatatypeElement entity = new DatatypeElement(XSD.ENTITY, string);
//							MAP.put(XSD.ENTITY, entity);
//							ncname.addSubtype(entity);
//							DatatypeElement id = new DatatypeElement(XSD.ID, string);
//							MAP.put(XSD.ID, id);
//							ncname.addSubtype(id);
//							DatatypeElement idref = new DatatypeElement(XSD.IDREF, string);
//							MAP.put(XSD.IDREF, idref);
//							ncname.addSubtype(idref);
					DatatypeElement nmtoken = new DatatypeElement(XSD.NMTOKEN, string);
					MAP.put(XSD.NMTOKEN, nmtoken);
					token.addSubtype(nmtoken);
					
		DatatypeElement time = new DatatypeElement(XSD.TIME, null);
		MAP.put(XSD.TIME, time);
	}
	
	public static boolean isSubtype(Resource sup, Resource sub)
			throws UnsupportedDatatypeException {
		DatatypeElement supde = MAP.get(sup);
		DatatypeElement subde = MAP.get(sub);
		if (supde == null) {
			throw new UnsupportedDatatypeException("Unsupported datatype "
					+ sup);
		}
		if (subde == null) {
			throw new UnsupportedDatatypeException("Unsupported datatype "
					+ sup);
		}
		return DatatypeElement.isSubtype(supde, subde);
	}

	public static Resource getPrimitive(Resource dt) {
		DatatypeElement dte = MAP.get(dt);
		if (dte == null) {
			return dt;
		}
		return dte.getPrimitive().getURI();
	}

	/**
	 * Get the canonicalised version of the literal
	 * 
	 * @return
	 */
	public static Literal getCanonicalLiteral(Literal l) throws DatatypeParseException, ParseException {
		if (l.getDatatype() != null
				&& Datatype.isSupportedStandardDatatype(l.getDatatype())) {
			Resource primitiveDT = XsdDatatypeMap.getPrimitive(l.getDatatype());

			if (primitiveDT == null)
				return l;

			Datatype<?> d = DatatypeFactory.getDatatype(l.toString(),
					primitiveDT);

			if (d == null) {
				return l;
			}

			String canon = d.getCanonicalRepresentation();

			if (!canon.equals(l.toString())
					|| !primitiveDT.equals(l.getDatatype())) {
				return new Literal(canon, primitiveDT);
			}
		}
		
		return l;
	}

	public static boolean areDisjoint(Resource dt1, Resource dt2)
			throws UnsupportedDatatypeException {
		 if (dt1 == null && XSD.STRING.equals(dt2)) {
			return false;
		} else if (dt2 == null && XSD.STRING.equals(dt1)) {
			return false;
		} if (dt1.equals(dt2)) {
			return false;
		}

		DatatypeElement de1 = MAP.get(dt1);
		DatatypeElement de2 = MAP.get(dt2);
		if (de1 == null) {
			throw new UnsupportedDatatypeException("Unsupported datatype "
					+ dt1);
		}
		if (de2 == null) {
			throw new UnsupportedDatatypeException("Unsupported datatype "
					+ dt2);
		}
		return DatatypeElement.areDisjoint(de1, de2);
	}

	public static class DatatypeElement { // implements
											// Comparable<DatatypeElement>{
		private DatatypeElement _primitive;
		private Resource _uri;
		private Set<DatatypeElement> _subtypes = null;

		public DatatypeElement(Resource uri, DatatypeElement primitive) {
			_uri = uri;
			_primitive = primitive;
		}

		public Resource getURI() {
			return _uri;
		}

		public void addSubtype(DatatypeElement... sub) {
			if (_subtypes == null) {
				_subtypes = new HashSet<DatatypeElement>();
			}
			for (DatatypeElement de : sub)
				_subtypes.add(de);
		}

		// public int compareTo(final DatatypeElement de2) {
		// return _uri.compareTo(de2._uri);
		// }

		public boolean isPrimitive() {
			return _primitive == null;
		}

		public DatatypeElement getPrimitive() {
			if (isPrimitive())
				return this;
			return _primitive;
		}

		public Set<DatatypeElement> getSubtypes() {
			return _subtypes;
		}

		@Override
		public boolean equals(final Object o) {
			if (o == this)
				return true;
			if (o instanceof DatatypeElement)
				return equals((DatatypeElement) o);
			return false;
		}

		public boolean equals(final DatatypeElement de) {
			if (de == this)
				return true;
			return _uri.equals(de._uri);
		}

		@Override
		public int hashCode() {
			return _uri.hashCode();
		}

		public boolean isSubtype(DatatypeElement de) {
			return isSubtype(this, de);
		}

		public TreeSet<DatatypeElement> getAllSubtypes() {
			return getAllSubtypes(this);
		}

		public static boolean isSubtype(DatatypeElement sup, DatatypeElement sub) {
			TreeSet<DatatypeElement> subs = sup.getAllSubtypes();
			return subs.contains(sub);
		}

		public static TreeSet<DatatypeElement> getAllSubtypes(DatatypeElement de) {
			TreeSet<DatatypeElement> subs = new TreeSet<DatatypeElement>();
			subs.add(de);
			getAllSubtypes(de, subs);

			return subs;
		}

		private static void getAllSubtypes(DatatypeElement de,
				TreeSet<DatatypeElement> sub) {
			if (de._subtypes == null)
				return;
			for (DatatypeElement sd : de._subtypes) {
				sub.add(sd);
				getAllSubtypes(sd, sub);
			}
		}

		public static boolean areDisjoint(DatatypeElement de1,
				DatatypeElement de2) {
			if (de1.getPrimitive().equals(de2.getPrimitive()))
				return false;

			return true;
		}
	}
}
