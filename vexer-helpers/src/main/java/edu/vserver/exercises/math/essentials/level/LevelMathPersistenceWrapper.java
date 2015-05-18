package edu.vserver.exercises.math.essentials.level;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fi.utu.ville.exercises.helpers.GsonPersistenceHandler;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.standardutils.TempFilesManager;
import fi.utu.ville.standardutils.XMLHelper;

/**
 * A persistencehandler wrapper to use with exercises that use
 * LevelMathDataWrapper. Internally it uses a GsonPersistenceHandler to convert
 * the difficulty levels to Json, which are combined into one XML file.
 * 
 * @param <E>
 *            ExerciseData to manage
 * @param <S>
 *            SubmissionInfo to manage
 */
public class LevelMathPersistenceWrapper<E extends ExerciseData, S extends SubmissionInfo>
		implements PersistenceHandler<LevelMathDataWrapper<E>, S> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2629452801442738865L;
	private final GsonPersistenceHandler<E, S> handler;

	public LevelMathPersistenceWrapper(Class<E> exerDataClass,
			Class<S> submInfoClass) {
		this.handler = new GsonPersistenceHandler<>(exerDataClass,
				submInfoClass);
	}

	@Override
	public byte[] saveExerData(
			LevelMathDataWrapper<E> etd,
			TempFilesManager tempManager,
			fi.utu.ville.exercises.model.PersistenceHandler.ByRefSaver matHandler)
			throws ExerciseException {

		PersistenceHandler<E, S> persistenceHandler = getPersistenceHandler();

		E easy = etd.getForLevel(DiffLevel.EASY);
		E normal = etd.getForLevel(DiffLevel.NORMAL);
		E hard = etd.getForLevel(DiffLevel.HARD);
		byte[] easyBuf = persistenceHandler.saveExerData(easy, tempManager,
				matHandler);
		byte[] normalBuf = persistenceHandler.saveExerData(normal, tempManager,
				matHandler);
		byte[] hardBuf = persistenceHandler.saveExerData(hard, tempManager,
				matHandler);

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("levels");

			CDATASection easyData = doc.createCDATASection(Arrays
					.toString(easyBuf));
			CDATASection normalData = doc.createCDATASection(Arrays
					.toString(normalBuf));
			CDATASection hardData = doc.createCDATASection(Arrays
					.toString(hardBuf));
			Element easyElement = doc.createElement("easy");
			easyElement.appendChild(easyData);
			Element normalElement = doc.createElement("normal");
			normalElement.appendChild(normalData);
			Element hardElement = doc.createElement("hard");
			hardElement.appendChild(hardData);

			rootElement.appendChild(easyElement);
			rootElement.appendChild(normalElement);
			rootElement.appendChild(hardElement);

			doc.appendChild(rootElement);
			return XMLHelper.xmlToBytes(doc);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		return new byte[] {};
	}

	@Override
	public LevelMathDataWrapper<E> loadExerData(
			byte[] dataPres,
			TempFilesManager tempManager,
			fi.utu.ville.exercises.model.PersistenceHandler.ByRefLoader matHandler)
			throws ExerciseException {
		E easy = null;
		E normal = null;
		E hard = null;
		Document doc = null;
		PersistenceHandler<E, S> persistenceHandler = getPersistenceHandler();
		try {
			doc = XMLHelper.parseFromBytes(dataPres);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element levelsElement = doc.getDocumentElement();
		NodeList listOfLevels = levelsElement.getChildNodes();
		for (int i = 0; i < listOfLevels.getLength(); i++) {
			Node levelElement = listOfLevels.item(i);
			String name = levelElement.getNodeName();

			String levelData = levelElement.getChildNodes().item(0)
					.getNodeValue();
			byte[] levelBuf = stringToByteArray(levelData);

			switch (name) {
			case "easy":
				easy = persistenceHandler.loadExerData(levelBuf, tempManager,
						matHandler);
				break;
			case "normal":
				normal = persistenceHandler.loadExerData(levelBuf, tempManager,
						matHandler);
				break;
			case "hard":
				hard = persistenceHandler.loadExerData(levelBuf, tempManager,
						matHandler);
				break;
			}
		}

		return new LevelMathDataWrapper<E>(easy, normal, hard);
	}

	@Override
	public byte[] saveSubmission(S subm, TempFilesManager tempManager)
			throws ExerciseException {
		PersistenceHandler<E, S> handler = getPersistenceHandler();
		return handler.saveSubmission(subm, tempManager);
	}

	@Override
	public S loadSubmission(byte[] dataPres, boolean forStatGiver,
			TempFilesManager tempManager) throws ExerciseException {
		PersistenceHandler<E, S> handler = getPersistenceHandler();
		return handler.loadSubmission(dataPres, forStatGiver, tempManager);
	}

	public GsonPersistenceHandler<E, S> getPersistenceHandler() {
		return handler;
	}

	private byte[] stringToByteArray(String value) {
		String[] byteValues = value.substring(1, value.length() - 1).split(",");
		byte[] bytes = new byte[byteValues.length];

		for (int i = 0, len = bytes.length; i < len; i++) {
			bytes[i] = Byte.valueOf(byteValues[i].trim());
		}
		return bytes;
	}

	public static void printDocument(Document doc, OutputStream out)
			throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), new StreamResult(
				new OutputStreamWriter(out, "UTF-8")));
	}
}
