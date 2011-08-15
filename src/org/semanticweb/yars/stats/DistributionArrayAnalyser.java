package org.semanticweb.yars.stats;

import java.util.Iterator;
import java.util.Map;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.filter.NodeFilter;
import org.semanticweb.yars.util.Array;

public class DistributionArrayAnalyser<E extends Comparable<E>> extends DistributionAnalyser<E>{
	
	private Count<Array<E>> _n;
	
	public DistributionArrayAnalyser(Iterator<Node[]> in){
		super(in);
	}
	
	public DistributionArrayAnalyser(Analyser in, NodeTransformer<E> np ){
		this(in, null, null, np);
	}
	
	public DistributionArrayAnalyser(Analyser in, NodeFilter[] key, NodeTransformer<E> np){
		this(in, key, null, np);
	}
	
	public DistributionArrayAnalyser(Analyser in, int[] element, NodeTransformer<E> np){
		this(in, null, element, np);
	}
	
	public DistributionArrayAnalyser(Analyser in, NodeFilter[] key, int[] element, NodeTransformer<E> np){
		super(in, key, element, np);
		_n = new Count<Array<E>>();
	}

	public void stats() {
		;
	}

	public Map<Array<E>, Integer> getStats(){return _n;}
	
	public void analyse(Node[] in){
		_n.add(new Array<E>(_np.processNode(in)));
	}
	
	public Map<Array<E>,Integer> getStatsArrayMap(){
		return _n;
	}
}
