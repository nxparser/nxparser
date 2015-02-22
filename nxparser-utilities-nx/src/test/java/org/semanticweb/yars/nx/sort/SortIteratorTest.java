package org.semanticweb.yars.nx.sort;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.junit.Test;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.NodeArrayComparator;
import org.semanticweb.yars.nx.NodeArrayComparator.NodeArrayComparatorArgs;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.nx.sort.SortIterator.SortArgs;

public class SortIteratorTest {

	@Test
	public void test() throws IOException, ParseException {
		URL u = null;
		try {
			u = new URL(
					"http://km.aifb.kit.edu/projects/btc-2014/data/crawls/01/data.nq-1.gz");
		} catch (MalformedURLException e) {
			// won't happen, because I have just entered the URL
		}

		NxParser nxp = new NxParser();
		nxp.parse(new GZIPInputStream(u.openStream()));
		
		SortArgs sa = new SortArgs(nxp);
		
		NodeArrayComparatorArgs naca = new NodeArrayComparatorArgs();
		naca.setOrder(new int[]{0,1,2,3}); //SPOC
		
		sa.setComparator(new NodeArrayComparator(naca));
		
		SortIterator si = new SortIterator(sa);
		
		String lastLine = null;
		String line = null;
		for (Node[] nx : si) {
			line = Nodes.toString(nx);
			if (lastLine != null && lastLine.compareTo(line) > 0)
				fail("The lines were not in lexicographic order: " + lastLine + " " + line);
			lastLine = line;
		}
			
	}

}
