package org.semanticweb.yars.parsers.turtle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.RiotParseException;
import org.apache.jena.riot.lang.LangTurtle;
import org.apache.jena.riot.lang.PipedRDFIterator;
import org.apache.jena.riot.lang.PipedTriplesStream;
import org.apache.jena.riot.system.RiotLib;
import org.apache.jena.riot.tokens.TokenizerFactory;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;

import com.hp.hpl.jena.graph.Node_Blank;
import com.hp.hpl.jena.graph.Node_Literal;
import com.hp.hpl.jena.graph.Node_URI;
import com.hp.hpl.jena.graph.Triple;

public class TurtleParser implements Iterator<Node[]>, Iterable<Node[]> {

	private static Logger _log = Logger.getLogger(TurtleParser.class.getName());

	PipedRDFIterator<Triple> _tripleIterator;
	String _baseURI;
	Resource _resBaseURI;

	public TurtleParser() {

	}

	public void parse(InputStream is, String baseURI, Charset cs) {
		_tripleIterator = new PipedRDFIterator<Triple>();
		_baseURI = baseURI;
		_resBaseURI = new Resource(_baseURI, false);

		LangTurtle lt = new LangTurtle(
				TokenizerFactory.makeTokenizer(new BufferedReader(
						new InputStreamReader(is, cs))), RiotLib.profile(
						RDFLanguages.TURTLE, baseURI), new PipedTriplesStream(
						_tripleIterator));
		try {
			lt.parse();
		} catch (RiotParseException e1) {
			_log.warning("Moving on to the next line, as I couldn't parse line "
					+ e1.getLine()
					+ ". Error at col "
					+ e1.getCol()
					+ ": "
					+ e1.getMessage());
		} catch (RiotException e) {
			_log.warning("Moving on to the next line, as I couldn't parse line whatever. Message: "
					+ e.getMessage());
		}
	}

	@Override
	public Iterator<Node[]> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return _tripleIterator.hasNext();
	}

	@Override
	public Node[] next() {
		Triple q = _tripleIterator.next();
		return new Node[] { jenaToNxParser(q.getSubject()),
				jenaToNxParser(q.getPredicate()),
				jenaToNxParser(q.getObject()), _resBaseURI };

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public org.semanticweb.yars.nx.Node jenaToNxParser(
			com.hp.hpl.jena.graph.Node jenaNode) {
		if (jenaNode instanceof Node_URI)
			return new Resource(jenaNode.getURI(), false);
		else if (jenaNode instanceof Node_Blank)
			return BNode.createBNode(_baseURI, jenaNode.getBlankNodeLabel());
		else if (jenaNode instanceof Node_Literal) {
			return new Literal(jenaNode.getLiteralLexicalForm(),
					jenaNode.getLiteralLanguage() == null ? null : jenaNode
							.getLiteralLanguage().equals("") ? null : jenaNode
							.getLiteralLanguage(),
					jenaNode.getLiteralDatatypeURI() == null ? null : jenaNode
							.getLiteralDatatypeURI().equals("") ? null
							: new Resource(jenaNode.getLiteralDatatypeURI(),
									false));
		} else
			// shouldn't happen
			return null;

	}

}
