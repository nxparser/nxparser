package org.semanticweb.yars.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * Print ticks for callback
 * @author aidhog
 *
 */
public class CallbackTicks implements Callback{
	static final transient Logger DEFAULT_LOG = Logger.getLogger(CallbackTicks.class.getName());
	static final String DEFAULT_MESSAGE = "Output ";
	static final Level DEFAULT_LEVEL = Level.INFO;
	
	final Logger _log;
	final Level _l;
	final Callback _cb;
	final int _ticks;
	final String _message;
	
	int _count = 0;
	
	public CallbackTicks(Callback out, int ticks){
		this(out, ticks, DEFAULT_LOG, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public CallbackTicks(Callback out, int ticks, Logger log){
		this(out, ticks, log, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public CallbackTicks(Callback out, int ticks, Logger log, Level l){
		this(out, ticks, log, l, DEFAULT_MESSAGE);
	}
	
	public CallbackTicks(Callback out, int ticks, Logger log, String message){
		this(out, ticks, log, DEFAULT_LEVEL, message);
	}
	
	public CallbackTicks(Callback out, int ticks, Logger log, Level l, String message){
		_cb = out;
		_log = log;
		_ticks = ticks;
		_message = message;
		_l = l;
	}
	
	public int count(){
		return _count;
	}

	public void endDocument() {
		_cb.endDocument();
	}

	public void processStatement(Node[] nx) {
		_count++;
		if(_count>0 && _count%_ticks==0){
			_log.log(_l, _message+" "+_count);
		}
		_cb.processStatement(nx);	
	}

	public void startDocument() {
		_cb.startDocument();
	}
	
	public static Callback createCallbackTicks(Callback out, int ticks){
		return createCallbackTicks(out, ticks, DEFAULT_LOG, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public static Callback createCallbackTicks(Callback out, int ticks, Logger log){
		return createCallbackTicks(out, ticks, log, DEFAULT_LEVEL, DEFAULT_MESSAGE);
	}
	
	public static Callback createCallbackTicks(Callback out, int ticks, Logger log, Level l){
		return createCallbackTicks(out, ticks, log, l, DEFAULT_MESSAGE);
	}
	
	public static Callback createCallbackTicks(Callback out, int ticks, Logger log, String message){
		return createCallbackTicks(out, ticks, log, DEFAULT_LEVEL, message);
	}
	
	public static Callback createCallbackTicks(Callback out, int ticks, Logger log, Level l, String message){
		if(ticks>0){
			return new CallbackTicks(out, ticks, log, l, message);
		}
		return out;
	}
}
