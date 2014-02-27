package fi.utu.ville.exercises.model;

import java.io.Serializable;

import com.vaadin.ui.Component;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * <p>
 * An implementor of {@link EditorHelper} collects together many
 * environment-dependent functionalities potentially required by an
 * {@link Editor} and provides a clean interface for accessing these
 * functionalities.
 * </p>
 * <p>
 * {@link Editor}-implementors receive an implementor of this class through
 * {@link Editor #initialize(Localizer, ExerciseData, EditorHelper)
 * initialize()}-method.
 * </p>
 * <p>
 * {@link EditorHelper} contains methods helping testing, saving and canceling
 * exercise-editing. There is also a default implementation that can be used for
 * implementing these functionalities (
 * {@link #getControlbar(EditedExerciseGiver) getControlbar()}).
 * {@link EditorHelper} also contains a method for fetching
 * {@link TempFilesManager}. {@link EditorHelper} handles all
 * {@link ExerciseSaveListener}s; listeners can be informed with
 * {@link #informSaveListeners(ExerciseData) informSaveListeners()}-method when
 * the exercise instance should be saved.
 * </p>
 * <p>
 * The general-info-editor-view ({@link #getInfoEditorView()}) must be shown at
 * some point to the user of an {@link Editor}-implementor to allow editing of
 * general attributes of an exercise-instance.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 */
public interface EditorHelper<E extends ExerciseData> extends Serializable {

	/**
	 * @return {@link GeneralExerciseInfo} parsed from the current state of the
	 *         general-info-editor
	 */
	GeneralExerciseInfo getExerciseInfo();

	/**
	 * @return The GUI through which the user can edit general attributes of an
	 *         exercise instance (eg. name and description)
	 */
	Component getInfoEditorView();

	/**
	 * <p>
	 * Exit the editor without informing the {@link ExerciseSaveListener}s, ie.
	 * cancel.
	 * </p>
	 * <p>
	 * If you use default-toolbar ({@link #getControlbar(EditedExerciseGiver)
	 * getControlbar()}) you don't need to call this directly.
	 * </p>
	 */
	void goBack();

	/**
	 * <p>
	 * Registers a new {@link ExerciseSaveListener} to be informed when editing
	 * of an exercise instance is done, and it should be saved.
	 * </p>
	 * <p>
	 * The only situation where this method would be required to be called
	 * directly by an {@link Editor}-instance would be when the editor nests
	 * some other editor. Normal exercise-types can ignore this method as
	 * appropriate listeners will be registered by ViLLE-system.
	 * </p>
	 * 
	 * @param saveListener
	 *            {@link ExerciseSaveListener} to register
	 */
	void registerExerSaveListener(ExerciseSaveListener<E> saveListener);

	/**
	 * <p>
	 * Informs all the registered {@link ExerciseSaveListener}s that the
	 * exercise with {@link ExerciseData} dataToSave should be saved.
	 * </p>
	 * <p>
	 * If you use default-toolbar ({@link #getControlbar(EditedExerciseGiver)
	 * getControlbar()}) you don't need to call this directly unless you want to
	 * provide an alternate way to initiate 'save'-action.
	 * </p>
	 * 
	 * @param dataToSave
	 *            {@link ExerciseData}-instance that should be saved
	 */
	void informSaveListeners(E dataToSave);

	/**
	 * <p>
	 * A means for fetching a testing-view for an {@link Executor}-instance
	 * typically loaded with {@link ExerciseData}-instance currently under edit.
	 * </p>
	 * <p>
	 * If you use default-toolbar ({@link #getControlbar(EditedExerciseGiver)
	 * getControlbar()}) you don't need to call this directly.
	 * </p>
	 * 
	 * @param toTest
	 *            {@link Executor}-loaded with parameters to be tested
	 * @return {@link Component} that includes ViLLE-exercise-view suitable for
	 *         testing
	 */
	Component getTestingExerciseView(Executor<?, ?> toTest);

	/**
	 * A component that includes buttons to preview and save current exercise
	 * instance, and to cancel editing.
	 * 
	 * @param toRegister
	 *            {@link EditedExerciseGiver} that returns {@link ExerciseData}
	 *            -instances of the current state of the editor; used to
	 *            generate test and save -actions
	 * @return {@link Component} containing default buttons for test, save and
	 *         cancel
	 */
	Component getControlbar(EditedExerciseGiver<E> toRegister);

	/**
	 * Enables and disables the default test-button. Should be used if the
	 * exercise is not always in a state that can be tested (and when default
	 * control-bar is used).
	 * 
	 * @param enabled
	 *            true if should be enabled
	 */
	void setTestEnabled(boolean enabled);

	/**
	 * Enables and disables the default save-button. Should be used if the
	 * exercise is not always in a state that can be saved (and when default
	 * control-bar is used).
	 * 
	 * @param enabled
	 *            true if should be enabled
	 */
	void setSaveEnabled(boolean enabled);

	/**
	 * @return current {@link TempFilesManager}
	 */
	TempFilesManager getTempManager();

	/**
	 * Implementor of this exercise knows how to return {@link ExerciseData}
	 * -instance corresponding to the current state of an {@link Editor}. That
	 * {@link ExerciseData}-instance can then be used to test and save the
	 * exercise under editing.
	 * 
	 * @author rahaav
	 * 
	 * @param <F>
	 *            {@link ExerciseData}-type
	 */
	interface EditedExerciseGiver<F extends ExerciseData> {

		/**
		 * <p>
		 * Returns an {@link ExerciseData}-instance corresponding to the current
		 * state of the {@link Editor}.
		 * </p>
		 * <p>
		 * Returning null is allowed, this is handled as a prompt to do nothing.
		 * Showing appropriate error messages to the user is the responsibility
		 * of the implementor. Generally speaking it might be better to disable
		 * test and save, when they are not allowed (and maybe even show some
		 * status message in the UI telling the user why the exercise is not
		 * currently ready for testing or saving).
		 * </p>
		 * 
		 * @param forSaving
		 *            if this is true data is wanted for saving, if false it is
		 *            wanted only for testing (in most cases this does not
		 *            matter)
		 * @return {@link ExerciseData}-instance or null if {@link Editor}
		 *         represents invalid exercise-state (eg. no questions in a
		 *         question-based exercise)
		 */
		F getCurrExerData(boolean forSaving);

	}

}
