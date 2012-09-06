package org.semanticweb.yars.util;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;

/**
 * Iterator which logs input ticks
 * @author aidhog
 *
 */
public class TicksIterator implements Iterator<Node[]>{
	static final transient Logger DEFAULT_LOG = Logger.getLogger(TicksIterator.class.getName());
	static final String DEFAULT_MESSAGE = "Input ";
	static final Level DEFAULT_LEVEL = Level.INFO;
	
	final Logger _log;
	final Level _l;
	final Iterator<Node[]> _in;
	final int _ticks;
	final String _message;
	
	long _count = 0;
	
	public TicksIterator(Iterator<Node[]> in, int ticks){
		this(in, ticks, DEFAULT_LOG, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public TicksIterator(Iterator<Node[]> in, int ticks, Logger log){
		this(in, ticks, log, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public TicksIterator(Iterator<Node[]> in, int ticks, Logger log, Level l){
		this(in, ticks, log, l, DEFAULT_MESSAGE);
	}
	
	public TicksIterator(Iterator<Node[]> in, int ticks, Logger log, String message){
		this(in, ticks, log, DEFAULT_LEVEL, message);
	}
	
	public TicksIterator(Iterator<Node[]> in, int ticks, Logger log, Level l, String message){
		_in = in;
		_log = log;
		_ticks = ticks;
		_message = message;
		_l = l;
	}
	
	public static Iterator<Node[]> createTicksIterator(Iterator<Node[]> in, int ticks){
		return createTicksIterator(in, ticks, DEFAULT_LOG, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public static Iterator<Node[]> createTicksIterator(Iterator<Node[]> in, int ticks, Logger log){
		return createTicksIterator(in, ticks, log, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public static Iterator<Node[]> createTicksIterator(Iterator<Node[]> in, int ticks, Logger log, Level l){
		return createTicksIterator(in, ticks, log, l, DEFAULT_MESSAGE);
	}
	
	public static Iterator<Node[]> createTicksIterator(Iterator<Node[]> in, int ticks, Logger log, String message){
		return createTicksIterator(in, ticks, log, DEFAULT_LEVEL, message);
	}
	
	public static Iterator<Node[]> createTicksIterator(Iterator<Node[]> in, int ticks, Logger log, Level l, String message){
		if(ticks>0){
			return new TicksIterator(in, ticks, log, l, message);
		}
		return in;
	}

	public boolean hasNext() {
		return _in.hasNext();
	}
	
	public long count(){
		return _count;
	}

	public Node[] next() {
		_count++;
		if(_count>0 && _count%_ticks==0){
			_log.log(_l, _message+" "+_count);
		}
		return _in.next();
	}

	public void remove() {
		_in.remove();
	}
}
