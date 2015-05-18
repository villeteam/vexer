package edu.vserver.exercises.math.essentials.level;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.standardutils.TempFilesManager;

public class LevelXML<E extends ExerciseData, S extends SubmissionInfo>
		implements PersistenceHandler<LevelMathDataWrapper<E>, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -114630597593544006L;

	protected Document document;

	private final PersistenceHandler<E, S> specificXMLWriter;

	public LevelXML(PersistenceHandler<E, S> specificXMLWriter) {
		this.specificXMLWriter = specificXMLWriter;
	}

	@Override
	public byte[] saveExerData(LevelMathDataWrapper<E> data,
			TempFilesManager tempMan, ByRefSaver refHandler)
			throws ExerciseException {

		LevelXMLHelper handler = new LevelXMLHelper();
		Document document = handler.getDocument();
		DocumentBuilder builder = handler.getDocumentBuilder();

		handler.createLevels();

		E easy = data.getForLevel(DiffLevel.EASY);
		E normal = data.getForLevel(DiffLevel.NORMAL);
		E hard = data.getForLevel(DiffLevel.HARD);

		String easyString = convertStreamToString(specificXMLWriter
				.saveExerData(easy, tempMan, refHandler));
		String normalString = convertStreamToString(specificXMLWriter
				.saveExerData(normal, tempMan, refHandler));
		String hardString = convertStreamToString(specificXMLWriter
				.saveExerData(hard, tempMan, refHandler));

		NodeList list = document.getElementsByTagName("Easy");

		try {
			appendXmlFragment(builder, list.item(0), easyString);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		list = document.getElementsByTagName("Normal");
		try {
			appendXmlFragment(builder, list.item(0), normalString);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		}

		list = document.getElementsByTagName("Hard");
		try {
			appendXmlFragment(builder, list.item(0), hardString);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return handler.writeXMLtoStream();

	}

	@Override
	public LevelMathDataWrapper<E> loadExerData(byte[] inStream,
			TempFilesManager tempMan, ByRefLoader refHandler)
			throws ExerciseException {

		if (inStream != null) {
			LevelXMLHelper handler = new LevelXMLHelper(inStream);
			document = handler.getDocument();

			NodeList easy = document.getElementsByTagName("Easy");

			LevelXMLHelper easyHandler = new LevelXMLHelper();
			easyHandler.getDocument().appendChild(
					easyHandler.getDocument().importNode(easy.item(0), true));

			NodeList normal = document.getElementsByTagName("Normal");
			LevelXMLHelper normalHandler = new LevelXMLHelper();

			normalHandler.getDocument().appendChild(
					normalHandler.getDocument()
							.importNode(normal.item(0), true));

			NodeList hard = document.getElementsByTagName("Hard");
			LevelXMLHelper hardHandler = new LevelXMLHelper();
			hardHandler.getDocument().appendChild(
					hardHandler.getDocument().importNode(hard.item(0), true));

			byte[] easyStream = easyHandler.writeXMLtoStream();
			byte[] normalStream = normalHandler.writeXMLtoStream();
			byte[] hardStream = hardHandler.writeXMLtoStream();

			LevelMathDataWrapper<E> levels = new LevelMathDataWrapper<E>(
					specificXMLWriter.loadExerData(easyStream, tempMan,
							refHandler), specificXMLWriter.loadExerData(
							normalStream, tempMan, refHandler),
					specificXMLWriter.loadExerData(hardStream, tempMan,
							refHandler));
			return levels;
		} else {
			return null;
		}
	}

	private String convertStreamToString(byte[] utf8Data) {
		String res = "";
		try {
			res = new String(utf8Data, "UTF-8");
		} catch (java.util.NoSuchElementException e) {
			res = "";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	private static void appendXmlFragment(DocumentBuilder docBuilder,
			Node parent, String fragment) throws IOException, SAXException {
		Document doc = parent.getOwnerDocument();
		Node fragmentNode = docBuilder.parse(
				new InputSource(new StringReader(fragment)))
				.getDocumentElement();
		fragmentNode = doc.importNode(fragmentNode, true);
		parent.appendChild(fragmentNode);
	}

	@Override
	public byte[] saveSubmission(S subm, TempFilesManager tempManager)
			throws ExerciseException {
		return specificXMLWriter.saveSubmission(subm, tempManager);
	}

	@Override
	public S loadSubmission(byte[] inStream, boolean forStatGiver,
			TempFilesManager tempManager) throws ExerciseException {
		return specificXMLWriter.loadSubmission(inStream, forStatGiver,
				tempManager);
	}

}
