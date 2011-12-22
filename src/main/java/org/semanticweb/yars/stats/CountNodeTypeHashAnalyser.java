package org.semanticweb.yars.stats;

import java.util.Iterator;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.filter.NodeFilter;

public class CountNodeTypeHashAnalyser extends DefaultAnalyser{
	private Count<Integer> _b;
	private Count<Integer> _r;
	private Count<Integer> _l;
	
	public CountNodeTypeHashAnalyser(Iterator<Node[]> in){
		super(in);
	}
	
	public CountNodeTypeHashAnalyser(Analyser in){
		this(in, null, null);
	}
	
	public CountNodeTypeHashAnalyser(Analyser in, NodeFilter[] key){
		this(in, key, null);
	}
	
	public CountNodeTypeHashAnalyser(Analyser in, int[] element){
		this(in, null, element);
	}
	
	public CountNodeTypeHashAnalyser(Analyser in, NodeFilter[] key, int[] element){
		super(in, key, element);
		_b = new Count<Integer>();
		_r = new Count<Integer>();
		_l = new Count<Integer>();
	}
	
	public void stats() {
		;
	}
	
	public void analyse(Node[] in){
		for(Node n:in){
			if(n instanceof Resource){
				_r.add(n.hashCode());
			} else if(n instanceof BNode){
				_b.add(n.hashCode());
			} else if(n instanceof Literal){
				_l.add(n.hashCode());
			} else{
				throw new RuntimeException("Unknown node type "+in.getClass());
			}
		}
	}
	
}
