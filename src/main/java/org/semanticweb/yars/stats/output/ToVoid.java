/**
 * 
 */
package org.semanticweb.yars.stats.output;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author juergen
 *
 */
public class ToVoid {
	
	public static String getRDF(String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<rdf:RDF xmlns=\"http://sw.deri.org/sw/2009/01/webstar/datasets#\"");
		sb.append(" xmlns:dcterms=\"http://purl.org/dc/terms/\"");
		sb.append(" xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"");
		sb.append(" xmlns:log=\"http://www.w3.org/2000/10/swap/log#\"");
		sb.append(" xmlns:owl=\"http://www.w3.org/2002/07/owl#\"");
		sb.append(" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
		sb.append(" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		sb.append(" xmlns:scovo=\"http://purl.org/NET/scovo#\"");
		sb.append(" xmlns:void=\"http://rdfs.org/ns/void#\">\n");
	
		sb.append(content);
		
		sb.append("\n</rdf:RDF>");
		
		return sb.toString();
	}

	public static String getDataSetHeader(String uri, String content) {
		StringBuffer sb = new StringBuffer(); 
		sb.append("\n<void:Dataset rdf:about=\"").append(uri).append("\">");
		sb.append("\n<foaf:homepage rdf:resource=\"http://sw.deri.org/sw/2009/01/webstar/datasets/\"/>");
	 
		sb.append(content);
	    
		sb.append("\n</void:Dataset>");
		
		return sb.toString();
		
	}
		
	public static String getStmtString(int count) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n<void:statItem rdf:parseType=\"Resource\">");
		sb.append("\n<scovo:dimension rdf:resource=\"http://rdfs.org/ns/void#numberOfTriples\"/>");
		sb.append("\n<rdf:value rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">").append(count).append("</rdf:value>");
		sb.append("\n<dcterms:creator rdf:resource=\"http://sw.deri.org/2006/08/nxparser\" />");
		sb.append("\n</void:statItem>");
		
		return sb.toString();
	}
	
	public static String getClassDist(Map<org.semanticweb.yars.nx.Node,Integer> classDistMap) {
		StringBuffer sb = new StringBuffer();
		for(Entry<org.semanticweb.yars.nx.Node, Integer> ent : classDistMap.entrySet()) {
			sb.append("\n<void:statItem rdf:parseType=\"Resource\">");
			sb.append("\n<scovo:dimension rdf:resource=\"http://rdfs.org/ns/void#numberOfTriples\"/>");
			sb.append("\n<scovo:dimension rdf:resource=\"").append(ent.getKey()).append("\" />");
			sb.append("\n<dcterms:creator rdf:resource=\"http://sw.deri.org/2006/08/nxparser\" />");
			sb.append("\n<rdf:value rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">").append(ent.getValue()).append("</rdf:value>");
			sb.append("\n</void:statItem>");
		}
		return sb.toString();

	}
}
