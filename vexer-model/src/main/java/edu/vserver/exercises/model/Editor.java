package edu.vserver.exercises.model;

import com.vaadin.ui.Component;

import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

/**
 * An implementor of this class knows how to edit and create
 * {@link ExerciseData} suitable for parameterizing exercise instances for
 * certain exercise-type.
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <E>
 *            {@link ExerciseData} that can be edited with the implementor
 */
public interface Editor<E extends ExerciseData> {

	/**
	 * @return The GUI through which the user can edit an exercise instance
	 */
	Component getView();

	/**
	 * Iniates the editor. If called with null as oldData, the implementor
	 * creates a new default {@link ExerciseData} as the base for the object.
	 * 
	 * @param localizer
	 *            {@link Localizer} making it possible to localize UI
	 * @param oldData
	 *            {@link ExerciseData} object to be edited or null if new should
	 *            be created
	 * @param editorHelper
	 *            {@link EditorHelper} giving an abstraction to several
	 *            environment-dependent aspects of editing an exercise; also
	 *            contains method for fetching {@link TempFilesManager}
	 */
	void initialize(Localizer localizer, E oldData, EditorHelper<E> editorHelper)
			throws ExerciseException;

}
