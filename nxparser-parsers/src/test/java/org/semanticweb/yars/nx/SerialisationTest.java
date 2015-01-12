package org.semanticweb.yars.nx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.yars.nx.parser.NxParser;

public class SerialisationTest {

	@Test
	public void testSerialisation() throws Exception {
		long time = System.currentTimeMillis();
		
		Set<Nodes> set = new HashSet<Nodes>();
		
		NxParser nxp = new NxParser();
		nxp.parse(new FileInputStream("src/test/resources/foaf.nt"));
		
		int i = 0;
		while (nxp.hasNext()) {
			set.add(new Nodes(nxp.next()));
			i++;
		}
		
		long time1 = System.currentTimeMillis();

		System.out.println("Triples: " + set.size() + " iterations: " + i);

		System.out.println("Parsing: time elapsed " + (time1-time) + " ms");
		
		FileOutputStream fout = new FileOutputStream("target/w3c-testcase.ser");
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(set);
		out.close();
		fout.close();

		long time2 = System.currentTimeMillis();

		System.out.println("Serialisation: time elapsed " + (time2-time1) + " ms");

	}

}
