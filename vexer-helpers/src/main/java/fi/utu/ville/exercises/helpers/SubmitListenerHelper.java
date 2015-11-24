package fi.utu.ville.exercises.helpers;

import java.io.Serializable;
import java.util.HashSet;

import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionResult;
import fi.utu.ville.exercises.model.SubmissionType;

/**
 * <p>
 * A simple helper class to which standard {@link SubmissionListener}-handling can be delegated to.
 * </p>
 * <p>
 * This class might be usable if all the functionality in {@link ExerciseExecutionHelper} is not needed.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            relevant {@link SubmissionInfo}-implementor
 */
public class SubmitListenerHelper<S extends SubmissionInfo> implements
		Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -4399905295186961688L;
	private final HashSet<SubmissionListener<S>> submitListeners = new HashSet<SubmissionListener<S>>();
	
	public SubmitListenerHelper() {
	}
	
	/**
	 * Registers a {@link SubmissionListener}. {@link Executor #registerSubmitListener(SubmissionListener)} can be delegated to this method.
	 * 
	 * @param submitListener
	 *            {@link SubmissionListener} to be registered
	 */
	public void registerSubmitListener(SubmissionListener<S> submitListener) {
		submitListeners.add(submitListener);
	}
	
	/**
	 * <p>
	 * Informs registered {@link SubmissionListener}s with {@link SubmissionResult} constructed from the parameters.
	 * </p>
	 * <p>
	 * Does not perform any {@link ExecutionState}-changes.
	 * </p>
	 * 
	 * @param correctness
	 *            double presenting correctness of submission in range 0.0 - 1.0
	 * @param timeOnTask
	 *            int presenting time-on-task user used in seconds
	 * @param data
	 *            {@link SubmissionInfo}-object about the submission
	 * @param submType
	 *            used {@link SubmissionType}
	 */
	public void informOnlySubmit(double correctness, int timeOnTask, S data,
			SubmissionType submType) {
		for (SubmissionListener<S> sListener : submitListeners) {
			sListener.submitted(new SubmissionResult<S>(correctness,
					timeOnTask, data, null, submType));
		}
	}
	
	/**
	 * <p>
	 * Informs registered {@link SubmissionListener}s with given {@link SubmissionResult}.
	 * </p>
	 * <p>
	 * Does not perform any {@link ExecutionState}-changes.
	 * </p>
	 * 
	 * @param fb
	 *            {@link SubmissionResult} to use
	 */
	public void informOnlySubmit(SubmissionResult<S> fb) {
		for (SubmissionListener<S> sListener : submitListeners) {
			sListener.submitted(fb);
		}
	}
}
