package org.semanticweb.yars.stats;

import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.filter.NodeFilter;

public class CountStmtAnalyser extends DefaultAnalyser{
	private int _count;
	
	public CountStmtAnalyser(Iterator<Node[]> in){
		super(in);
	}
	
	public CountStmtAnalyser(Iterator<Node[]> in, NodeFilter[] key){
		super(in, key);
	}
	
	public CountStmtAnalyser(Analyser in){
		super(in);
	}
	
	public CountStmtAnalyser(Analyser in, NodeFilter[] key){
		super(in, key);
	}
	
	public int getStmt() {
		return _count;
	}
	
	public void analyse(Node[] in){
		_count++;
	}

	public void stats() {
		;
	}
}
