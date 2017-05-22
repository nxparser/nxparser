package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

/**
 * The HTTP headers from the HTTP-in-RDF 1.0 vocabulary.
 * 
 * @see <a href="http://www.w3.org/TR/HTTP-in-RDF10/">The corresponding Working
 *      Draft at the W3C</a>
 *
 */
public class HTTPHEADERS {

	// Generated using
	// $ rapper http://www.w3.org/2011/http-headers | awk 'FS=" " { print $1 }
	// '| sort -u | grep 'http-headers' | awk '{ sub(".*#", ""); sub(">","");
	// sav = $0; gsub("-","_");c=toupper($0);print "public static final
	// Resource", c, "= new Resource(NS +", "\"" sav "\");" ;}'

	public final static String NS = "http://www.w3.org/2011/http-headers#";

	public static final Resource ACCEPT = new Resource(NS + "accept");
	public static final Resource ACCEPT_ADDITIONS = new Resource(NS + "accept-additions");
	public static final Resource ACCEPT_CHARSET = new Resource(NS + "accept-charset");
	public static final Resource ACCEPT_ENCODING = new Resource(NS + "accept-encoding");
	public static final Resource ACCEPT_FEATURES = new Resource(NS + "accept-features");
	public static final Resource ACCEPT_LANGUAGE = new Resource(NS + "accept-language");
	public static final Resource ACCEPT_RANGES = new Resource(NS + "accept-ranges");
	public static final Resource ACCESS_CONTROL = new Resource(NS + "access-control");
	public static final Resource ACCESS_CONTROL_ALLOW_CREDENTIALS = new Resource(
			NS + "access-control-allow-credentials");
	public static final Resource ACCESS_CONTROL_ALLOW_HEADERS = new Resource(NS + "access-control-allow-headers");
	public static final Resource ACCESS_CONTROL_ALLOW_METHODS = new Resource(NS + "access-control-allow-methods");
	public static final Resource ACCESS_CONTROL_ALLOW_ORIGIN = new Resource(NS + "access-control-allow-origin");
	public static final Resource ACCESS_CONTROL_MAX_AGE = new Resource(NS + "access-control-max-age");
	public static final Resource ACCESS_CONTROL_REQUEST_HEADERS = new Resource(NS + "access-control-request-headers");
	public static final Resource ACCESS_CONTROL_REQUEST_METHOD = new Resource(NS + "access-control-request-method");
	public static final Resource AGE = new Resource(NS + "age");
	public static final Resource A_IM = new Resource(NS + "a-im");
	public static final Resource ALLOW = new Resource(NS + "allow");
	public static final Resource ALTERNATES = new Resource(NS + "alternates");
	public static final Resource APPLY_TO_REDIRECT_REF = new Resource(NS + "apply-to-redirect-ref");
	public static final Resource AUTHENTICATION_INFO = new Resource(NS + "authentication-info");
	public static final Resource AUTHORIZATION = new Resource(NS + "authorization");
	public static final Resource CACHE_CONTROL = new Resource(NS + "cache-control");
	public static final Resource C_EXT = new Resource(NS + "c-ext");
	public static final Resource C_MAN = new Resource(NS + "c-man");
	public static final Resource COMPLIANCE = new Resource(NS + "compliance");
	public static final Resource CONNECTION = new Resource(NS + "connection");
	public static final Resource CONTENT_BASE = new Resource(NS + "content-base");
	public static final Resource CONTENT_DISPOSITION = new Resource(NS + "content-disposition");
	public static final Resource CONTENT_ENCODING = new Resource(NS + "content-encoding");
	public static final Resource CONTENT_ID = new Resource(NS + "content-id");
	public static final Resource CONTENT_LANGUAGE = new Resource(NS + "content-language");
	public static final Resource CONTENT_LENGTH = new Resource(NS + "content-length");
	public static final Resource CONTENT_LOCATION = new Resource(NS + "content-location");
	public static final Resource CONTENT_MD5 = new Resource(NS + "content-md5");
	public static final Resource CONTENT_RANGE = new Resource(NS + "content-range");
	public static final Resource CONTENT_SCRIPT_TYPE = new Resource(NS + "content-script-type");
	public static final Resource CONTENT_STYLE_TYPE = new Resource(NS + "content-style-type");
	public static final Resource CONTENT_TRANSFER_ENCODING = new Resource(NS + "content-transfer-encoding");
	public static final Resource CONTENT_TYPE = new Resource(NS + "content-type");
	public static final Resource CONTENT_VERSION = new Resource(NS + "content-version");
	public static final Resource COOKIE = new Resource(NS + "cookie");
	public static final Resource COOKIE2 = new Resource(NS + "cookie2");
	public static final Resource C_OPT = new Resource(NS + "c-opt");
	public static final Resource COST = new Resource(NS + "cost");
	public static final Resource C_PEP = new Resource(NS + "c-pep");
	public static final Resource C_PEP_INFO = new Resource(NS + "c-pep-info");
	public static final Resource DASL = new Resource(NS + "dasl");
	public static final Resource DATE = new Resource(NS + "date");
	public static final Resource DAV = new Resource(NS + "dav");
	public static final Resource DEFAULT_STYLE = new Resource(NS + "default-style");
	public static final Resource DELTA_BASE = new Resource(NS + "delta-base");
	public static final Resource DEPTH = new Resource(NS + "depth");
	public static final Resource DERIVED_FROM = new Resource(NS + "derived-from");
	public static final Resource DESTINATION = new Resource(NS + "destination");
	public static final Resource DIFFERENTIAL_ID = new Resource(NS + "differential-id");
	public static final Resource DIGEST = new Resource(NS + "digest");
	public static final Resource ETAG = new Resource(NS + "etag");
	public static final Resource EXPECT = new Resource(NS + "expect");
	public static final Resource EXPIRES = new Resource(NS + "expires");
	public static final Resource EXT = new Resource(NS + "ext");
	public static final Resource FROM = new Resource(NS + "from");
	public static final Resource GETPROFILE = new Resource(NS + "getprofile");
	public static final Resource HOST = new Resource(NS + "host");
	public static final Resource IF = new Resource(NS + "if");
	public static final Resource IF_MATCH = new Resource(NS + "if-match");
	public static final Resource IF_MODIFIED_SINCE = new Resource(NS + "if-modified-since");
	public static final Resource IF_NONE_MATCH = new Resource(NS + "if-none-match");
	public static final Resource IF_RANGE = new Resource(NS + "if-range");
	public static final Resource IF_UNMODIFIED_SINCE = new Resource(NS + "if-unmodified-since");
	public static final Resource IM = new Resource(NS + "im");
	public static final Resource KEEP_ALIVE = new Resource(NS + "keep-alive");
	public static final Resource LABEL = new Resource(NS + "label");
	public static final Resource LAST_MODIFIED = new Resource(NS + "last-modified");
	public static final Resource LINK = new Resource(NS + "link");
	public static final Resource LOCATION = new Resource(NS + "location");
	public static final Resource LOCK_TOKEN = new Resource(NS + "lock-token");
	public static final Resource MAN = new Resource(NS + "man");
	public static final Resource MAX_FORWARDS = new Resource(NS + "max-forwards");
	public static final Resource MESSAGE_ID = new Resource(NS + "message-id");
	public static final Resource METER = new Resource(NS + "meter");
	public static final Resource METHOD_CHECK = new Resource(NS + "method-check");
	public static final Resource METHOD_CHECK_EXPIRES = new Resource(NS + "method-check-expires");
	public static final Resource MIME_VERSION = new Resource(NS + "mime-version");
	public static final Resource NEGOTIATE = new Resource(NS + "negotiate");
	public static final Resource NON_COMPLIANCE = new Resource(NS + "non-compliance");
	public static final Resource OPT = new Resource(NS + "opt");
	public static final Resource OPTIONAL = new Resource(NS + "optional");
	public static final Resource ORDERING_TYPE = new Resource(NS + "ordering-type");
	public static final Resource ORIGIN = new Resource(NS + "origin");
	public static final Resource OVERWRITE = new Resource(NS + "overwrite");
	public static final Resource P3P = new Resource(NS + "p3p");
	public static final Resource PEP = new Resource(NS + "pep");
	public static final Resource PEP_INFO = new Resource(NS + "pep-info");
	public static final Resource PICS_LABEL = new Resource(NS + "pics-label");
	public static final Resource POSITION = new Resource(NS + "position");
	public static final Resource PRAGMA = new Resource(NS + "pragma");
	public static final Resource PROFILEOBJECT = new Resource(NS + "profileobject");
	public static final Resource PROTOCOL = new Resource(NS + "protocol");
	public static final Resource PROTOCOL_INFO = new Resource(NS + "protocol-info");
	public static final Resource PROTOCOL_QUERY = new Resource(NS + "protocol-query");
	public static final Resource PROTOCOL_REQUEST = new Resource(NS + "protocol-request");
	public static final Resource PROXY_AUTHENTICATE = new Resource(NS + "proxy-authenticate");
	public static final Resource PROXY_AUTHENTICATION_INFO = new Resource(NS + "proxy-authentication-info");
	public static final Resource PROXY_AUTHORIZATION = new Resource(NS + "proxy-authorization");
	public static final Resource PROXY_FEATURES = new Resource(NS + "proxy-features");
	public static final Resource PROXY_INSTRUCTION = new Resource(NS + "proxy-instruction");
	public static final Resource PUBLIC = new Resource(NS + "public");
	public static final Resource RANGE = new Resource(NS + "range");
	public static final Resource REDIRECT_REF = new Resource(NS + "redirect-ref");
	public static final Resource REFERER = new Resource(NS + "referer");
	public static final Resource REFERER_ROOT = new Resource(NS + "referer-root");
	public static final Resource RESOLUTION_HINT = new Resource(NS + "resolution-hint");
	public static final Resource RESOLVER_LOCATION = new Resource(NS + "resolver-location");
	public static final Resource RETRY_AFTER = new Resource(NS + "retry-after");
	public static final Resource SAFE = new Resource(NS + "safe");
	public static final Resource SECURITY_SCHEME = new Resource(NS + "security-scheme");
	public static final Resource SERVER = new Resource(NS + "server");
	public static final Resource SET_COOKIE = new Resource(NS + "set-cookie");
	public static final Resource SET_COOKIE2 = new Resource(NS + "set-cookie2");
	public static final Resource SETPROFILE = new Resource(NS + "setprofile");
	public static final Resource SLUG = new Resource(NS + "slug");
	public static final Resource SOAPACTION = new Resource(NS + "soapaction");
	public static final Resource STATUS_URI = new Resource(NS + "status-uri");
	public static final Resource SUBOK = new Resource(NS + "subok");
	public static final Resource SUBST = new Resource(NS + "subst");
	public static final Resource SURROGATE_CAPABILITY = new Resource(NS + "surrogate-capability");
	public static final Resource SURROGATE_CONTROL = new Resource(NS + "surrogate-control");
	public static final Resource TCN = new Resource(NS + "tcn");
	public static final Resource TE = new Resource(NS + "te");
	public static final Resource TIMEOUT = new Resource(NS + "timeout");
	public static final Resource TITLE = new Resource(NS + "title");
	public static final Resource TRAILER = new Resource(NS + "trailer");
	public static final Resource TRANSFER_ENCODING = new Resource(NS + "transfer-encoding");
	public static final Resource UA_COLOR = new Resource(NS + "ua-color");
	public static final Resource UA_MEDIA = new Resource(NS + "ua-media");
	public static final Resource UA_PIXELS = new Resource(NS + "ua-pixels");
	public static final Resource UA_RESOLUTION = new Resource(NS + "ua-resolution");
	public static final Resource UA_WINDOWPIXELS = new Resource(NS + "ua-windowpixels");
	public static final Resource UPGRADE = new Resource(NS + "upgrade");
	public static final Resource URI = new Resource(NS + "uri");
	public static final Resource USER_AGENT = new Resource(NS + "user-agent");
	public static final Resource VARIANT_VARY = new Resource(NS + "variant-vary");
	public static final Resource VARY = new Resource(NS + "vary");
	public static final Resource VERSION = new Resource(NS + "version");
	public static final Resource VIA = new Resource(NS + "via");
	public static final Resource WANT_DIGEST = new Resource(NS + "want-digest");
	public static final Resource WARNING = new Resource(NS + "warning");
	public static final Resource WWW_AUTHENTICATE = new Resource(NS + "www-authenticate");
	public static final Resource X_DEVICE_ACCEPT = new Resource(NS + "x-device-accept");
	public static final Resource X_DEVICE_ACCEPT_CHARSET = new Resource(NS + "x-device-accept-charset");
	public static final Resource X_DEVICE_ACCEPT_ENCODING = new Resource(NS + "x-device-accept-encoding");
	public static final Resource X_DEVICE_ACCEPT_LANGUAGE = new Resource(NS + "x-device-accept-language");
	public static final Resource X_DEVICE_USER_AGENT = new Resource(NS + "x-device-user-agent");
}