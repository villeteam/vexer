package edu.vserver.exercises.model;

import java.io.Serializable;
import java.util.UUID;

import edu.vserver.standardutils.AbstractFile;
import edu.vserver.standardutils.BinaryStringConversionHelper;
import edu.vserver.standardutils.TempFilesManager;
import edu.vserver.standardutils.XMLHelper;

/**
 * <p>
 * An implementor of {@link PersistenceHandler} can write a matching
 * {@link ExerciseData} or {@link SubmissionInfo}-object to a stream in
 * predetermined (preferably) XML-format, and can also read an
 * {@link ExerciseData} or {@link SubmissionInfo}-object from a (correctly
 * formed) stream.
 * </p>
 * <p>
 * Implementations must meet the constraint
 * 
 * <pre>
 * 	"E".equalsSemantically(parsistenceHandler.read(parsistenceHandler.save("E"))
 * </pre>
 * 
 * </p>
 * <p>
 * Even though XML is preferred format the only constraint actually required for
 * correct functionality in ViLLE-exercise-system is that the implementor can
 * read data that it has written. Experience shows however that human-readable
 * and Java-agnostic techniques such as XML are more flexible to refactor and
 * keep backwards compatible, or even export to other programs.
 * </p>
 * <p>
 * In most situations GsonPersistenceHandler (See package edu.vserver.exercises.helpers
 * for more info) can be used as an
 * {@link PersistenceHandler} if {@link ExerciseData}- and
 * {@link SubmissionInfo}-implementors meet requirements for "gson"-specific
 * Java object to JSON-conversion. An converter for {@link AbstractFile}s is
 * provided meaning that the object's can contain {@link AbstractFile}-fields.
 * </p>
 * <p>
 * For embedding binary data (possibly files kept in {@link TempFilesManager}
 * -manager temporary folder) to generated XML or generating files from embedded
 * binary data, you can use {@link BinaryStringConversionHelper}. In
 * {@link ExerciseData}-files this is acceptable only for small files as larger
 * files can be handled much more efficiently and flexibly using
 * {@link ByRefSaver} and {@link ByRefLoader}.
 * </p>
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @see TempFilesManager
 * @see XMLHelper
 * @see BinaryStringConversionHelper
 * @see GsonPersistenceHandler
 * 
 */
