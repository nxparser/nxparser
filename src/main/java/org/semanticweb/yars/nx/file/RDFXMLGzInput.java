package org.semanticweb.yars.nx.file;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars2.rdfxml.RDFXMLParser;

/**
 * Class which implements a resetable iterator 
 * of statements for an input gzipped RDF/XML file.
 * 
 * @author Aidan Hogan
 * @date 2009-10-01
 */
public class RDFXMLGzInput extends FileInput {
	public static int BUFFER = 1024*8;
	private String _base;
	
	/**
	 * Constructor
	 * 
	 * @param file Filename of input gzipped RDF/XML
	 * @param baseURI Base-URI to use for resolving relative URIs
	 * @throws ParseException If error results from parsing
	 */
	public RDFXMLGzInput(File file, String baseURI) throws ParseException{
		super(file);
		_base = baseURI;
		_in = openFile();
	}
	
	protected Iterator<Node[]> openFile(){
		if(_base!=null) return openFile(_base);
		return null;
	}
	
	protected Iterator<Node[]> openFile(String base){
		try{
			FileInputStream fis = new FileInputStream(_f);
			GZIPInputStream gzis = new GZIPInputStream(fis, BUFFER);
			toClose(gzis);
			RDFXMLParser rxxp = new RDFXMLParser(gzis, base);
			return rxxp;
		} catch(Exception ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	public RDFXMLGzInput copyOf() throws ParseException {
		return new RDFXMLGzInput(_f, _base);
	}
}
