package org.semanticweb.yars.nx;

import java.util.TreeSet;

import org.semanticweb.yars.nx.parser.ParseException;

/**
 * @deprecated : Use org.semanticweb.yars.nx.dt package
 */
public class NumericLiteral extends Literal {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static final Resource[] INTEGER_TYPES = {
		new Resource(XSD+"integer"),
		new Resource(XSD+"int"),
		new Resource(XSD+"positiveInteger"),
		new Resource(XSD+"negativeInteger"),
		new Resource(XSD+"nonPositiveInteger"),
		new Resource(XSD+"nonNegativeInteger"),
		new Resource(XSD+"unsignedInt")
	};
	private static final Resource[] BYTE_TYPES = {
		new Resource(XSD+"byte"),
		new Resource(XSD+"unsignedByte")
	};
	private static final Resource[] SHORT_TYPES = {
		new Resource(XSD+"short"), 
		new Resource(XSD+"unsignedShort")
	};	
	private static final Resource[] LONG_TYPES = {
		new Resource(XSD+"long"),
		new Resource(XSD+"unsignedLong")
	};
	private static final Resource[] FLOAT_TYPES = {
		new Resource(XSD+"float")
	};
	private static final Resource[] DOUBLE_TYPES = {
			new Resource(XSD+"double"),
			new Resource(XSD+"decimal")
	};
	
	private static final TreeSet<Resource> INTEGERS = toTreeSet(INTEGER_TYPES);
	private static final TreeSet<Resource> BYTES = toTreeSet(BYTE_TYPES);
	private static final TreeSet<Resource> SHORTS = toTreeSet(SHORT_TYPES);
	private static final TreeSet<Resource> LONGS = toTreeSet(LONG_TYPES);
	private static final TreeSet<Resource> FLOATS = toTreeSet(FLOAT_TYPES);
	private static final TreeSet<Resource> DOUBLES = toTreeSet(DOUBLE_TYPES);

	public static final TreeSet<Resource> NUMBERS = toTreeSet(INTEGER_TYPES, BYTE_TYPES, SHORT_TYPES, LONG_TYPES, FLOAT_TYPES, DOUBLE_TYPES);
	
	public static final Resource INTEGER = new Resource(XSD+"integer");
	public static final Resource BYTE = new Resource(XSD+"byte");
	public static final Resource SHORT = new Resource(XSD+"short");
	public static final Resource LONG = new Resource(XSD+"long");
	public static final Resource FLOAT = new Resource(XSD+"float");
	public static final Resource DOUBLE = new Resource(XSD+"double");
	public static final Resource DECIMAL = new Resource(XSD+"decimal");
	
	private Number _n;
	
	public NumericLiteral(Number n){
		super(n.toString(), getDatatype(n));
		_n = n;
	}
	
	public NumericLiteral(Number n, Resource dt){
		super(n.toString(), dt);
		_n = n;
	}
	
	public NumericLiteral(String s, Resource dt, Number n) throws NumberFormatException{
		super(s, dt);
		_n = n;
	}
	
	public static NumericLiteral getNumericLiteral(Literal l) throws ParseException{
		if(l instanceof NumericLiteral){
			return (NumericLiteral)l;
		} else if(l instanceof Literal){
			try{
				Number n = NumericLiteral.parseNumber(l);
				return new NumericLiteral(l.getData(), l.getDatatype(), n);
			} catch(NumberFormatException nfe){
				throw new ParseException("Cannot parse NumericLiteral from "+l.toN3());
			}
		}
		else throw new ParseException("Cannot parse NumericLiteral from "+l.getClass().getSimpleName()+" argument.");
	}
	
	public Number getNumber(){
		return _n;
	}
	
	public static Number parseNumber(String s) throws NumberFormatException{
		Number n;
		try{
			n = Integer.parseInt(s);
		} catch (NumberFormatException nfe){
			n = Double.parseDouble(s);
		}
		return n;
	}
	
	public static Resource getDatatype(Number n){
		if(n instanceof Integer)
			return INTEGER;
		else if(n instanceof Byte)
			return BYTE;
		else if(n instanceof Short)
			return SHORT;
		else if(n instanceof Long)
			return LONG;
		else if(n instanceof Float)
			return FLOAT;
		else
			return DOUBLE;
	}
	
	public void stripSign(){
		if(_n.doubleValue()>=0)
			return;
		
		if(_n instanceof Integer)
			_n = _n.intValue()*-1;
		else if(_n instanceof Byte)
			_n = (Byte)_n.byteValue()*-1;
		else if(_n instanceof Short)
			_n = (Short)_n.shortValue()*-1;
		else if(_n instanceof Long)
			_n = (Long)_n.longValue()*-1;
		else if(_n instanceof Float)
			_n = (Float)_n.floatValue()*-1;
		else
			_n = (Double)_n.doubleValue()*-1;
		
		_data = _n.toString();
	}
	
	private static Number parseNumber(Literal l) throws NumberFormatException{
		if(l instanceof NumericLiteral)
			return ((NumericLiteral)l).getNumber();
		
		Resource dt = l.getDatatype();
		
		if(INTEGERS.contains(dt)){
			return Integer.parseInt(l.getData());
		} else if(DOUBLES.contains(dt)){
			return Double.parseDouble(l.getData());
		} else if(LONGS.contains(dt)){
			return Long.parseLong(l.getData());
		} else if(FLOATS.contains(dt)){
			return Float.parseFloat(l.getData());
		} else if(BYTES.contains(dt)){
			return Byte.parseByte(l.getData());
		} else if(SHORTS.contains(dt)){
			return Short.parseShort(l.getData());
		} else return Double.parseDouble(l.getData());
	}
	
	private static TreeSet<Resource> toTreeSet(Resource[]... rss){
		TreeSet<Resource> ts = new TreeSet<Resource>();
		for(Resource[] rs:rss){
			for(Resource r:rs){
				ts.add(r);
			}
		}
		return ts;
	}
	
	public int compareTo(NumericLiteral nl){
		double d1 = _n.doubleValue(), d2 = nl.getNumber().doubleValue();
		if(d1 == d2){
			return 0;
		} else if(d1>d2){
			if((int)(d1-d2)==0){
				return 1;
			} else return (int) (d1-d2);
		} else {
			if((int)(d1-d2)==0){
				return -1;
			} else return (int) (d1-d2);
		}
	}
}
