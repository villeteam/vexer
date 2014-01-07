package edu.vserver.exercises.stub;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.standardutils.BinaryStringConversionHelper;
import edu.vserver.standardutils.TempFilesManager;
import edu.vserver.standardutils.XMLHelper;

public class StatisticalSubmInfoSerializer {

	public static final StatisticalSubmInfoSerializer INSTANCE = new StatisticalSubmInfoSerializer();

	// used element and attribute names
	private static final String rootName = "statistical-subm-info";

	private static final String timeOnTaskAttr = "time-on-task";
	private static final String evaluationAttr = "evaluation";
	private static final String doneTimeAttr = "done-time";
	private static final String actSubmElName = "submission-info";

	private StatisticalSubmInfoSerializer() {
	}

	public <S extends SubmissionInfo> StatisticalSubmissionInfo<S> load(
			byte[] dataPres, boolean forStatGiver,
			PersistenceHandler<?, S> serializerHandler,
			TempFilesManager tempManager) {
		StatisticalSubmissionInfo<S> res = null;
		ByteArrayInputStream binput = null;
		try {

			Document doc = XMLHelper.parseFromBytes(dataPres);

			Element rootEl = (Element) doc.getElementsByTagName(rootName).item(
					0);
			// TODO FIXME
			// tempManager.initialize();

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

			submInfo = serializerHandler.loadSubmission(submBytes,
					forStatGiver, tempManager);

			res = new StatisticalSubmissionInfo<S>(timeOnTask, evaluation,
					doneTime, submInfo);

			System.out.println("Loaded: " + res);

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

	public <S extends SubmissionInfo> byte[] save(
			StatisticalSubmissionInfo<S> toWrite,
			PersistenceHandler<?, S> serializerHandler,
			TempFilesManager tempManager) {
		byte[] res = null;
		try {
			System.out.println("About to save: " + toWrite);

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

	public byte[] loadOnlySubmDataForInspecting(byte[] dataPres,
			boolean forStatGiver) {
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
