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

import com.vaadin.server.VaadinSession;

import edu.vserver.exercises.model.ExerciseTypeDescriptor;

/**
 * A class holding global-resources needed by the stub. The resource-links and
 * other data are stored in {@link VaadinSession}-scope for simplicity even
 * though are global not per session. This should not however present any bugs
 * as long as the links are not altered on-the-fly. The stub as a whole however
 * contains certain (file-naming) race-conditions that could present bugs in
 * multi-user environment.
 * 
 * @author Riku Haavisto
 */
class StubSessionData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5074154246188402009L;

	private static Logger logger = Logger.getLogger(StubSessionData.class
			.getName());

	public static StubSessionData getInstance() {

		// as long as called inside the "vaadin"-code this should be thread-safe

		return VaadinSession.getCurrent().getAttribute(StubSessionData.class);

	}

	public static void initIfNeeded(
			List<ExerciseTypeDescriptor<?, ?>> typesToLoad,
			String pathToResourceFiles, List<Locale> localesToTest) {

		// as long as called inside the "vaadin"-code this should be thread-safe

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

	public List<ExerciseTypeDescriptor<?, ?>> getTypesToTest() {
		testState();
		return typesToTest;
	}

	public List<Locale> getLocalesToTest() {
		testState();
		return localesToTest;
	}

	// public String getStubLanguageFilesDir() {
	// testState();
	//
	// VaadinServlet.getCurrent().
	//
	// String langDir = VaadinServlet.getCurrent().getServletContext()
	// .getRealPath("/VILLE/resources/language/extensions");
	// return langDir;
	//
	// }

	public String getStubExerMaterialsTempDir() {
		testState();
		return getStubExerMaterialsTempDirPath();
	}

	public String getTypeBaseDir(ExerciseTypeDescriptor<?, ?> type) {
		testState();
		return stubFilesBaseDir + File.separatorChar
				+ type.getClass().getSimpleName();
	}

	public void clearTemp() {
		testState();
		StubUtil.deleteDirectory(new File(getStubExerMaterialsTempDir()), false);
	}

	// private that do not test-state

	private String getTypeBaseFolderPath(ExerciseTypeDescriptor<?, ?> type) {
		return stubFilesBaseDir + File.separatorChar
				+ type.getClass().getSimpleName();
	}

	private String getStubExerMaterialsTempDirPath() {
		return stubFilesBaseDir + File.separatorChar + "temp";
	}

	private void initState() {
		testForDuplicateDescriptors(typesToTest);
		testAndCreate(stubFilesBaseDir);
		testAndCreate(getStubExerMaterialsTempDirPath());
		for (ExerciseTypeDescriptor<?, ?> type : typesToTest) {
			testAndCreate(getTypeBaseFolderPath(type));
		}
		stateInited = true;
	}

	private static void testAndCreate(String path) {
		File testAndCreateFile = new File(path);
		if (!(testAndCreateFile.exists() && testAndCreateFile.isDirectory())) {
			if (!testAndCreateFile.mkdir()) {
				throw new AssertionError(
						"Could not create a directory for storing exercise-type-stub-files. "
								+ "Change the directory path or file permissions. "
								+ "Tried to use directory: " + path);
			}
		}
	}

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