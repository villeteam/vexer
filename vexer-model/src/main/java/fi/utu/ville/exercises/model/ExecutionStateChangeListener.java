package fi.utu.ville.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementors of this interface registered through
 * {@link Executor #registerExecutionStateChangeListener(ExecutionStateChangeListener)}
 * -method are called with a new {@link ExecutionState} instance whenever the
 * state of the executor changes.
 * </p>
 * <p>
 * Implementing {@link ExecutionStateChangeListener}-handling can mostly be
 * delegated to edu.vserver.exercises.helpers.ExerciseExecutionHelper .
 * </p>
 * 
 * @author Riku Haavisto
 */
public interface ExecutionStateChangeListener extends Serializable {

	/**
	 * Called with a new {@link ExecutionState} instance whenever the state of
	 * the executor that the {@link ExecutionStateChangeListener} is registered
	 * to changes.
	 * 
	 * @param newState
	 *            {@link ExecutionState} that is the current state of the
	 *            {@link Executor} the {@link ExecutionStateChangeListener} is
	 *            registered to
	 */
	void actOnStateChange(ExecutionState newState);

}