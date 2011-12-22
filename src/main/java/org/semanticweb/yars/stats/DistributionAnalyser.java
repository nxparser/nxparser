package org.semanticweb.yars.stats;

import java.util.Iterator;
import java.util.Map;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.filter.NodeFilter;

public class DistributionAnalyser<E> extends DefaultAnalyser{
	protected Count<E> _n;
	protected NodeTransformer<E> _np;
	
	
	public DistributionAnalyser(Iterator<Node[]> in){
		super(in);
	}
	
	public DistributionAnalyser(Analyser in, NodeTransformer<E> np ){
		this(in, null, null, np);
	}
	
	public DistributionAnalyser(Analyser in, NodeFilter[] key, NodeTransformer<E> np){
		this(in, key, null, np);
	}
	
	public DistributionAnalyser(Analyser in, int[] element, NodeTransformer<E> np){
		this(in, null, element, np);
	}
	
	public DistributionAnalyser(Analyser in, NodeFilter[] key, int[] element, NodeTransformer<E> np){
		super(in, key, element);
		_n = new Count<E>();
		_np = np;
	}

	public void stats() {
		;
	}

	public Map<E, Integer> getStatsMap(){
		return _n;
	}
	
	public void analyse(Node[] in){
		for(E e:_np.processNode(in)){
			_n.add(e);
		}
	}
}
