package org.semanticweb.yars.nx;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import org.semanticweb.yars.util.Array;

public class Nodes implements Comparable<Nodes>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1195176262281356050L;
	
	public static final Node[] EOM = new Node[0];
	
	Node[] _na;
	NodeComparator _nc;
	
	public Nodes(Node... n){
		this(NodeComparator.NC, n);
	}
	
	public Nodes(Collection<Node> cn){
		this(NodeComparator.NC, cn);
	}
	
	public Nodes(NodeComparator nc, Collection<Node> cn){
		_na = new Node[cn.size()];
		cn.toArray(_na);
		_nc = nc;
	}
	
	public Nodes(NodeComparator nc, Node... n){
		_na = n;
		_nc = nc;
	}
	
	public Node[] getNodes(){
		return _na;
	}
	
	public int compareTo(Nodes ns){
		return _nc.compare(_na, ns.getNodes());
	}
	
	public boolean equals(Nodes ns){
		return _nc.equals(_na, ns.getNodes());
	}
	
	public boolean equals(Object o){
		if(o instanceof Nodes){
			return equals((Nodes)o);
		} return false;
	}
	
	public String toN3(){
		return toN3(_na);
	}
	
	public String toString() {
		return toN3();
	}
	
	public int hashCode(){
		return hashCode(_na);
	}
	
	public static String toN3(Node[] ns) {
		StringBuffer buf = new StringBuffer();
		for (Node n: ns){
			buf.append(n.toN3());
			buf.append(" ");
		}
		buf.append(".");

		return buf.toString();
	}
	
	/**
	 * Fast hashcode method for Node arrays
	 */
	public static int hashCode(Node... nx) {
		return Array.hashCode(nx, 0, nx.length);
	}
	
	/**
	 * Fast hashcode method for Node arrays
	 */
	public static int hashCode(Node[] nx, int pos, int length) {
		return Array.hashCode(nx, pos, length);
	}
	
	public static void main (String args[]) throws IOException{
		Node[] a1 = {new BNode("b1asdf"), new BNode("b2qwer")};
		System.err.println(hashCode(a1));
		System.err.println(new Nodes(a1).hashCode());
		
		Node[] a2 = {new BNode("b1asdf"), new BNode("b2qwer")};
		System.err.println(hashCode(a2));
		System.err.println(new Nodes(a2).hashCode());
		
		System.err.println(new Nodes(a1).equals(new Nodes(a2)));
		System.err.println(new Nodes(a2).equals(new Nodes(a1)));
		System.err.println(new Nodes(a1).equals(new Nodes(a1)));
		
		ObjectOutputStream oos = new ObjectOutputStream(System.out);
		oos.writeObject(new Nodes(a1));
		oos.close();
	}
}
