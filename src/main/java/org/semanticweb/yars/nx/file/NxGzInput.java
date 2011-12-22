package org.semanticweb.yars.nx.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;

/**
 * Class which implements a resetable iterator 
 * of statements for an input G-Zipped Nx file.
 * 
 * @author Aidan Hogan
 * @date 2009-10-01
 */
public class NxGzInput extends FileInput {
	public static int BUFFER = 1024*8;
	
	/**
	 * Constructor
	 * @param file Filename of input G-Zipped Nx file
	 * @throws ParseException If error parsing Nx
	 */
	public NxGzInput(File file) throws ParseException{
		super(file);
	}
	
	protected Iterator<Node[]> openFile(){
		try{
			FileInputStream fis = new FileInputStream(_f);
			GZIPInputStream gzis = new GZIPInputStream(fis, BUFFER);
			toClose(gzis);
			NxParser nxp = new NxParser(gzis);
			return nxp;
		} catch(Exception ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	public NxGzInput copyOf() throws ParseException{
		return new NxGzInput(_f);
	}
}
