package org.semanticweb.yars.jaxrs;

import javax.ws.rs.core.MediaType;

public class MediaTypeDeterminer {
	/**
	 * Determines a {@link MediaType} based on file extensions.
	 * 
	 * @param path
	 *            The path of the file
	 * @return The file's {@link MediaType} if it is in my list, or
	 *         {@link MediaType.APPLICATION_OCTET_STREAM_TYPE} otherwise.
	 */
	public static MediaType determineMediaTypeBasedOnFileExtension(String path) {
		String extension = path.toLowerCase().substring(path.lastIndexOf(".") + 1);

		final MediaType mt;

		// Determining the Media Type looking at the path's extension
		switch (extension) {
		case "png":
			mt = MediaType.valueOf("image/png");
			break;
		case "jpg":
		case "jpeg":
		case "jpe":
			mt = MediaType.valueOf("image/jpeg");
			break;
		case "properties":
		case "txt":
			mt = MediaType.TEXT_PLAIN_TYPE;
			break;
		case "js":
			mt = MediaType.valueOf("text/javascript");
			break;
		case "json":
			mt = MediaType.APPLICATION_JSON_TYPE;
			break;
		case "htm":
		case "html":
			mt = MediaType.TEXT_HTML_TYPE;
			break;
		case "css":
			mt = MediaType.valueOf("text/css");
			break;
		case "woff":
			mt = MediaType.valueOf("application/font-woff");
			break;
		case "eot":
			mt = MediaType.valueOf("application/vnd.ms-fontobject");
			break;
		case "otf":
			mt = MediaType.valueOf("application/font-sfnt");
			break;
		case "ttf":
			mt = MediaType.valueOf("application/x-font-ttf");
			break;
		case "svg":
			mt = MediaType.APPLICATION_SVG_XML_TYPE;
			break;
		case "xml":
			mt = MediaType.APPLICATION_XML_TYPE;
			break;
		case "ttl":
			mt = NxMessageBodyReaderWriter.TURTLE_MEDIATYPE;
			break;
		case "jsonld":
			mt = JsonLdMessageBodyReaderWriter.JSONLD_MEDIATYPE;
			break;
		case "nt":
			mt = NxMessageBodyReaderWriter.NTRIPLES_MEDIATYPE;
			break;
		case "rdf":
			mt = RdfXmlMessageBodyWriter.RDF_XML_MEDIATYPE;
			break;
		default:
			mt = MediaType.APPLICATION_OCTET_STREAM_TYPE;
			break;
		}
		return mt;
	}
}
