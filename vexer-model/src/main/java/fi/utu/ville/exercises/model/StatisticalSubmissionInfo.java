package fi.utu.ville.exercises.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class wraps an {@link SubmissionInfo}-implementor and some generic info
 * on the submission.
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            exercise-type-specific {@link SubmissionInfo}
 * 
 * @see SubmissionStatisticsGiver
 */
public final class StatisticalSubmissionInfo<S extends SubmissionInfo>
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8344582909175535897L;
	private final int timeOnTask;
	private final double evalution;
	private final long doneTime;
	private final S submissionData;

	/**
	 * Constructs a new StatisticalSubmissionInfo-object. Exercise-type
	 * implementors should never need to construct objects of this class.
	 * 
	 * @param timeOnTask
	 *            the time student used to make submission (in seconds)
	 * @param evaluation
	 *            the evaluation the student received from this submission
	 * @param doneTime
	 *            time-stamp of when the submission was submitted
	 * @param submData
	 *            exercise-type-specific {@link SubmissionInfo}-implementor
	 */
	public StatisticalSubmissionInfo(int timeOnTask, double evaluation,
			long doneTime, S submData) {
		this.timeOnTask = timeOnTask;
		this.evalution = evaluation;
		this.doneTime = doneTime;
		this.submissionData = submData;
	}

	/**
	 * The time student used for this submission.
	 * 
	 * @return the time student used to this submission (in seconds)
	 */
	public int getTimeOnTask() {
		return timeOnTask;
	}

	/**
	 * The evaluation in scale 0.0 (worst) - 1.0 (best) the student received
	 * from this submission.
	 * 
	 * @return the evaluation the student received from this submission
	 */
	public double getEvalution() {
		return evalution;
	}

	/**
	 * Time-stamp of when the student submitted this submission. In milliseconds
	 * as defined by {@link System #currentTimeMillis()}. A {@link Date}-object
	 * wrapping the time-stamp can be constructed by {@link Date #Date(long)}.
	 * 
	 * @return time-stamp of when the submission was submitted
	 */
	public long getDoneTime() {
		return doneTime;
	}

	/**
	 * Exercise-type-specific {@link SubmissionInfo}-implementor defining the
	 * exercise-type specific aspects of certain submission.
	 * 
	 * @return exercise-type-specific {@link SubmissionInfo}-implementor
	 */
	public S getSubmissionData() {
		return submissionData;
	}

	@Override
	public String toString() {
		return "StatisticalSubmissionInfo [timeOnTask=" + timeOnTask
				+ ", evalution=" + evalution + ", doneTime=" + doneTime
				+ ", submissionData=" + submissionData + "]";
	}

}