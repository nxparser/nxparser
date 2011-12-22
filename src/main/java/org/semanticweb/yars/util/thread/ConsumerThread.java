package org.semanticweb.yars.util.thread;

import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.cli.Main;
import org.semanticweb.yars.nx.parser.Callback;

public class ConsumerThread extends Thread{
	int _ticks = Main.TICKS;
	
	static int _thread_count = 0;
	
	Callback _cb;
	BlockingQueue<Node[]> _q;
	InterruptedException _e;

	int _eofs = 1, _done = 0;

	/**
	 * Constructor for one producer
	 * @param cb consuming Callback
	 * @param q co-ordinating queue
	 */
	public ConsumerThread(Callback cb, BlockingQueue<Node[]> q){
		this(cb, q, 1);
	}
	
	public void setTicks(int ticks){
		_ticks = ticks;
	}


	/**
	 * Constructor for multiple producers
	 * @param cb consuming Callback
	 * @param q co-ordinating queue
	 * @param eofs number of producers -- how many EOF messages to expect before finishing
	 */
	public ConsumerThread(Callback cb, BlockingQueue<Node[]> q, int eofs){
		super(ConsumerThread.class.getName()+_thread_count);
		_thread_count++;
		_cb = cb;
		_q = q;
		_eofs = eofs;
	}

	public void run(){
		int i = 0;
		
		_cb.startDocument();
		Node[] na;
		try {
			while(_done<_eofs){
				i++;
				
				if(_ticks>0 && i%_ticks ==0){
					System.err.println(getName()+" done "+i);
				}
				na = _q.take();
				if(na.equals(Nodes.EOM)){
					_done++;
				} else{
					_cb.processStatement(na);
				}
			}
		} catch (InterruptedException e) {
			_e = e;
		}
		_cb.endDocument();
	}

	public boolean successful(){
		return _e == null;
	}

	public InterruptedException getException(){
		return _e;
	}
}
