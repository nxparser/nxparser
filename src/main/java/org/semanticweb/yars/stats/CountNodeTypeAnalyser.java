package org.semanticweb.yars.stats;

import java.util.Iterator;
import java.util.Map;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.filter.NodeFilter;

public class CountNodeTypeAnalyser extends DefaultAnalyser{
	private Count<BNode> _b;
	private Count<Resource> _r;
	private Count<Literal> _l;
	
	public CountNodeTypeAnalyser(Iterator<Node[]> in){
		super(in);
	}
	
	public CountNodeTypeAnalyser(Analyser in){
		this(in, null, null);
	}
	
	public CountNodeTypeAnalyser(Analyser in, NodeFilter[] key){
		this(in, key, null);
	}
	
	public CountNodeTypeAnalyser(Analyser in, int[] element){
		this(in, null, element);
	}
	
	public CountNodeTypeAnalyser(Analyser in, NodeFilter[] key, int[] element){
		super(in, key, element);
		_b = new Count<BNode>();
		_r = new Count<Resource>();
		_l = new Count<Literal>();
	}

	public void stats() {
		;
	}
	
	public Map<Resource,Integer> getResourceMap() {
		return _r;
	}
	public Map<BNode,Integer> getBNodeMap() {
		return _b;
	}
	public Map<Literal,Integer> getLiteralMap() {
		return _l;
	}
	
	public void analyse(Node[] in){
		for(Node n:in){
			if(n instanceof Resource){
				_r.add((Resource)n);
			} else if(n instanceof BNode){
				_b.add((BNode)n);
			} else if(n instanceof Literal){
				_l.add((Literal)n);
			} else{
				throw new RuntimeException("Unknown Node type "+in.getClass());
			}
		}
	}

}
