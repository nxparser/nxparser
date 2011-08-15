package org.semanticweb.yars.nx;

import org.semanticweb.yars.nx.parser.ParseException;

/**
 * @deprecated : Use org.semanticweb.yars.nx.dt package
 */
public class BooleanLiteral extends Literal {
	
	private static final long serialVersionUID = 1L;
	public static final Resource BOOLEAN = new Resource(XSD+"boolean");
	
	public static final BooleanLiteral TRUE = new BooleanLiteral(true);
	public static final BooleanLiteral FALSE = new BooleanLiteral(false);
	
	private BooleanLiteral(boolean value){
		//need to call super constrcutor first. boo.
		super(Boolean.toString(value), BOOLEAN);
	}
	
	public boolean toBoolean(){
		if(_data.equals("true")){
			return true;
		} else return false;
	}
	
	

	public static BooleanLiteral getEffectiveBooleanLit(Literal l) throws ParseException {
		Resource dt = l.getDatatype();
		if(l instanceof BooleanLiteral)
			return (BooleanLiteral)l;
		else if(dt==null || dt.equals(STRING)){
			if(l.getData()==null || l.getData().length()==0)
				return FALSE;
			else
				return TRUE;
		} else if(dt.equals(BOOLEAN)){
			if(l.getData().equals("true"))
				return BooleanLiteral.TRUE;
			else
				return BooleanLiteral.FALSE;
		} else {
			if(l.getData().equals("NaN"))
				return FALSE;
				
			NumericLiteral nl = NumericLiteral.getNumericLiteral(l);
			Number n = nl.getNumber();
			if(n==null)
				throw new IllegalArgumentException("Cannot parse literal "+l.getData()+" "+l.getDatatype()+" into a boolean value.");
			else if(n.doubleValue()==0)
				return FALSE;
			else
				return TRUE;
		}
	}
	
	public static void main(String args[]){
		Literal l = new Literal("true", new Resource("http://www.w3.org/2001/XMLSchema#boolean"));
		Node l2 = new Literal("true", new Resource("http://www.w3.org/2001/XMLSchema#boolean"));
		System.out.println(l.equals(l2));
		
		BooleanLiteral bl = new BooleanLiteral(true);
		Node bl2 = new BooleanLiteral(true);
		
		System.out.println(bl.equals(l2));
		System.out.println(bl.equals(bl2));
		System.out.println(l2.equals(bl));
	}
}
