package org.semanticweb.yars.util.thread;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

/**
 * Uses a blocking queue to co-ordinate between a producer thread
 * wrapping an iterator and a consumer thread wrapping a callback
 * @author aidhog
 *
 */
public class ConsumerProducerThread extends Thread{
	public static final Node[] EOM = new Node[0];
	public static final int DEFAULT_BUFFER = 500;

	private ConsumerThread _ct;
	private ProducerThread _pt;
	private Exception _e;
	
	public ConsumerProducerThread(Iterator<Node[]> in, Callback out){
		this(in, out, DEFAULT_BUFFER);
	}

	public ConsumerProducerThread(Iterator<Node[]> in, Callback out, int buffer){
		this(in, out, new ArrayBlockingQueue<Node[]>(buffer));
	}

	public ConsumerProducerThread(Iterator<Node[]> in, Callback out, BlockingQueue<Node[]> q){
		_ct = new ConsumerThread(out, q);
		_pt = new ProducerThread(in, q);
	}

	/**
	 * Threaded execution
	 */
	public void run(){
		try{
			runUnthreaded();
		} catch(Exception e){
			_e = e;
		}
	}
	
	public void setTicks(int ticks){
		_ct.setTicks(ticks);
		_pt.setTicks(ticks);
	}

	public boolean successful(){
		return _e == null;
	}

	public Exception getException(){
		return _e;
	}

	/**
	 * Non-threaded execution
	 * @throws Exception 
	 */
	public void runUnthreaded() throws InterruptedException{
		_pt.start();
		_ct.start();

		_pt.join();

		_ct.join();

		if(!_ct.successful()){
			throw _ct.getException();
		}
	}
}
