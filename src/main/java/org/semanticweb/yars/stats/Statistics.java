package org.semanticweb.yars.stats;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.ParseException;

public class Statistics {
	public final static Resource RDFTYPE = new Resource("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

	Count<Node> _classes;
	Count<Node> _predicates;
	HashSet<Integer> _objects;
	HashSet<Integer> _sources;
	
	int _instances, _lines, _bnodes, _uris;
	
	public Statistics() {
		_classes = new Count<Node>();
		_predicates = new Count<Node>();
		_objects = new HashSet<Integer>();
		_sources = new HashSet<Integer>();
		_instances = 0;
		_lines = 0;
		_bnodes = 0;
		_uris = 0;
	}
		
	public void count(Iterator<Node[]> it, boolean all) throws IOException, ParseException {		
		Node[] quad = null;
		Node oldSub = null;
		TreeSet<Node> cons = new TreeSet<Node>();
		TreeSet<Node> types = new TreeSet<Node>();
		boolean done = ! it.hasNext();

		while (! done) {
			done = ! it.hasNext();
			if(! done){
				_lines++;
				quad = it.next();
			}
			
			if(_lines%1000000 == 0){
				System.err.println("Done "+_lines);
				//printStats();
				//_classes.printStats();
				//_predicates.printStats();
			}
			
			if(oldSub == null){
				oldSub = quad[0];
			}
			else if(!oldSub.equals(quad[0]) || !it.hasNext()){
				_instances++;
				
				if(oldSub instanceof BNode){
					_bnodes++;
					for(Node n:types)
						_classes.add(n);
				}

				else if(oldSub instanceof Resource){
					_uris++;
					for(Node n:types)
						_classes.add(n);
				}
				
				else{
					System.err.println("oldSub is a "+oldSub.getClass().getName());
				}
				types = new TreeSet<Node>();
				
				oldSub = quad[0];
			}
			cons.add(quad[3]);
			
			//remove duplication for instance typing using TreeSet
			if (quad[1].equals(RDFTYPE)) {
				types.add(quad[2]);
			}

			_predicates.add(quad[1]);
		
			if (all) {
				//_objects.add(quad[2]);
				_sources.add(quad[3].hashCode());
			}
		}
	}
		
	public void printStats() {
		System.out.println("Number of classes "+_classes.size());
		System.out.println("Number of subjects "+_instances);
		System.out.println("Number of predicates "+_predicates.size());
		//System.out.println("Number of objects "+_objects.size());
		System.out.println("Number of contexts "+_sources.size());
		System.out.println("Number of quadruples "+_lines);
		System.out.println("Number of anonymous nodes "+_bnodes);
		System.out.println("Number of URI identified nodes "+_uris);
	}
	
	public void printClasses() {
		_classes.printStats();
	}
	
	public void printPredicates() {
		_predicates.printStats();
	}
	
	public class Count<T> {
		Hashtable<T, Integer> _ht;
		
		public Count() {
			_ht = new Hashtable<T, Integer>();
		}
		
		public void add(T id) {
			if (_ht.containsKey(id)) {
				Integer i = _ht.get(id);
				_ht.put(id, new Integer(i.intValue() + 1));
			} else {
				_ht.put(id, new Integer(1));
			}
		}
		
		public void printStats() {
			Iterator<Map.Entry<T, Integer>> it = _ht.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry<T, Integer> e = it.next();
			
				System.out.println(e.getKey() + "\t" + e.getValue());
			}
		}
		
		public String toString() {
			StringBuffer s = new StringBuffer();
			Iterator<Map.Entry<T, Integer>> it = _ht.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry<T, Integer> e = it.next();
			
				s.append(e.getKey() + "\t" + e.getValue()+"\n");
			}
			return s.toString();
		}
		
		public int size() {
			return _ht.size();
		}
	}
}