package fi.utu.ville.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * An implementor holds information about the current state of certain exercise.
 * The class can be used to inform interested listeners that for example no
 * submissions should be made at certain point of execution.
 * </p>
 * <p>
 * Implementing {@link ExecutionState} can mostly be delegated to
 * edu.vserver.exercises.helpers.ExerciseExecutionHelper .
 * </p>
 * 
 * 
 * @author Riku Haavisto
 * 
 */
public interface ExecutionState extends Serializable {

	/**
	 * @return true if reset-button should be enabled in the current state
	 */
	boolean isAllowReset();

	/**
	 * @return true if submit-button should be enabled in the current state
	 */
	boolean isAllowSubmit();

	/**
	 * @return true if reset-button should be shown in the current state
	 */
	boolean isResetShown();

	/**
	 * @return true if submit-button should be shown in the current state
	 */
	boolean isSubmitShown();

	/**
	 * @return true if the exercise menu should be enabled in the current state
	 */
	boolean isMenuEnabled();

	/**
	 * The return value of this method is used in showing a warning to a student
	 * who is trying to leave an exercise without submitting changes made to it.
	 * Usually it is enough to err on the side of caution, ie. to return true
	 * from this method after the user has used for example some text-field
	 * regardless whether the value of the field was actually changed or not.
	 * 
	 * @return true if the user made changes to the exercise's state since last
	 *         submit or the loading of the exercise
	 */
	boolean hasUnsubmittedChanges();

}