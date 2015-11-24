package fi.utu.ville.exercises.model;

import java.io.Serializable;

import com.vaadin.ui.Component;

/**
 * This class encapsulates needed information about a submission made to a certain exercise.
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <S>
 *            {@link SubmissionInfo}-implementor parameterizing what kind of {@link SubmissionInfo} is appropriate
 */
public final class SubmissionResult<S extends SubmissionInfo> implements
		Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 2070530841554148530L;
	
	private final int timeOnTask;
	private final double correctness;
	private final S additionalData;
	private final Component feedbackComponent;
	private final SubmissionType submType;
	
	/**
	 * Constructs a new {@link SubmissionResult}-object.
	 * 
	 * @param correctness
	 *            correctness value of the submission in the range 0.0 - 1.0
	 * @param timeOnTask
	 *            time on task in seconds (from submission to submission; reseting the exercise state should not reset the timer)
	 * @param additionalData
	 *            exercise-type specific additional info as a suitable {@link SubmissionInfo}-object
	 * @param feedbackComponent
	 *            {@link Component} that can be shown to the user as additional visual feedback on the submission just made
	 * @param submType
	 *            {@link SubmissionType} telling how ViLLE-system should act upon this submission
	 */
	public SubmissionResult(double correctness, int timeOnTask,
			S additionalData, Component feedbackComponent,
			SubmissionType submType) {
		this.correctness = correctness;
		this.timeOnTask = timeOnTask;
		this.additionalData = additionalData;
		this.feedbackComponent = feedbackComponent;
		this.submType = submType;
		if (this.correctness < 0.0 || this.correctness > 1.0) {
			throw new IllegalArgumentException(
					"Correctness must be in the range 0.0-1.0; was: "
							+ correctness);
		}
	}
	
	/**
	 * @return correctness value in the range 0.0 - 1.0
	 */
	public double getCorrectness() {
		return correctness;
	}
	
	/**
	 * @return time on task in seconds (from submission to submission; reseting the exercise state should not reset the timer)
	 */
	public int getTimeOnTask() {
		return timeOnTask;
	}
	
	/**
	 * @return {@link SubmissionInfo}-object containing exercise-type specific data recording info about the submission
	 */
	public S getSubmissionInfo() {
		return additionalData;
	}
	
	/**
	 * @return an optional {@link Component} that can be shown as a visual feedback to the user who made the submission
	 */
	public Component getFeedbackComponent() {
		return feedbackComponent;
	}
	
	/**
	 * @return what kind of submission this submission is (eg. is it just auto-save to store the current state of the exercise or a normal submission)
	 */
	public SubmissionType getSubmissionType() {
		return submType;
	}
	
}
