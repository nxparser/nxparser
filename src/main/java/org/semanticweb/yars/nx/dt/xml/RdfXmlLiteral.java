package org.semanticweb.yars.nx.dt.xml;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.Datatype;
import org.semanticweb.yars.nx.dt.DatatypeParseException;
import org.semanticweb.yars.nx.namespace.RDF;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * Represents the xsd:hexBinary datatype
 * @author aidhog
 *
 */
		
public class RdfXmlLiteral extends Datatype<Document> {
	public static final Resource DT = RDF.XMLLITERAL;
	private Document _d = null;
	private boolean _empty = false;
	
	public RdfXmlLiteral(String s) throws DatatypeParseException{
		s = s.trim();
		
		if(s==null)
			throw new DatatypeParseException("Null value passed.",0);
		else if(s.isEmpty()){
			_empty = true;
			return;
		}
		
		s = "<xmlliteral>"+s+"</xmlliteral>";
		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
		try {
			DocumentBuilderFactory builderF = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderF.newDocumentBuilder();
			builder.setErrorHandler(new SuperAdvancedErrorHandler());
			
			_d = builder.parse(bais);
			_d.normalize();
		} catch (Exception e) {
			throw new DatatypeParseException("Error parsing DOM Document (XMLLiteral) : "+e.getMessage(), 2);
		}
	}

	public Document getValue() {
		return _d;
	}

	public String getCanonicalRepresentation() {
		if(_empty) return "";
		return xmlToString(_d.getFirstChild().getFirstChild()).substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());
	}
	
	private static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String args[]) throws DatatypeParseException{
		RdfXmlLiteral hex = new RdfXmlLiteral("<lah>asd<br />fkg</lah>");
		System.err.println(hex.getCanonicalRepresentation());
	}
	
	public static class SuperAdvancedErrorHandler implements ErrorHandler {

		public void error(SAXParseException arg0) throws SAXException {
			;
		}

		public void fatalError(SAXParseException arg0) throws SAXException {
			;
		}

		public void warning(SAXParseException arg0) throws SAXException {
			;
		}
		
	}
}
