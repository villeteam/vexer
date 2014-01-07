package edu.vserver.exercises.helpers;

import java.io.Serializable;
import java.util.HashSet;

import edu.vserver.exercises.model.ExerciseSaveListener;
import edu.vserver.exercises.model.SubmissionResult;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.exercises.model.SubmissionListener;
import edu.vserver.exercises.model.SubmissionType;

/**
 * A simple helper class to which standard {@link ExerciseSaveListener}-handling
 * can be delegated to.
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

	public void registerSubmitListener(SubmissionListener<S> submitListener) {
		submitListeners.add(submitListener);
	}

	public void informOnlySubmit(double correctness, int timeOnTask, S data,
			SubmissionType submType) {
		for (SubmissionListener<S> sListener : submitListeners) {
			sListener.submitted(new SubmissionResult<S>(correctness,
					timeOnTask, data, null, submType));
		}
	}

	public void informOnlySubmit(SubmissionResult<S> fb) {
		for (SubmissionListener<S> sListener : submitListeners) {
			sListener.submitted(fb);
		}
	}
}
