package edu.vserver.exercises.model;

import java.io.Serializable;

/**
 * An implementor holds information about the current state of certain exercise.
 * The class can be used to inform interested listeners that for example no
 * submissions should be made at certain point of execution.
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 */
public interface ExecutionState extends Serializable {

	/**
	 * @return true if reset-button should be enabled in the current state
	 */
	boolean isAllowReset();

	/**
	 * @return true if submit-button be enabled in the current state
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
	 * @return true if the user made changes to the exercise's state since last
	 *         submit or the loading of the exercise
	 */
	boolean hasUnsubmittedChanges();

}