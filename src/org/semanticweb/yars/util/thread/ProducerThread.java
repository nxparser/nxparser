package org.semanticweb.yars.util.thread;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.cli.Main;

/**
 * Thread which takes a Node[] Iterator and successively adds
 * the contents to a blocking queue. Also adds Nodes.EOM when
 * the iterator is exhausted.
 * 
 * @author aidhog
 *
 */
public class ProducerThread extends Thread{
	int _ticks = Main.TICKS;
	
	static int _thread_count = 0;

	Iterator<Node[]> _in;
	BlockingQueue<Node[]> _q;
	InterruptedException _e;

	public ProducerThread(Iterator<Node[]> in, BlockingQueue<Node[]> q){
		super(ProducerThread.class.getName()+_thread_count);
		_thread_count++;
		_q = q;
		_in = in;
	}

	public void run(){
		int i = 0;
		try {
			while(_in.hasNext()){
				i++;
				if(_ticks>0 && i%_ticks ==0){
					System.err.println(getName()+" done "+i);
				}
				_q.put(_in.next());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			_e = e;
		}
		_q.add(Nodes.EOM);
	}
	
	public void setTicks(int ticks){
		_ticks = ticks;
	}

	public boolean successful(){
		return _e == null;
	}

	public InterruptedException getException(){
		return _e;
	}
}
