package edu.vserver.exercises.stub;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseException.ErrorType;
import edu.vserver.exercises.model.PersistenceHandler.ByRefLoader;
import edu.vserver.exercises.model.PersistenceHandler.ByRefSaver;
import edu.vserver.standardutils.AFFile;
import edu.vserver.standardutils.AbstractFile;
import edu.vserver.standardutils.Util;

/**
 * Stub-implementation for scoped material saving and loading (
 * {@link ByRefSaver} and {@link ByRefLoader}).
 * 
 * 
 * 
 * @author Riku Haavisto
 * 
 */
public class StubMatPersistenceHandler implements ByRefSaver, ByRefLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432351237084696903L;

	private final String matScope;
	private final Set<String> regToBeSaved = new HashSet<String>();
	private final boolean inSaveMode;

	/**
	 * Constructs a new {@link StubMatPersistenceHandler} and initializes it to
	 * use certain matScope-folder.
	 * 
	 * @param matScope
	 *            path to folder to be used as material-scope
	 * @param inSaveMode
	 *            whether to allow saving by reference or not
	 */
	StubMatPersistenceHandler(String matScope, boolean inSaveMode) {
		this.matScope = matScope;
		this.inSaveMode = inSaveMode;
	}

	@Override
	public AbstractFile loadByReference(String refId) throws ExerciseException {
		File afFile = new File(matScope + File.separator + refId);
		if (afFile.exists()) {
			return new ExistingAFFile(afFile);
		} else {
			throw new ExerciseException(ErrorType.OTHER,
					"No persistent-material with refId= " + refId + " exists",
					null);
		}
	}

	@Override
	public String saveByReference(AbstractFile toSave) throws ExerciseException {
		if (!inSaveMode) {
			throw new IllegalStateException("Not in save mode!");
		}
		// create the folder only 'lazily'
		File matScopeF = new File(matScope);
		if (!matScopeF.exists()) {
			matScopeF.mkdir();
		}

		if (toSave instanceof ExistingAFFile) {
			ExistingAFFile fileToSave = (ExistingAFFile) toSave;
			String oldRef = fileToSave.getName();
			regToBeSaved.add(oldRef);
			return oldRef;
		} else {
			File newlyCreatedFile = Util.getNonConflictingFile(matScope,
					toSave.getName());
			try {
				FileUtils.writeByteArrayToFile(newlyCreatedFile,
						toSave.getRawData());
			} catch (IOException e) {
				throw new ExerciseException(ErrorType.OTHER,
						"Error saving abstract-file persistently", e);
			}
			String newRef = newlyCreatedFile.getName();
			regToBeSaved.add(newRef);
			return newRef;
		}
	}

	/**
	 * This method attempts to clean-up all the saved material-files that have
	 * become obsolete (were not saved when the exercise-instance was last
	 * saved).
	 */
	void cleanupObsoleteFiles() {
		if (!inSaveMode) {
			throw new IllegalStateException("Not in save mode!");
		}
		File matFolder = new File(matScope);
		if (!matFolder.exists()) {
			// nothing is really saved by given exercise-type
			return;
		}
		for (String fname : matFolder.list()) {
			if (!regToBeSaved.contains(fname)) {
				File toDel = new File(matScope + File.separator + fname);
				toDel.delete();
			}
		}
	}

	// this is used only as a marker class to know which files have already been
	// saved to disk
	private static final class ExistingAFFile extends AFFile {

		public ExistingAFFile(File file) {
			super(file);
		}

	}

}
