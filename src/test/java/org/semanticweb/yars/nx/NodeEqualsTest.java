package org.semanticweb.yars.nx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class NodeEqualsTest {

	@Test
	public void testEquals() {
		Resource r1 = new Resource("http://example.org/");
		Resource r2 = new Resource("http://example.org/");

		System.out.println(r1.hashCode());
		System.out.println(r2.hashCode());
		
		System.out.println(r1.equals(r2));

		Set<Node> set = new HashSet<Node>();
		
		set.add(r1);
		set.add(r2);
		
		System.out.println(set);
		
		System.out.println(set.contains(r2));
		
	}

	@Test
	public void testMap() {
		Resource r1 = new Resource("http://example.org/one");
		Resource r2 = new Resource("http://example.org/two");

		System.out.println(r1.hashCode());
		System.out.println(r2.hashCode());
		
		System.out.println(r1.equals(r2));

		Map<Nodes, Nodes> map = new HashMap<Nodes, Nodes>();
		
		map.put(new Nodes(r1), new Nodes(r2));
		map.put(new Nodes(r2), new Nodes(r1));

		System.out.println(map);
		
		System.out.println(map.get(new Nodes(r1)));
		System.out.println(map.get(new Nodes(r2)));
	}

}
