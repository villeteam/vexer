package fi.utu.ville.exercises.stub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.standardutils.BinaryStringConversionHelper;
import fi.utu.ville.standardutils.TempFilesManager;
import fi.utu.ville.standardutils.XMLHelper;

/**
 * A class for storing persistently {@link StatisticalSubmissionInfo}-objects to
 * XML and loading them from the persistent representation.
 * 
 * @author Riku Haavisto
 * 
 */
public class StatisticalSubmInfoSerializer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3630050290963327288L;

	private static final Logger logger = Logger
			.getLogger(StatisticalSubmInfoSerializer.class.getName());

	public static final StatisticalSubmInfoSerializer INSTANCE = new StatisticalSubmInfoSerializer();

	// used element and attribute names
	private static final String rootName = "statistical-subm-info";

	private static final String timeOnTaskAttr = "time-on-task";
	private static final String evaluationAttr = "evaluation";
	private static final String doneTimeAttr = "done-time";
	private static final String actSubmElName = "submission-info";

	private StatisticalSubmInfoSerializer() {
	}

	/**
	 * Parses a byte array produced by
	 * {@link #save(StatisticalSubmissionInfo, PersistenceHandler, TempFilesManager)
	 * save()} to a {@link StatisticalSubmissionInfo}-object.
	 * 
	 * @param dataPres
	 *            saved bytes
	 * @param forStatGiver
	 *            whether info will be loaded for stat-giver
	 * @param persistenceHandler
	 *            exercise-type specific {@link PersistenceHandler}
	 * @param tempManager
	 *            {@link TempFilesManager}
	 * @return {@link StatisticalSubmissionInfo} loaded from parameters
	 */
	public <S extends SubmissionInfo> StatisticalSubmissionInfo<S> load(
			byte[] dataPres, boolean forStatGiver,
			PersistenceHandler<?, S> persistenceHandler,
			TempFilesManager tempManager) {
		StatisticalSubmissionInfo<S> res = null;
		ByteArrayInputStream binput = null;
		try {

			Document doc = XMLHelper.parseFromBytes(dataPres);

			Element rootEl = (Element) doc.getElementsByTagName(rootName).item(
					0);

			int timeOnTask = Integer.parseInt(rootEl
					.getAttribute(timeOnTaskAttr));
			double evaluation = Double.parseDouble(rootEl
					.getAttribute(evaluationAttr));
			long doneTime = Long.parseLong(rootEl.getAttribute(doneTimeAttr));

			Element submInfoEl = (Element) rootEl.getElementsByTagName(
					actSubmElName).item(0);

			String submInfoAscii = submInfoEl.getTextContent();

			byte[] submBytes = Base64.decodeBase64(submInfoAscii);

			S submInfo = null;

			submInfo = persistenceHandler.loadSubmission(submBytes,
					forStatGiver, tempManager);

			res = new StatisticalSubmissionInfo<S>(timeOnTask, evaluation,
					doneTime, submInfo);

			logger.info("Loaded: " + res);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExerciseException e) {
			e.printStackTrace();
		} finally {
			if (binput != null) {
				try {
					binput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return res;
	}

	/**
	 * Saves a given {@link StatisticalSubmissionInfo} object to persistence
	 * (XML) byte-form.
	 * 
	 * @param toWrite
	 *            {@link StatisticalSubmissionInfo} to save
	 * @param serializerHandler
	 *            exercise-type specific {@link PersistenceHandler} for saving
	 *            the {@link SubmissionInfo}-object
	 * @param tempManager
	 *            {@link TempFilesManager}
	 * @return bytes of the persistent representation of
	 *         {@link StatisticalSubmissionInfo}-object
	 */
	public <S extends SubmissionInfo> byte[] save(
			StatisticalSubmissionInfo<S> toWrite,
			PersistenceHandler<?, S> serializerHandler,
			TempFilesManager tempManager) {
		byte[] res = null;
		try {
			logger.info("About to save: " + toWrite);

			Document doc = XMLHelper.createEmptyDocument();

			Element topEl = doc.createElement(rootName);

			topEl.setAttribute(doneTimeAttr, toWrite.getDoneTime() + "");
			topEl.setAttribute(evaluationAttr, toWrite.getEvalution() + "");
			topEl.setAttribute(timeOnTaskAttr, toWrite.getTimeOnTask() + "");

			Element submInfoEl = doc.createElement(actSubmElName);

			byte[] submBytes = serializerHandler.saveSubmission(
					toWrite.getSubmissionData(), tempManager);

			submInfoEl.setTextContent(BinaryStringConversionHelper
					.bytesToString(submBytes));

			topEl.appendChild(submInfoEl);

			doc.appendChild(topEl);

			res = XMLHelper.xmlToBytes(doc);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ExerciseException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * <p>
	 * Loads the bytes of {@link SubmissionInfo} contained in a
	 * byte-representation of a {@link StatisticalSubmissionInfo}.
	 * </p>
	 * 
	 * <p>
	 * This method is mainly useful for loading the byte-representation to
	 * inspection so that the developer of an exercise type can easily see does
	 * the persistent representation of a {@link SubmissionInfo} be what it
	 * should be.
	 * </p>
	 * 
	 * @param dataPres
	 *            bytes of persisted {@link SubmissionInfo}
	 * @return the bytes of persisted {@link SubmissionInfo}
	 */
	public byte[] loadOnlySubmDataForInspecting(byte[] dataPres) {
		byte[] res = null;
		try {

			Document doc = XMLHelper.parseFromBytes(dataPres);

			Element rootEl = (Element) doc.getElementsByTagName(rootName).item(
					0);

			Element submInfoEl = (Element) rootEl.getElementsByTagName(
					actSubmElName).item(0);

			String submInfoAscii = submInfoEl.getTextContent();

			res = Base64.decodeBase64(submInfoAscii);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

}
