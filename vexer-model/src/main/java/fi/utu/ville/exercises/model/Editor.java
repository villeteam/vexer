package fi.utu.ville.exercises.model;

import java.io.Serializable;

import com.vaadin.ui.Component;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

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
public interface Editor<E extends ExerciseData> extends Serializable {

	/**
	 * <p>
	 * Returns a gui for editing an exercise instance of given type.
	 * </p>
	 * <p>
	 * In addition to controls for editing the exercise instance, this view must
	 * contain editor for editing general exercise info (the editor is provided
	 * by {@link EditorHelper #getInfoEditorView()}). The view must also contain
	 * controls for save, cancel and preferably to preview currently edited
	 * exercise (default implementation and helpers for custom implementations
	 * can be fetched through {@link EditorHelper}).
	 * </p>
	 * 
	 * @return The GUI through which the user can edit an exercise instance
	 */
	Component getView();

	/**
	 * Iniates the editor. If called with null as oldData, the implementor
	 * creates a new default {@link ExerciseData} as the base for the object.
	 * @param ui
	 *			  {@link VilleUI} making it possible to push other views the editor
	 *			  creates to the stack.
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
	void initialize(VilleUI ui, Localizer localizer, E oldData, EditorHelper<E> editorHelper)
			throws ExerciseException;

}
