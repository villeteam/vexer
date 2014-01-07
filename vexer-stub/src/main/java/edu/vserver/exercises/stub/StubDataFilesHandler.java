package edu.vserver.exercises.stub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.GeneralExerciseInfo;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.standardutils.TempFilesManager;

/**
 * A class handling file operations to store data needed by the stub-versions
 * persistence layer. Stores and loads {@link ExerciseData}- and
 * {@link SubmissionInfo} -instances. All files are stored inside the folder
 * returned by {@link VilleExerStubUI #getStubResourceBaseDir()}.
 * 
 * @author Riku Haavisto
 * 
 */
final class StubDataFilesHandler {

	private static final Logger logger = Logger
			.getLogger(StubDataFilesHandler.class.getName());

	private static final String submDataFileExt = ".subm";
	private static final String submDirName = "submissions";
	private static final String xmlFileName = "exer-instance.xml";
	private static final String descFileName = "description.txt";
	private static final String matFolderName = "materials";

	private StubDataFilesHandler() {

	}

	private static String getExerInstanceBaseDir(
			ExerciseTypeDescriptor<?, ?> type, String exerName) {
		return StubSessionData.getInstance().getTypeBaseDir(type)
				+ File.separatorChar + exerName;
	}

	private static String getSubmBaseDir(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		return StubSessionData.getInstance().getTypeBaseDir(type)
				+ File.separatorChar + exerName + File.separatorChar
				+ submDirName;
	}

	private static String parseCorrXmlPath(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		return StubSessionData.getInstance().getTypeBaseDir(type)
				+ File.separator + exerName + File.separator + xmlFileName;
	}

	private static String parseCorrMatScope(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		return StubSessionData.getInstance().getTypeBaseDir(type)
				+ File.separator + exerName + File.separator + matFolderName;
	}

	private static String getDescPath(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		return getExerInstanceBaseDir(type, exerName) + File.separator
				+ descFileName;
	}

	private static void createIfNeededExerInstanceFolderStructure(
			ExerciseTypeDescriptor<?, ?> type, String exerName) {

		if ("".equals(exerName)) {
			throw new IllegalArgumentException(
					"ExerName cannot be an empty string!");
		}

		String instBase = getExerInstanceBaseDir(type, exerName);
		File instBaseF = new File(instBase);
		if (!instBaseF.exists()) {
			if (!instBaseF.mkdir()) {
				// TODO some error msg
			}
		}
		String instSubmBase = getSubmBaseDir(type, exerName);
		File instSubmF = new File(instSubmBase);

		if (!instSubmF.exists()) {
			if (!instSubmF.mkdir()) {
				// TODO some error msg
			}
		}

	}

	public static List<String> getPresentExerInstances(
			ExerciseTypeDescriptor<?, ?> type) {
		ArrayList<String> res = new ArrayList<String>();

		File typeBase = new File(StubSessionData.getInstance().getTypeBaseDir(
				type));

		// TODO: some checks?

		for (File file : typeBase.listFiles()) {
			if (file.isDirectory()) {
				res.add(file.getName());
			}
		}

		return res;
	}

	public static void deleteExerInstance(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		String instanceBasePath = getExerInstanceBaseDir(type, exerName);
		File instBaseFolder = new File(instanceBasePath);
		if (instBaseFolder.exists()) {
			StubUtil.deleteDirectory(instBaseFolder, true);
		}
	}

