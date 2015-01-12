package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class HTTP {
	public final static String NS = "http://www.w3.org/2006/http#";

	//headers
	public final static Resource CONNECTION = new Resource(NS+"Connection");
	
	//HTTP 1.1
	public final static Resource ACCEPT = new Resource(NS+"accept");
	public final static Resource ACCEPT_CHARSET = new Resource(NS+"accept-charset");
	public final static Resource ACCEPT_ENCODING = new Resource(NS+"accept-encoding");
	public final static Resource ACCEPT_LANGUAGE = new Resource(NS+"accept-language");
	public final static Resource ACCEPT_RANGES = new Resource(NS+"accept-ranges");
	public final static Resource AGE = new Resource(NS+"age");
	public final static Resource ALLOW = new Resource(NS+"allow");
	public final static Resource AUTHORIZATION = new Resource(NS+"authorization");
	public final static Resource CACHE_CONTROL = new Resource(NS+"cache-control");
	public final static Resource CONNECTION_ = new Resource(NS+"connection");
	public final static Resource CONTENT_ENCODING = new Resource(NS+"content-encoding");
	public final static Resource CONTENT_LANGUAGE = new Resource(NS+"content-language");
	public final static Resource CONTENT_LENGTH = new Resource(NS+"content-length");
	public final static Resource CONTENT_LOCATION = new Resource(NS+"content-location");
	public final static Resource CONTENT_MD5 = new Resource(NS+"content-md5");
	public final static Resource CONTENT_RANGE = new Resource(NS+"content-range");
	public final static Resource CONTENT_TYPE = new Resource(NS+"content-type");
	public final static Resource DATE = new Resource(NS+"date");
	public final static Resource ETAG = new Resource(NS+"etag");
	public final static Resource EXPECT = new Resource(NS+"expect");
	public final static Resource EXPIRES = new Resource(NS+"expires");
	public final static Resource FROM = new Resource(NS+"from");
	public final static Resource HOST = new Resource(NS+"host");
	public final static Resource IF_MATCH = new Resource(NS+"if-match");
	public final static Resource IF_MODIFIED_SINCE = new Resource(NS+"if-modified-since");
	public final static Resource IF_NONE_MATCH = new Resource(NS+"if-none-match");
	public final static Resource IF_RANGE = new Resource(NS+"if-range");
	public final static Resource IF_UNMODIFIED_SINCE = new Resource(NS+"if-unmodified-since");
	public final static Resource LAST_MODIFIED = new Resource(NS+"last-modified");
	public final static Resource LOCATION = new Resource(NS+"location"); 
	public final static Resource MAX_FORWARDS = new Resource(NS+"max-forwards");
	public final static Resource MIME_VERSION = new Resource(NS+"mime-version");
	public final static Resource PRAGMA = new Resource(NS+"mime-version");
	public final static Resource PROXY_AUTHENTICATE = new Resource(NS+"proxy-authenticate");
	public final static Resource PROXY_AUTHORIZATION = new Resource(NS+"proxy-authorization");
	public final static Resource RANGE = new Resource(NS+"range");
	public final static Resource REFERER = new Resource(NS+"referer");
	public final static Resource RETRY_AFTER = new Resource(NS+"retry-after");
	public final static Resource SERVER = new Resource(NS+"server");
	public final static Resource TE = new Resource(NS+"te");
	public final static Resource TRAILER = new Resource(NS+"trailer");
	public final static Resource TRANSFER_ENCODING= new Resource(NS+"transfer-encoding");
	public final static Resource UPGRADE= new Resource(NS+"upgrade");
	public final static Resource USER_AGENT= new Resource(NS+"user-agent");
	public final static Resource VARY= new Resource(NS+"vary");
	public final static Resource VIA= new Resource(NS+"via");
	public final static Resource WARNING= new Resource(NS+"warning");
	public final static Resource WWW_AUTHENTICATE= new Resource(NS+"www-authenticate");
	
	/**
	 * @deprecated non-standard: use HTTP.RESPONSE_CODE
	 */
	public static final Resource STATUSCODE = new Resource(NS+"responseCode");
	
	//RFC2068
	public final static Resource CONTENT_BASE = new Resource(NS+"content-base");
	public final static Resource CONTENT_VERSION = new Resource(NS+"content-version");
	public final static Resource LINK = new Resource(NS+"link");
	public final static Resource PUBLIC = new Resource(NS+"public");
	public final static Resource URI = new Resource(NS+"uri");
	
	//RFC2109
	public final static Resource SET_COOKIE = new Resource(NS+"set-cookie");
	
	//PICSLabels
	public static final Resource PICS_LABEL = new Resource(NS+"pics-label");
	public static final Resource PROTOCOL = new Resource(NS+"protocol");
	public static final Resource PROTOCOL_REQUEST = new Resource(NS+"protocol-request");
}