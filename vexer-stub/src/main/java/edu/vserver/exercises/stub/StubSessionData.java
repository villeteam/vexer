package edu.vserver.exercises.stub;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.vaadin.server.VaadinSession;

import edu.vserver.exercises.model.ExerciseTypeDescriptor;

/**
 * A class holding global-resources needed by the stub. The resource-links and
 * other data are stored in {@link VaadinSession}-scope for simplicity.
 * 
 * @author Riku Haavisto
 * 
 */
class StubSessionData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5074154246188402009L;

	private static Logger logger = Logger.getLogger(StubSessionData.class
			.getName());

	/**
	 * @return current {@link StubSessionData}-instance
	 */
	public static StubSessionData getInstance() {

		return VaadinSession.getCurrent().getAttribute(StubSessionData.class);

	}

	/**
	 * Initializes a new {@link StubSessionData} instance, if there is not
	 * already an instance stored to {@link VaadinSession}.
	 * 
	 * @param typesToLoad
	 *            list of {@link ExerciseTypeDescriptor}s that will be present
	 *            for testing in the stub
	 * @param pathToResourceFiles
	 *            path of resource-files directory to use
	 * @param localesToTest
	 *            list of {@link Locale}s that will be available for testing the
	 *            localization of the exercise-type
	 */
	public static void initIfNeeded(
			List<ExerciseTypeDescriptor<?, ?>> typesToLoad,
			String pathToResourceFiles, List<Locale> localesToTest) {

		StubSessionData current = VaadinSession.getCurrent().getAttribute(
				StubSessionData.class);

		StubState.initIfNeeded(localesToTest.get(0), typesToLoad.get(0),
				StubExecutionSettings.values()[0]);

		if (current == null) {
			current = new StubSessionData(pathToResourceFiles, typesToLoad,
					localesToTest);
			VaadinSession.getCurrent().setAttribute(StubSessionData.class,
					current);
		} else if (!current.stubFilesBaseDir.equals(pathToResourceFiles)) {
			logger.warning("Stub-files base directory was changed from " + "'"
					+ current.stubFilesBaseDir + "' to '" + pathToResourceFiles
					+ "'. If you do not need the old files anymore, you "
					+ "can manually remove the old directory!");
			current = new StubSessionData(pathToResourceFiles, typesToLoad,
					localesToTest);
			VaadinSession.getCurrent().setAttribute(StubSessionData.class,
					current);
		}

	}

	/**
	 * Constructs a new {@link StubSessionData}-object and initiates needed
	 * folder structure.
	 * 
	 * @param filesBaseDir
	 *            base directory to which the stub data files are stored
	 * @param typesToLoad
	 *            list of {@link ExerciseTypeDescriptor}s that will be present
	 *            for testing in the stub
	 * @param localesToTest
	 *            list of {@link Locale}s that will be available for testing the
	 *            localization of the exercise-type
	 */
	private StubSessionData(String filesBaseDir,
			List<ExerciseTypeDescriptor<?, ?>> typesToLoad,
			List<Locale> localesToTest) {
		this.stubFilesBaseDir = filesBaseDir;
		if (typesToLoad == null || typesToLoad.isEmpty()) {
			throw new IllegalArgumentException("No types to load");
		}
		if (localesToTest == null || localesToTest.isEmpty()) {
			throw new IllegalArgumentException(
					"No locales specified for testing");
		}
		this.typesToTest = new ArrayList<ExerciseTypeDescriptor<?, ?>>(
				typesToLoad);
		this.localesToTest = new ArrayList<Locale>(localesToTest);
		initState();
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		// ensure that the needed directories still exist after
		// reloading the session
		initState();
	}

	private final String stubFilesBaseDir;
	private final List<ExerciseTypeDescriptor<?, ?>> typesToTest;
	private final List<Locale> localesToTest;
	private transient boolean stateInited = false;

	private void testState() {
		if (!stateInited)
			throw new IllegalStateException("State not initialized");
	}

	/**
	 * @return list of {@link ExerciseTypeDescriptor} that are available for
	 *         testing
	 */
	public List<ExerciseTypeDescriptor<?, ?>> getTypesToTest() {
		testState();
		return typesToTest;
	}

	/**
	 * @return a list of to make available for testing
	 */
	public List<Locale> getLocalesToTest() {
		testState();
		return localesToTest;
	}

	/**
	 * @return directory to use for stub's temporary files
	 */
	public String getStubExerMaterialsTempDir() {
		testState();
		return getStubExerMaterialsTempDirPath();
	}

	/**
	 * Return the base directory to use for storing data-files to exercises of
	 * certain exercise-type.
	 * 
	 * @param type
	 *            {@link ExerciseTypeDescriptor} for which to get the base
	 *            directory
	 * @return path of the correct base directory for certain exercise-type
	 */
	public String getTypeBaseDir(ExerciseTypeDescriptor<?, ?> type) {
		testState();
		return stubFilesBaseDir + File.separatorChar
				+ type.getClass().getSimpleName();
	}

	/**
	 * Clears everything inside the currently used temp-directory.
	 */
	public void clearTemp() {
		testState();
		StubUtil.deleteDirectory(new File(getStubExerMaterialsTempDir()), false);
	}

	// private methods that do not test-state

	private String getTypeBaseFolderPath(ExerciseTypeDescriptor<?, ?> type) {
		return stubFilesBaseDir + File.separatorChar
				+ type.getClass().getSimpleName();
	}

	private String getStubExerMaterialsTempDirPath() {
		return stubFilesBaseDir + File.separatorChar + "temp";
	}

	/**
	 * Initiates the {@link StubSessionData}'s state by doing some tests and
	 * creating needed folders if they do not already exist.
	 */
	private void initState() {
		testForDuplicateDescriptors(typesToTest);
		testAndCreate(stubFilesBaseDir);
		testAndCreate(getStubExerMaterialsTempDirPath());
		for (ExerciseTypeDescriptor<?, ?> type : typesToTest) {
			testAndCreate(getTypeBaseFolderPath(type));
		}
		stateInited = true;
	}

	/**
	 * Tests whether a directory exists in given path, and if it does not
	 * exists, attempts to create a directory to that path.
	 * 
	 * @param path
	 *            path to test or create
	 */
	private static void testAndCreate(String path) {
		File testAndCreateFile = new File(path);
		if (!(testAndCreateFile.exists() && testAndCreateFile.isDirectory())) {
			try {
				FileUtils.forceMkdir(testAndCreateFile);
			} catch (IOException e) {
				throw new AssertionError(
						"Could not create a directory for storing exercise-type-stub-files. "
								+ "Change the directory path or file permissions. "
								+ "Tried to use directory: " + path);
			}
		}
	}

	/**
	 * Tests that there are no duplicate simple-names for the descriptors as
	 * this would cause conflicts when using stub.
	 * 
	 * @param typesToLoad
	 *            list of {@link ExerciseTypeDescriptor} to be loaded for the
	 *            stub
	 */
	private static void testForDuplicateDescriptors(
			List<ExerciseTypeDescriptor<?, ?>> typesToLoad) {
		HashSet<String> allNames = new HashSet<String>();

		for (ExerciseTypeDescriptor<?, ?> exerDesc : typesToLoad) {
			if (!allNames.add(exerDesc.getClass().getSimpleName())) {
				throw new IllegalArgumentException(
						"Duplicate 'simple-name' in"
								+ " typeToLoad exerciseTypeDescriptor-list: "
								+ exerDesc.getClass().getSimpleName()
								+ ". This might not be "
								+ "a real problem, but the stub relies on uniqueness of "
								+ "the simple-names when saving needed files."

				);
			}
		}

	}

}