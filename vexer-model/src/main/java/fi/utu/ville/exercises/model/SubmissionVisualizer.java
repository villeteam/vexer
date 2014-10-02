package fi.utu.ville.exercises.model;

import java.io.Serializable;

import com.vaadin.ui.Component;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * An implementor of this class knows how to visualize certain submission stored
 * as {@link SubmissionInfo} made to a given exercise ({@link ExerciseData} ).
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <E>
 *            {@link ExerciseData} of the exercise type submissions to which can
 *            be visualized by the implementor
 * @param <S>
 *            {@link SubmissionInfo} -data object that can be visualized by the
 *            implementor
 */
public interface SubmissionVisualizer<E extends ExerciseData, S extends SubmissionInfo>
		extends Serializable {

	/**
	 * Loads the visualizer with data. This method must be called before calling
	 * other methods of the visualizer. The method initializes the visualizer.
	 * 
	 * @param exercise
	 *            {@link ExerciseData} object to be loaded for the visualizer;
	 *            this is the exercise the submission was made to
	 * @param dataObject
	 *            {@link SubmissionInfo} of the submission made to given
	 *            exercise that is going to be visualized
	 * @param localizer
	 *            {@link Localizer} making it possible to localize UI
	 * @param tempManager
	 *            {@link TempFilesManager} giving a temporary folder for that
	 *            can be used if needed
	 */
	void initialize(E exercise, S dataObject, Localizer localizer,
			TempFilesManager tempManager) throws ExerciseException;

	/**
	 * @return The GUI that visualizes the submission
	 */
	Component getView();

	/**
	 * @return The submission presented as a human-readable string (might be for
	 *         example the programming code a student used in a programming
	 *         exercise or user's answers to questions in certain exercise)
	 */
	String exportSubmissionDataAsText();

}
