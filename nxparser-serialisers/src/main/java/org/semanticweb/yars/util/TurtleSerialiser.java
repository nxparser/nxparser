package org.semanticweb.yars.util;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.parser.Callback;

public class TurtleSerialiser extends Callback {

	final Logger _log = Logger.getLogger(TurtleSerialiser.class.getName());

	final Map<Node, Map<Node, Set<Node>>> _spo;
	final Map<Node, Map<Node, Set<Node>>> _ops;

	final Appendable _app;

	public TurtleSerialiser(Appendable app) {
		_app = app;
		_spo = new HashMap<Node, Map<Node, Set<Node>>>();
		_ops = new HashMap<Node, Map<Node, Set<Node>>>();
	}

	@Override
	protected void startDocumentInternal() {
	}

	@Override
	protected void processStatementInternal(Node[] nx) {

		Map<Node, Set<Node>> innerMap = _spo.get(nx[0]);
		Set<Node> innerSet;
		if (innerMap == null) {
			innerMap = new HashMap<Node, Set<Node>>();
			innerMap.put(nx[1], new HashSet<Node>(Arrays.asList(new Node[] { nx[2] })));
			_spo.put(nx[0], innerMap);
		} else {
			innerSet = innerMap.get(nx[1]);
			if (innerSet == null)
				innerMap.put(nx[1], new HashSet<Node>(Arrays.asList(new Node[] { nx[2] })));
			else
				innerSet.add(nx[2]);
		}

		innerMap = _ops.get(nx[2]);
		if (innerMap == null) {
			innerMap = new HashMap<Node, Set<Node>>();
			innerMap.put(nx[1], new HashSet<Node>(Arrays.asList(new Node[] { nx[0] })));
			_ops.put(nx[2], innerMap);
		} else {
			innerSet = innerMap.get(nx[1]);
			if (innerSet == null)
				innerMap.put(nx[1], new HashSet<Node>(Arrays.asList(new Node[] { nx[0] })));
			else
				innerSet.add(nx[0]);
		}

	}

	@Override
	protected void endDocumentInternal() {

		// collect lists
		Map<Node, Deque<Node>> d = new HashMap<Node, Deque<Node>>();

		Set<Nodes> consideredStatements = new HashSet<Nodes>();

		Map<Node, Set<Node>> ps = _ops.get(RDF.NIL);

		// all triples ending lists
		if (ps.containsKey(RDF.REST))
			for (Node s : ps.get(RDF.REST)) {
				d.put(s, new LinkedList<Node>());

				Node currentListHead = s;

				Set<Nodes> innerConsideredStatements = new HashSet<Nodes>();
				innerConsideredStatements.add(new Nodes(new Node[] { s, RDF.REST, RDF.NIL }));

				while (_spo.containsKey(currentListHead) && _spo.get(currentListHead).containsKey(RDF.FIRST)) {
					Set<Node> elementSet = _spo.get(currentListHead).get(RDF.FIRST);
					if (elementSet == null || elementSet.size() > 1 || elementSet.isEmpty()) {
						_log.warning("Malformed list...");
						break;
					}
					Node element = elementSet.iterator().next();
					d.get(s).addFirst(element);
					Set<Node> nextListHeadCandidates = _ops.get(currentListHead).get(RDF.REST);
					if (nextListHeadCandidates == null)
						// at the end of the list
						break;
					if (nextListHeadCandidates.size() > 1 || nextListHeadCandidates.isEmpty()) {
						_log.warning("Malformed list...");
						break;
					}
					currentListHead = nextListHeadCandidates.iterator().next();
				}

				System.out.println(d);
			}

	}

}
