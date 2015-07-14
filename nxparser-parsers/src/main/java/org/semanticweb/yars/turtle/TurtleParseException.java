package org.semanticweb.yars.turtle;
import org.semanticweb.yars.nx.parser.ParseException;

public class TurtleParseException extends ParseException {
	private static final long serialVersionUID = 1L;
	
	public TurtleParseException(String string) {
		super(string);
	}
	
	public TurtleParseException(Exception e) {
		super(e);
	}

	public TurtleParseException(String string, int line, int column) {
		super(string + " line " + line + " col " + column);
	}

	public TurtleParseException(Exception ex, int line, int column) {
		super(ex.getMessage() + " line " + line + " col " + column);
	}
}