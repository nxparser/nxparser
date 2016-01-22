package org.semanticweb.yars.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.parser.ErrorHandler;

public class ErrorHandlerImpl implements ErrorHandler {
	public static Logger _log = Logger.getLogger(ErrorHandlerImpl.class.getName());

	Exception _fatal;
	List<Exception> _warnings = new ArrayList<Exception>();

	@Override
	public void fatalError(Exception e) {
		_fatal = e;

		_log.log(Level.WARNING, "Parsing failed: {0}: {1}", new Object[] { e.getClass(), e.getMessage() });
	}

	@Override
	public void warning(Exception e) {
		_warnings.add(e);

		_log.log(Level.INFO, "Parsing warning: {0}: {1}", new Object[] { e.getClass(), e.getMessage() });
	}

	public Exception getFatalError() {
		return _fatal;
	}

	public Collection<Exception> getWarnings() {
		return _warnings;
	}
}