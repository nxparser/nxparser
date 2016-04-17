package org.semanticweb.yars.nx;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.util.Util;

public class NodesReResolvingIteratorTest {

	@Test
	public void testContextualBNodeTreatment1() throws URISyntaxException {

		BNode bn = NodesReResolvingIterator
				.reResolveContextualBnode(
						new BNode("_:httpx3Ax2Fx2Fthisx2Enx78parserx2Egithubx2Eiox2Freferencex2Ftox2FURIx2Fofx2Fcurrentx2Frdfx2Fgraphx2Fforx2Frepresentingx2Fpermanentlyx2Frelativex2FURIsx2Finx2FNx2DTriplesx2Fxxb1x78x78filex783Ax782FCx783Ax782FUsersx782FTobiasx782FDocumentsx782FAIFBx782Fivisionx782FMeetingsx782520Physicalx782F2015x782D09x782520Patrasx782Fix782DVISIONx782DBrusselsx782DWorkflowx782DDemox785F2x782E0x782Fnewx785Ftakeoffx785Fworkflowx785Fmodelx782Ettl"),
						new URI("http://example.org/lalala/2"));

		BNode goldStandard = new BNode("_:b1xxfilex3Ax2FCx3Ax2FUsersx2FTobiasx2FDocumentsx2FAIFBx2Fivisionx2FMeetingsx2520Physicalx2F2015x2D09x2520Patrasx2Fix2DVISIONx2DBrusselsx2DWorkflowx2DDemox5F2x2E0x2Fnewx5Ftakeoffx5Fworkflowx5Fmodelx2Ettl");

		assertEquals(goldStandard, bn);
	}

	@Test
	public void testContextualBNodeTreatment2() throws URISyntaxException {

		BNode bn = NodesReResolvingIterator
				.reResolveContextualBnode(
						new BNode("_:httpx3Ax2Fx2Fthisx2Enx78parserx2Egithubx2Eiox2Freferencex2Ftox2FURIx2Fofx2Fcurrentx2Frdfx2Fgraphx2Fforx2Frepresentingx2Fpermanentlyx2Frelativex2FURIsx2Finx2FNx2DTriplesx2Fxxb1"),
						new URI("http://example.org/lalala/2"));

		BNode goldStandard = new BNode("_:b1xxhttpx3Ax2Fx2Fex78amplex2Eorgx2Flalalax2F2");

		assertEquals(goldStandard, bn);
	}

	@Test
	public void resolvingTestSameDocument() throws URISyntaxException {
		URI wellknown = Util.THIS_URI;
		URI relativeToItself = wellknown.relativize(wellknown);
		
		URI uriToResolveAgainst = new URI("http://ex.org/123");
		
		URI resolvedURI = Util.properlyResolve(uriToResolveAgainst, relativeToItself);
		
		assertEquals(uriToResolveAgainst,resolvedURI);
	}
	
	@Test
	public void resolvingTestURIsameDirectory() throws URISyntaxException {
		URI wellknown = Util.THIS_URI;
		URI relative = wellknown.relativize(new URI(Util.THIS_STRING + "234"));
		
		String base = "http://ex.org/";
		
		URI uriToResolveAgainst = new URI(base + "123");
		
		URI resolvedURI = Util.properlyResolve(uriToResolveAgainst, relative);
		
		assertEquals(new URI(base + 234),resolvedURI);
	}
	
	@Test
	public void resolvingTestURIotherDirectory() throws URISyntaxException {
		URI wellknown = Util.THIS_URI;
		URI relative = wellknown.relativize(new URI(Util.THIS_STRING + "234"));
		
		String base = "http://ex.org/";
		
		URI uriToResolveAgainst = new URI(base + "123/");
		
		URI resolvedURI = Util.properlyResolve(uriToResolveAgainst, relative);
		
		assertEquals(new URI(base + "123/234"),resolvedURI);
	}
	
	@Ignore("known issue: relativising \"up\" the path does not work (Java Bug #6226081)")
	@Test
	public void resolvingTestURIotherDirectoryTowardsRoot() throws URISyntaxException {
		URI wellknown = Util.THIS_URI;
		URI relative = wellknown.relativize(new URI(Util.THIS_SCHEME_AND_AUTHORITY + "r/1#t0"));
		
		String base = "http://ex.org/";
		
		URI uriToResolveAgainst = new URI(base + "123/");
		
		URI resolvedURI = Util.properlyResolve(uriToResolveAgainst, relative);
		
		assertEquals(new URI(base + "r/1#t0"),resolvedURI);
	}
}
