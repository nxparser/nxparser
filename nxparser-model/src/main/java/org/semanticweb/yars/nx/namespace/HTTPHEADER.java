package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

/**
 * 
 * @deprecated The 2006 HTTP header vocabulary has been obsoleted by
 *             {@link HTTPHEADERS the 2011 HTTP 1.0 header vocabulary}.
 *
 */
@Deprecated
public class HTTPHEADER {
	
	@Deprecated
	public final static String NS = "http://www.w3.org/2006/http-header#";

	//headers
	@Deprecated
	public final static Resource HEADER_NAME = new Resource(NS+"HeaderName");
	
	//HTTP 1.1
	@Deprecated
	public final static Resource ACCEPT = new Resource(NS+"accept");
	@Deprecated
	public final static Resource ACCEPT_CHARSET = new Resource(NS+"accept-charset");
	@Deprecated
	public final static Resource ACCEPT_ENCODING = new Resource(NS+"accept-encoding");
	@Deprecated
	public final static Resource ACCEPT_LANGUAGE = new Resource(NS+"accept-language");
	@Deprecated
	public final static Resource ACCEPT_RANGES = new Resource(NS+"accept-ranges");
	@Deprecated
	public final static Resource AGE = new Resource(NS+"age");
	@Deprecated
	public final static Resource ALLOW = new Resource(NS+"allow");
	@Deprecated
	public final static Resource AUTHORIZATION = new Resource(NS+"authorization");
	@Deprecated
	public final static Resource CACHE_CONTROL = new Resource(NS+"cache-control");
	@Deprecated
	public final static Resource CONNECTION = new Resource(NS+"connection");
	@Deprecated
	public final static Resource CONTENT_ENCODING = new Resource(NS+"content-encoding");
	@Deprecated
	public final static Resource CONTENT_LANGUAGE = new Resource(NS+"content-language");
	@Deprecated
	public final static Resource CONTENT_LENGTH = new Resource(NS+"content-length");
	@Deprecated
	public final static Resource CONTENT_LOCATION = new Resource(NS+"content-location");
	@Deprecated
	public final static Resource CONTENT_MD5 = new Resource(NS+"content-md5");
	@Deprecated
	public final static Resource CONTENT_RANGE = new Resource(NS+"content-range");
	@Deprecated
	public final static Resource CONTENT_TYPE = new Resource(NS+"content-type");
	@Deprecated
	public final static Resource DATE = new Resource(NS+"date");
	@Deprecated
	public final static Resource ETAG = new Resource(NS+"etag");
	@Deprecated
	public final static Resource EXPECT = new Resource(NS+"expect");
	@Deprecated
	public final static Resource EXPIRES = new Resource(NS+"expires");
	@Deprecated
	public final static Resource FROM = new Resource(NS+"from");
	@Deprecated
	public final static Resource HOST = new Resource(NS+"host");
	@Deprecated
	public final static Resource IF_MATCH = new Resource(NS+"if-match");
	@Deprecated
	public final static Resource IF_MODIFIED_SINCE = new Resource(NS+"if-modified-since");
	@Deprecated
	public final static Resource IF_NONE_MATCH = new Resource(NS+"if-none-match");
	@Deprecated
	public final static Resource IF_RANGE = new Resource(NS+"if-range");
	@Deprecated
	public final static Resource IF_UNMODIFIED_SINCE = new Resource(NS+"if-unmodified-since");
	@Deprecated
	public final static Resource LAST_MODIFIED = new Resource(NS+"last-modified");
	@Deprecated
	public final static Resource LOCATION = new Resource(NS+"location"); 
	@Deprecated
	public final static Resource MAX_FORWARDS = new Resource(NS+"max-forwards");
	@Deprecated
	public final static Resource MIME_VERSION = new Resource(NS+"mime-version");
	@Deprecated
	public final static Resource PRAGMA = new Resource(NS+"mime-version");
	@Deprecated
	public final static Resource PROXY_AUTHENTICATE = new Resource(NS+"proxy-authenticate");
	@Deprecated
	public final static Resource PROXY_AUTHORIZATION = new Resource(NS+"proxy-authorization");
	@Deprecated
	public final static Resource RANGE = new Resource(NS+"range");
	@Deprecated
	public final static Resource REFERER = new Resource(NS+"referer");
	@Deprecated
	public final static Resource RETRY_AFTER = new Resource(NS+"retry-after");
	@Deprecated
	public final static Resource SERVER = new Resource(NS+"server");
	@Deprecated
	public final static Resource TE = new Resource(NS+"te");
	@Deprecated
	public final static Resource TRAILER = new Resource(NS+"trailer");
	@Deprecated
	public final static Resource TRANSFER_ENCODING= new Resource(NS+"transfer-encoding");
	@Deprecated
	public final static Resource UPGRADE= new Resource(NS+"upgrade");
	@Deprecated
	public final static Resource USER_AGENT= new Resource(NS+"user-agent");
	@Deprecated
	public final static Resource VARY= new Resource(NS+"vary");
	@Deprecated
	public final static Resource VIA= new Resource(NS+"via");
	@Deprecated
	public final static Resource WARNING= new Resource(NS+"warning");
	@Deprecated
	public final static Resource WWW_AUTHENTICATE= new Resource(NS+"www-authenticate");
	
	/**
	 * @deprecated non-standard: use HTTP.RESPONSE_CODE
	 */
	@Deprecated
	public static final Resource STATUSCODE = new Resource(NS+"responseCode");
	
	//RFC2068
	@Deprecated
	public final static Resource CONTENT_BASE = new Resource(NS+"content-base");
	@Deprecated
	public final static Resource CONTENT_VERSION = new Resource(NS+"content-version");
	@Deprecated
	public final static Resource LINK = new Resource(NS+"link");
	@Deprecated
	public final static Resource PUBLIC = new Resource(NS+"public");
	@Deprecated
	public final static Resource URI = new Resource(NS+"uri");
	
	//RFC2109
	@Deprecated
	public final static Resource SET_COOKIE = new Resource(NS+"set-cookie");
	
	//PICSLabels
	@Deprecated
	public static final Resource PICS_LABEL = new Resource(NS+"pics-label");
	@Deprecated
	public static final Resource PROTOCOL = new Resource(NS+"protocol");
	@Deprecated
	public static final Resource PROTOCOL_REQUEST = new Resource(NS+"protocol-request");
}