package org.semanticweb.yars.nx;

import java.net.URI;

public abstract class RDFTestCase {
	private URI _uri;
	private String _name;
	private String _comment;
	private URI _action;

	public URI getUri() {
		return _uri;
	}

	public String getName() {
		return _name;
	}

	public String getComment() {
		return _comment;
	}

	public URI getAction() {
		return _action;
	}

	public RDFTestCase(URI uri, String name, String comment, URI action) {
		_action = action;
		_uri = uri;
		_name = name;
		_comment = comment;
	}

	public static class TestCasePositive extends RDFTestCase {

		public TestCasePositive(URI uri, String name, String comment,
				URI action) {
			super(uri, name, comment, action);
		}
		
	}

	public static class TestCaseNegative extends RDFTestCase {
		public TestCaseNegative(URI uri, String name, String comment, URI action) {
			super(uri, name, comment, action);
		}
	}

	public static class TestCaseEval extends RDFTestCase {
		URI _result;

		public TestCaseEval(URI uri, String name, String comment, URI action,
				URI result) {
			super(uri, name, comment, action);
			_result = result;
		}
		
		public URI getResult() {
			return _result;
		}
	}
}
