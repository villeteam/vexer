package edu.vserver.exercises.math.essentials.level;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class LevelXMLHelper {

	protected Document document;

	private DocumentBuilder builder;

	private final ByteArrayOutputStream data = new ByteArrayOutputStream();

	public LevelXMLHelper() {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();

		builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

	public DocumentBuilder getDocumentBuilder() {
		return builder;
	}

	public LevelXMLHelper(byte[] readFrom) {

		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(readFrom));
		} catch (SAXException sxe) {
			// Error generated during parsing
			Exception x = sxe;
			if (sxe.getException() != null) {
				x = sxe.getException();
			}
			x.printStackTrace();

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();

		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}
		document = doc;

	}

	public byte[] writeXMLtoStream() {
		try {

			Source src = new DOMSource(document);

			Result result = new StreamResult(data);

			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(src, result);

			return data.toByteArray();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Document getDocument() {
		return document;
	}

	public void createLevels() {
		document.appendChild(document.createElement("GameSettings"));
		document.getFirstChild().appendChild(document.createElement("Easy"));
		document.getFirstChild().appendChild(document.createElement("Normal"));
		document.getFirstChild().appendChild(document.createElement("Hard"));
	}

}
