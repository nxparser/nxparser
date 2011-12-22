package org.semanticweb.yars.stats;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.filter.NodeFilter;

public abstract class DefaultAnalyser implements Analyser {
	private final Analyser _in;
	private final int[] _element;
	private final NodeFilter[] _key;
	
	public DefaultAnalyser(Iterator<Node[]> in){
		this(in, null);
	}
	
	public DefaultAnalyser(Iterator<Node[]> in, NodeFilter[] key){
		if(in instanceof Analyser)
			_in = (Analyser)in;
		else _in = new InputAnalyser(in);
		
		_key = key;
		_element = null;
	}
	
	public DefaultAnalyser(Analyser in){
		this(in, null, null);
	}
	
	public DefaultAnalyser(Analyser in, NodeFilter[] key){
		this(in, key, null);
	}
	
	public DefaultAnalyser(Analyser in, int[] element){
		this(in, null, element);
	}
	
	public DefaultAnalyser(Analyser in, NodeFilter[] key, int[] element){
		_in = in;
		_key = key;
		_element = element;
	}
	
	public boolean hasNext() {
		return _in.hasNext();
	}

	public Node[] next() {
		Node[] next = _in.next();
		Node[] analyseNodes = createIn(next); 
		if(checkKey(analyseNodes))
			analyse(analyseNodes);
		return next;
	}
	
	private boolean checkKey(Node[] in){
		if(_key==null)
			return true;
		for(int i=0; i<_key.length; i++){
			NodeFilter f = _key[i];
			if(f!=null){
				if(!f.check(in[i])){
					return false;
				}
			}
		}
		return true;
	}
	
	private Node[] createIn(Node[] in){
		if(_element==null)
			return in;
		Node[] na = new Node[_element.length];
		int j=0;
		for(int i:_element){
			na[j] = in[i];
			j++;
		}
		return na;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
