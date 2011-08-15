package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;

public class Namespace {
	
	public static String getNamespace(Node n){
		if(n instanceof Resource){
			return getNamespace((Resource)n);
		}
		else return null;
	}
	
	
	public static String getNamespace(Resource r){
		return getNamespace(r.toString());
	}
	
	public static String getNamespace(String url){
		int hash, slash, end;
		hash = url.lastIndexOf("#");
		slash = url.lastIndexOf("/");
		end = max(hash, slash);
		if(end <= 0)
			return null;
		return url.substring(0,end+1);
	}
	
	private static int max(int a, int b){
		if(a>b) return a;
		return b;
	}
}
