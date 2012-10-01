package org.semanticweb.yars.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class ResetableFileIterator implements ResetableIterator<Node[]>{
	protected Iterator<Node[]> iter = null;
	
	InputStream is = null;
	String in = null;
	boolean gz = false;
	int ticks = 0;
	Exception e = null;
	
	public ResetableFileIterator(String in, boolean gz){
		this(in,gz,0);
	}
	
	public ResetableFileIterator(String in, boolean gz, int ticks){
		this.in = in;
		this.gz = gz;
		this.ticks = ticks;
		reset();
	}

	protected void resetFile() throws IOException {
		InputStream is = new FileInputStream(in);
		if(gz)
			is = new GZIPInputStream(is);
		iter = new NxParser(is);
		if(ticks>0)
			iter = new TicksIterator(iter,ticks);
	}

	public boolean hasNext() {
		return iter!=null && iter.hasNext();
	}

	public Node[] next() {
		if(!hasNext())
			throw new NoSuchElementException();
		return iter.next();
	}

	public void remove() {
		iter.remove();
	}
	
	public void close() throws IOException{
		if(is!=null)
			is.close();
	}

	public void reset() {
		iter = null;
		try{
			resetFile();
		} catch(Exception e){
			this.e = e;
		}
	}
	
	public Exception getException(){
		return e;
	}
	
}
