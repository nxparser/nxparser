package org.semanticweb.yars.nx.parser;

/**
 * The interface for parsers with callbacks (push).
 * 
 * The general constructor for parsers should include:
 * RdfParser(InputStream is, URI base)
 * RdfParser(InputStream is, Charset charset, URI base)
 * RdfParser(Reader r, URI base)
 * 
 * If you want to explicitely specify a charset, please use the Reader constructor.
 * 
 * Parsers for RDF return triples (Node[3] objects).
 * 
 * The NxParser may return n-tuples - Node[n] objects.
 * 
 * In the parse loop, there should be a check for the interrupt flag of the thread:
 * 
 *  if (Thread.currentThread().isInterrupted()) {
 *       throw new InterruptedException();
 *  }
 * 
 * @author aharth, Tobias Kaefer
 */
public interface RdfParser {
	/** register error handler */
	public void setErrorHandler(ErrorHandler eh);
	/** start parsing and invoke methods on callback, report errors to error handler if there is one registered */
	public void parse(Callback cb) throws InterruptedException;
}