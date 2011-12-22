package org.semanticweb.yars.nx.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.util.ResetableIterator;

/**
 * Wrapper class which implements some default behaviour for
 * a resetable iterator of statements for an input file.
 * 
 * @author Aidan Hogan
 * @date 2009-10-01
 */
public abstract class FileInput implements ResetableIterator<Node[]> {
	protected File _f;
	protected Iterator<Node[]> _in;
	protected InputStream _is;
	
	protected FileInput(){;}
	/**
	 * Constructor
	 * @param file File containing input statements
	 * @throws ParseException If error parsing statements
	 */
	public FileInput(File file) throws ParseException{
		_f = file;
		_in = openFile();
	}
	
	/**
	 * Opens the file and returns an iterator over the contained
	 * statements.
	 * @return Iterator over the contained statements
	 */
	protected abstract Iterator<Node[]> openFile();
	
	/**
	 * Reset the iterator of statements to the start
	 */
	public void reset() {
		_in = openFile();
	}
	
	/**
	 * Set input stream to close
	 */
	protected void toClose(InputStream is){
		_is = is;
	}

	/**
	 * Has more statements to read
	 */
	public boolean hasNext() {
		return _in.hasNext();
	}

	/**
	 * Next statement to read
	 */
	public Node[] next() {
		return _in.next();
	}

	/**
	 * Not usually supported although part of standard
	 * iterator interface
	 */
	public void remove() {
		_in.remove();
	}
	
	/**
	 * Close input file
	 * @throws IOException 
	 */
	public void close() throws IOException {
		_is.close();
	}
	
	/**
	 * File input
	 */
	public abstract FileInput copyOf()  throws ParseException, IOException;
}
