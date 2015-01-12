package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class MC {
	public final static String NS = "http://sw.deri.org/2006/04/multicrawler/vocab.rdf#";
	
	//various linkgs
	public static final Resource HEAD_PROFILE_LINK = new Resource(NS+"head_profile_link");
	public static final Resource A_HREF_LINK = new Resource(NS+"a_href_link");
	public static final Resource IMG_SRC_LINK = new Resource(NS+"img_src_link");
	public static final Resource OBJECT__LINK = new Resource(NS+"object_link"); 
	public static final Resource LINK_HREF_LINK = new Resource(NS+"link_href_link"); 
	public static final Resource PARAM_VALUELINK = new Resource(NS+"param_value_link");
	public static final Resource FRAME_SRC_LINK = new Resource(NS+"frame_src_link");
	public static final Resource IFRAME_SRC_LINK = new Resource(NS+"iframe_src_link");
	public static final Resource AREA_HREF_LINK = new Resource(NS+"area_href_link");
	public static final Resource INPUT_SRC_LINK = new Resource(NS+"input_src_link");
	public static final Resource FORM_ACTION_LINK = new Resource(NS+"form_action_link");
	public static final Resource APPLECT_CODEBASE_LINK = new Resource(NS+"applet_codebase_link");

	public static final Resource IS_VALID_RDFXML = new Resource(NS+"isValidRDFXML");
	public static final Resource IS_WELL_FORMED_XML = new Resource(NS+"isWellFormedXML");
	
	public static final Resource MEDIATYPE = new Resource(NS+"mediaType");
	public static final Resource HEADER = new Resource(NS+"header");
	
}