public interface PersistenceHandler<E extends ExerciseData, S extends SubmissionInfo>
		extends Serializable {

	/**
	 * Saves the exercise type specific data object using the correct
	 * {@link ExerciseData} class. Returns a stream of the (XML)-data.
	 * 
	 * @param etd
	 *            The {@link ExerciseData}-implementor representation to be
	 *            saved
	 * @param tempManager
	 *            temp-files-manager currently in use
	 * @return byte[] to which the etd is serialized in the form determined by
	 *         this class (preferably XML)
	 * @throws ExerciseException
	 *             if save fails
	 */
	byte[] saveExerData(E etd, TempFilesManager tempManager,
			ByRefSaver matHandler) throws ExerciseException;

	/**
	 * Loads an object of the exercise type specific {@link ExerciseData} -class
	 * from a stream.
	 * 
	 * @param dataPres
	 *            The byte-array from which the data is tried to be parsed
	 * @param tempManager
	 *            temp-files-manager currently in use
	 * @return An object of the exercise type specific {@link ExerciseData}
	 *         -class
	 * @throws ExerciseException
	 *             if load fails
	 */
	E loadExerData(byte[] dataPres, TempFilesManager tempManager,
			ByRefLoader matHandler) throws ExerciseException;

	/**
	 * Saves the exercise type specific data object using the correct
	 * {@link SubmissionInfo} class. Returns a stream of the (XML)-data.
	 * 
	 * @param subm
	 *            The {@link SubmissionInfo}-implementor to be saved
	 * @param tempManager
	 *            temp-files-manager currently in use
	 * @return byte[] to which the subm is serialized in the form determined by
	 *         this class (preferably XML)
	 * @throws ExerciseException
	 *             if save fails
	 */
	byte[] saveSubmission(S subm, TempFilesManager tempManager)
			throws ExerciseException;

	/**
	 * <p>
	 * Loads an object of the exercise type specific {@link SubmissionInfo}
	 * -class from a stream.
	 * </p>
	 * <p>
	 * If the {@link SubmissionInfo}-object is loaded for
	 * {@link SubmissionStatisticsGiver} (forStatGiver is true) it should be
	 * taken into account that multiple {@link SubmissionInfo}-objects are
	 * loaded using same temp-folder. This is a no-issue in most cases as
	 * usually data is either stored in {@link ExerciseData} which is loaded
	 * only once also for {@link SubmissionStatisticsGiver} or they do not
	 * contain any data that could really be analyzed in a generic manner (when
	 * they can just be left un-loaded). Potential method (if needed) to
	 * implement adding several files to same temp-folder would be rename all
	 * files with random strings ( eg. {@link UUID #randomUUID()}.toString())
	 * (and check that there is no conflicts), and to store the new file-names
	 * to returned {@link SubmissionInfo}-objects.
	 * </p>
	 * 
	 * @param dataPres
	 *            The byte-array from which the data is tried to be parsed
	 * @param forStatGiver
	 *            this is true if the {@link SubmissionInfo} is loaded for
	 *            {@link SubmissionStatisticsGiver} meaning that it is possible
	 *            for many {@link SubmissionInfo}-objects to be loaded sharing
	 *            the same temp-folder
	 * @param tempManager
	 *            temp-files-manager currently in use
	 * @return An object of the exercise type specific {@link SubmissionInfo}
	 *         -class
	 * @throws ExerciseException
	 *             if load fails
	 */
	S loadSubmission(byte[] dataPres, boolean forStatGiver,
			TempFilesManager tempManager) throws ExerciseException;

	/**
	 * <p>
	 * Implementors of this class handle saving of resource files (
	 * {@link AbstractFile} in an efficient manner.
	 * </p>
	 * <p>
	 * {@link AbstractFile}s saved through implementor can be fetched later
	 * using the returned id-string by {@link ByRefLoader}.
	 * </p>
	 * <p>
	 * The reference-ids are only meaningful in certain context, like references
	 * saved by {@link ByRefSaver} in
	 * {@link PersistenceHandler #saveExerData(ExerciseData, TempFilesManager, ByRefSaver)
	 * saveExerData()} can be used in fetching files from {@link ByRefLoader} in
	 * {@link PersistenceHandler #loadExerData(byte[], TempFilesManager, ByRefLoader)
	 * loadExerData()} used later for loading the same exercise-instance.
	 * </p>
	 * <p>
	 * No assumptions should be made on how the resource-files are actually
	 * stored as this might be changed for performance (etc.) reasons. All
	 * functionality should rely only on the interface represented by
	 * {@link AbstractFile}.
	 * </p>
	 * 
	 * @see ByRefLoader
	 * @see PersistenceHandler
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public interface ByRefSaver {

		/**
		 * Saves the given material-resource ({@link AbstractFile} persistently
		 * and returns an reference-id by which it can later be loaded by
		 * matching {@link ByRefLoader} (in 'load'-context matching this
		 * 'save'-context).
		 * 
		 * @param toSave
		 *            {@link AbstractFile} to save
		 * @return {@link String} representing an id by which the resource can
		 *         later be reloaded
		 * @throws ExerciseException
		 *             if some error prevents saving the file
		 */
		String saveByReference(AbstractFile toSave) throws ExerciseException;

	}

	/**
	 * <p>
	 * Implementors of this class handle efficient loading of resource-files
	 * saved through {@link ByRefSaver}.
	 * </p>
	 * <p>
	 * Reference-ids saved after saving {@link AbstractFile}s using
	 * {@link ByRefSaver} can be used to load the resource as
	 * {@link AbstractFile}s.
	 * </p>
	 * <p>
	 * For further details on for example context in which ref-ids are
	 * meaningful see documentation of {@link ByRefLoader}.
	 * </p>
	 * 
	 * @see ByRefSaver
	 * @see PersistenceHandler
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public interface ByRefLoader {

		/**
		 * Loads and returns an {@link AbstractFile} matching to the given refId
		 * if such material-resource is saved in the matching context (ie.
		 * returned after saving by a {@link ByRefSaver} in symmetrical
		 * 'save'-method to current 'load'-method.
		 * 
		 * @param refId
		 *            id of the resource to load
		 * @return {@link AbstractFile} matching to the id
		 * @throws ExerciseException
		 *             if resource for the given id does not exists or cannot be
		 *             loaded
		 * 
		 */
		AbstractFile loadByReference(String refId) throws ExerciseException;

	}

}
