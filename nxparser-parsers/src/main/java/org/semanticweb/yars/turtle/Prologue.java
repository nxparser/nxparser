/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.yars.turtle;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.yars.nx.Resource;

/**
 * Keep track of prefixes and base URI.
 *
 * @author aharth
 */
public class Prologue {
	final static Logger _log = Logger.getLogger(Prologue.class.getName());

	Map<String, String> _prefixes;
    URI _baseURI = null;

	public Prologue() {
		_prefixes = new HashMap<String, String>();
	}

	public void setPrefix(String prefix, String s) {
		// strip <>
		s = s.substring(1, s.length()-1);

		// empty prefix
		if (s.length() == 0) {
			s = _baseURI.toString();
		}

    	URI u = URI.create(s);
        _log.log(Level.FINE, "Creating resource {0}, absolute? {1}", new Object[] { s, u.isAbsolute() });

    	if (!u.isAbsolute()) {
    		_log.log(Level.FINE, "Resolving {0} relative to {1}", new Object[] { u, getBaseURI() } );
    		u = getBaseURI().resolve(u);
    	}

		_log.log(Level.FINE, "@prefix {0}: {1}", new Object[] { prefix, u.toString() } );
		_prefixes.put(prefix, u.toString());
	}

	public String expandPrefix(String prefix) {
		return _prefixes.get(prefix);
	}

	/**
     * @return baseURI, if set.
     */
    public URI getBaseURI() {
    	return _baseURI;
    }

    /**
     * @param baseURI The baseURI to set.
     */
    public void setBaseURI(Resource base) {
    	setBase(URI.create(base.getLabel()));
    }

    public void setBase(URI base) {
		if (_baseURI == null) {
			// Setting the base URI for the first time. This happens when
			// parsing starts and we get the document URI.
			_baseURI = base;
			if (!_baseURI.isAbsolute()) {
				throw new IllegalArgumentException("Document URI " + base
						+ " needs to be absolute!");
			}
		} else {
			// relative base URIs within a document get resolved against the
			// current base URI.
			_baseURI = _baseURI.resolve(base);
		}

    }

}