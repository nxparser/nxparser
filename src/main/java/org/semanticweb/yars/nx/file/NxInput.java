package org.semanticweb.yars.nx.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;

/**
 * Class which implements a resetable iterator 
 * of statements for an input nx file.
 * 
 * @author Aidan Hogan
 * @date 2009-10-01
 */
public class NxInput extends FileInput {
	public static int BUFFER = 1024*8;
	
	/**
	 * Constructor
	 * @param file Filename of input Nx file
	 * @throws ParseException If error parsing Nx
	 */
	public NxInput(File file) throws ParseException{
		super(file);
	}
	
	protected Iterator<Node[]> openFile(){
		try{
			FileInputStream fis = new FileInputStream(_f);
			BufferedInputStream bis = new BufferedInputStream(fis, BUFFER);
			NxParser nxp = new NxParser(bis);
			toClose(bis);
			return nxp;
		} catch(Exception ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	/**
	 * File input
	 * @throws ParseException
	 */
	public NxInput copyOf() throws ParseException{
		return new NxInput(_f);
	}
}
