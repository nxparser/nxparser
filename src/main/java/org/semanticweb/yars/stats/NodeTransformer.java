package org.semanticweb.yars.stats;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.namespace.Namespace;

public interface NodeTransformer<E>{

	public E[] processNode(Node[] n);


	public static class NoTransformer implements NodeTransformer<Node>{

		public Node[] processNode(Node[] n) {
			return n;
		}
	}

	public static class HashTransformer implements NodeTransformer<Integer>{

		public Integer[] processNode(Node[] na) {
			Integer[] hs = new Integer[na.length];
			for(int i=0; i<na.length; i++){
				hs[i] = na[i].hashCode();
			}
			return hs;
		}

	}

	public static class NamespaceTransformer implements NodeTransformer<String>{
		public final static String EMPTY_NS = "N/A";

		public String[] processNode(Node[] na) {
			String[] nss = new String[na.length];
			for(int i=0; i<na.length; i++){
				nss[i] = Namespace.getNamespace(na[i]);
				if(nss[i]==null)
					nss[i] = EMPTY_NS;
			}
			return nss;
		}

	}

	public static class NamespaceHashTransformer implements NodeTransformer<Integer>{
		public final static int EMPTY_NS_HASH = 0;

		public Integer[] processNode(Node[] na) {
			Integer[] nshs = new Integer[na.length];
			for(int i=0; i<na.length; i++){
				String ns = Namespace.getNamespace(na[i]);
				if(ns==null)
					nshs[i] = EMPTY_NS_HASH;
				else nshs[i]= ns.hashCode();
			}
			return nshs;
		}

	}
}