	public static byte[] loadLatestBareSubmInfo(
			ExerciseTypeDescriptor<?, ?> type, String exerName) {

		byte[] res = null;

		File fromFolder = new File(getSubmBaseDir(type, exerName));

		int largestUsedOrderNum = getLargestUsedOrderNum(fromFolder);

		if (largestUsedOrderNum == 0) {
			return null;
		}

		String fileToLoadPath = fromFolder.getAbsolutePath() + File.separator
				+ largestUsedOrderNum + submDataFileExt;

		byte[] submDataPres = null;
		try {
			File submDataFile = new File(fileToLoadPath);
			if (submDataFile.exists()) {
				submDataPres = FileUtils.readFileToByteArray(submDataFile);
				// TODO FIXME
				res = StatisticalSubmInfoSerializer.INSTANCE
						.loadOnlySubmDataForInspecting(submDataPres, false);

			}

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE,
					"Could not find the submission-data file for some reason",
					e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public static <S extends SubmissionInfo> S loadLatestSubmInfo(
			Class<S> submDataClass, ExerciseTypeDescriptor<?, S> type,
			String exerName, TempFilesManager tempManager) {

		S res = null;

		StatisticalSubmissionInfo<S> genStatInfo = null;

		File fromFolder = new File(getSubmBaseDir(type, exerName));

		int largestUsedOrderNum = getLargestUsedOrderNum(fromFolder);

		if (largestUsedOrderNum == 0) {
			return null;
		}

		PersistenceHandler<?, S> dataLoader = type.newExerciseXML();

		String fileToLoadPath = fromFolder.getAbsolutePath() + File.separator
				+ largestUsedOrderNum + submDataFileExt;

		byte[] submDataPres = null;
		try {
			File submDataFile = new File(fileToLoadPath);
			if (submDataFile.exists()) {
				submDataPres = FileUtils.readFileToByteArray(submDataFile);
				// TODO FIXME
				genStatInfo = StatisticalSubmInfoSerializer.INSTANCE.load(
						submDataPres, false, dataLoader, tempManager);
				res = genStatInfo.getSubmissionData();

			}

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE,
					"Could not find the submission-data file for some reason",
					e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public static <S extends SubmissionInfo> void writeSubmToDisk(
			ExerciseTypeDescriptor<?, S> type, String exerName,
			StatisticalSubmissionInfo<S> submInfo, TempFilesManager tempManager) {
		File exerSubmFolder = new File(getSubmBaseDir(type, exerName));
		String submDataSavePath = exerSubmFolder.getAbsolutePath()
				+ File.separator + getNextFreeOrderNum(exerSubmFolder)
				+ submDataFileExt;
		File outFile = new File(submDataSavePath);
		try {

			FileUtils.writeByteArrayToFile(
					outFile,
					StatisticalSubmInfoSerializer.INSTANCE.save(submInfo,
							type.newExerciseXML(), tempManager));

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Missing the submission-data folder", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Problems writing to stream", e);
		}
	}

	private static int getLargestUsedOrderNum(File fromFolder) {
		int largestUsed = 0;
		if (!(fromFolder.exists() && fromFolder.isDirectory())) {
			throw new IllegalStateException("Submission-folder ( " + fromFolder
					+ " ) does not exist.");
		}
		try {
			for (String aSubm : fromFolder.list()) {

				int orderNum = Integer.parseInt(aSubm.split("\\.")[0]);

				if (orderNum > largestUsed) {
					largestUsed = orderNum;
				}
			}
		} catch (NumberFormatException e) {
			throw new IllegalStateException(
					"Inconsistency (wrong naming pattern) in the submission-folder: "
							+ fromFolder, e);
		}

		return largestUsed;
	}

	private static int getNextFreeOrderNum(File fromFolder) {
		return getLargestUsedOrderNum(fromFolder) + 1;
	}

	public static <S extends SubmissionInfo> List<StatisticalSubmissionInfo<S>> loadAllSubmissions(
			ExerciseTypeDescriptor<?, S> type, String exerName,
			Class<S> submDataType, TempFilesManager tempManager) {

		ArrayList<StatisticalSubmissionInfo<S>> res = new ArrayList<StatisticalSubmissionInfo<S>>();

		PersistenceHandler<?, S> loadHandler = type.newExerciseXML();

		File fromFolder = new File(getSubmBaseDir(type, exerName));

		if (!(fromFolder.exists() && fromFolder.isDirectory())) {
			throw new IllegalStateException("Submission-folder ( " + fromFolder
					+ " ) does not exist.");
		}
		try {

			logger.info("Loading submission-data-objects from folder "
					+ fromFolder);

			for (String aSubm : fromFolder.list()) {

				byte[] submDataPres = null;
				try {
					File submDataFile = new File(fromFolder.getAbsolutePath()
							+ File.separator + aSubm);
					if (submDataFile.exists()) {
						submDataPres = FileUtils
								.readFileToByteArray(submDataFile);

						StatisticalSubmissionInfo<?> genSubmInfo = StatisticalSubmInfoSerializer.INSTANCE
								.load(submDataPres, true, loadHandler,
										tempManager);
						res.add(castSubminfo(submDataType, genSubmInfo));

					}

				} catch (FileNotFoundException e) {
					logger.log(
							Level.SEVERE,
							"Could not find the submission-data file for some reason",
							e);
				}

			}
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Could not find the submission-data-file",
					e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error reading submission-data stream", e);
		}

		return res;

	}

	private static <S extends SubmissionInfo> StatisticalSubmissionInfo<S> castSubminfo(
			Class<S> submDataClass, StatisticalSubmissionInfo<?> genInfo) {
		return new StatisticalSubmissionInfo<S>(genInfo.getTimeOnTask(),
				genInfo.getEvalution(), genInfo.getDoneTime(),
				submDataClass.cast(genInfo.getSubmissionData()));
	}

	public static <E extends ExerciseData> void saveExerStream(
			ExerciseTypeDescriptor<E, ?> type, GeneralExerciseInfo exerInfo,
			E dataToSave, TempFilesManager tempManager)
			throws ExerciseException {

		String exerName = exerInfo.getName();

		// it is possible that we are making new exercise
		createIfNeededExerInstanceFolderStructure(type, exerName);

		String filePath = parseCorrXmlPath(type, exerName);
		String matScope = parseCorrMatScope(type, exerName);
		StubMatPersistenceHandler matSaver = new StubMatPersistenceHandler(
				matScope, true);
		try {

			byte[] toSave = type.newExerciseXML().saveExerData(dataToSave,
					tempManager, matSaver);

			FileUtils.writeByteArrayToFile(new File(filePath), toSave);

			FileUtils.writeStringToFile(new File(getDescPath(type, exerName)),
					exerInfo.getDescription(), "UTF-8");

		} catch (FileNotFoundException e) {
			logger.log(
					Level.SEVERE,
					"Could not find the file to save the submission-data stream to",
					e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error wrinting submission-data stream", e);
		} finally {
			matSaver.cleanupObsoleteFiles();
		}
	}

	public static String getExerDescription(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		String res = "";
		String pathToData = getDescPath(type, exerName);
		Scanner scanner = null;
		try {

			scanner = new Scanner(new File(pathToData), "UTF-8");
			while (scanner.hasNextLine()) {
				res += scanner.nextLine();
			}

		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "Could not load exercise-description", e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return res;
	}

	public static <E extends ExerciseData, S extends SubmissionInfo> E loadExerDataForEditor(
			Class<E> exerTypeDataClass, PersistenceHandler<E, S> xmlHandler,
			ExerciseTypeDescriptor<?, ?> type, String exerName,
			TempFilesManager tempManager) {
		E res = null;
		// new exercise
		if ("".equals(exerName)) {
			return res;
		}

		String pathToData = parseCorrXmlPath(type, exerName);
		// it is intended behavior to return null for editor if no xml-
		// file exists at the moment (whereas to the executor that would be
		// an unrecoverable error)
		if ((new File(pathToData)).exists()) {

			try {
				res = loadExerTypeData(exerTypeDataClass, xmlHandler, type,
						exerName, tempManager);
			} catch (ExerciseException e) {
				// Should this be reported to user ; at least in a notification
				// that creating new because the old exercise could not be
				// loaded..?
				logger.log(
						Level.WARNING,
						"Could not load old exercise; possibly did not exist..?; "
								+ "returning null for editor to create a totally new exercise",
						e);
			}
		}
		return res;
	}

	public static <E extends ExerciseData, S extends SubmissionInfo> E loadExerTypeData(
			Class<E> exerTypeDataClass, PersistenceHandler<E, S> xmlHandler,
			ExerciseTypeDescriptor<?, ?> type, String exerName,
			TempFilesManager tempManager) throws ExerciseException {
		E res = null;
		String pathToData = parseCorrXmlPath(type, exerName);
		byte[] dataPres = null;
		StubMatPersistenceHandler matLoader = new StubMatPersistenceHandler(
				parseCorrMatScope(type, exerName), false);
		try {
			dataPres = FileUtils.readFileToByteArray(new File(pathToData));

			res = xmlHandler.loadExerData(dataPres, tempManager, matLoader);
		} catch (FileNotFoundException e) {
			throw new ExerciseException(
					ExerciseException.ErrorType.EXERCISE_XML_NOT_FOUND,
					"Stub xml not found from path: " + pathToData, e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public static byte[] loadExerTypeData(ExerciseTypeDescriptor<?, ?> type,
			String exerName) {
		String pathToData = parseCorrXmlPath(type, exerName);
		byte[] dataPres = null;
		try {
			dataPres = FileUtils.readFileToByteArray(new File(pathToData));

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Stub xml not found from path: "
					+ pathToData, e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataPres;

	}

}
