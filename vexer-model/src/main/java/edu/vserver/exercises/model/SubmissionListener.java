package edu.vserver.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementors of this interface registered through
 * {@link Executor #registerSubmitListener(SubmissionListener)} -method are
 * called with a new {@link SubmissionResult} instance whenever a new
 * submission is made through the {@link Executor} the listener is registered
 * to.
 * </p>
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <S>
 *            type of the suitable {@link SubmissionInfo}
 */
public interface SubmissionListener<S extends SubmissionInfo> extends
		Serializable {

	/**
	 * Called with a new {@link SubmissionResult} instance when a submission
	 * is made.
	 * 
	 * @param submission
	 *            {@link SubmissionResult} that the implementor should act
	 *            upon
	 */
	void submitted(SubmissionResult<S> submission);

}