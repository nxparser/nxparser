package org.semanticweb.yars.nx.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)

public class NTriplesTestSuite extends TestCase {
	static final String INPUT = "src/test/resources/ntriples/w3c-testcase.nt";
	ArrayList<Object[]> data = new ArrayList<Object[]>();
	
	String line;
	 
	public NTriplesTestSuite(String line) {
		setName(line);
		this.line = line;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(INPUT));
		
			
			String line = null;
			while((line = br.readLine())!=null){
				if(!line.trim().isEmpty())
					data.add(new String[]{line});
			}
			br.close();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		return data;
	}
	
	@Test
	public void pushParseTest(){
		try{
			NxParser.parseNodes(this.line);
		} catch(Exception e){
			if(!this.line.trim().startsWith("#")){
				fail("Could not parse '"+line+"'");
			}
		}
	}
}
