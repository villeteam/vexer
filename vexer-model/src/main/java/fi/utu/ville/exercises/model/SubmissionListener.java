package fi.utu.ville.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementors of this interface registered through
 * {@link Executor #registerSubmitListener(SubmissionListener)} -method are
 * called with a new {@link SubmissionResult} instance whenever a new submission
 * is made through the {@link Executor} the listener is registered to.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            type of the suitable {@link SubmissionInfo}
 */
public interface SubmissionListener<S extends SubmissionInfo> extends
		Serializable {

	/**
	 * Called with a new {@link SubmissionResult} instance when a submission is
	 * made.
	 * 
	 * @param submission
	 *            {@link SubmissionResult} that the implementor should act upon
	 */
	void submitted(SubmissionResult<S> submission);
	
	/**
	 * Called with a new {@link SubmissionResult} instance when a submission is
	 * made.
	 * 
	 * @param submission
	 *            {@link SubmissionResult} that the implementor should act upon
	 *        submissionWindowIsModal
	 *            true if modality of window, that is shown as a feedback, is to be turned on
	 */
	void submitted(SubmissionResult<S> submission, boolean submissionWindowIsModal);

}