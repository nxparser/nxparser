package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class GEO {
	public static final String NS = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	
	public final static Resource POINT = new Resource(NS+"Point");
	public final static Resource SPATIAL_THING = new Resource(NS+"SpatialThing");

	public final static Resource LAT = new Resource(NS+"lat");
	public final static Resource LONG = new Resource(NS+"long");
	public final static Resource ALT = new Resource(NS+"alt");
}
