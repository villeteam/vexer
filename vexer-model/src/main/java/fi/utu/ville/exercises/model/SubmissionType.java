package fi.utu.ville.exercises.model;

/**
 * Type of certain submission. Used to differentiate between user and system
 * initiated submissions (the latter is used for auto-saving the state in which
 * the executor is, for example to guard against networking errors).
 * 
 * @author Riku Haavisto
 */
public interface SubmissionType {

	/**
	 * If the submission is just a back-ground submission, nothing should be
	 * shown on the user-interface even if no submission can be made because of
	 * inconsistent execution-state at the moment.
	 * 
	 * @return whether the submission type in question is back-ground submission
	 *         or not
	 */
	boolean isBackgroundSubmission();

	/**
	 * <p>
	 * This submission-type is used only for extracting and storing the current
	 * {@link SubmissionInfo} from the executor. The {@link SubmissionInfo} can
	 * be used to later load the state the executor was in.
	 * </p>
	 * <p>
	 * If this is set to true, even a successful submission of this type should
	 * <b>not</b> clear the flag that there are unsubmitted changes in the
	 * current executor, as this is not a real submission but only a "save".
	 * </p>
	 * <p>
	 * As no real submission will be made it is allowed to pass also
	 * {@link SubmissionInfo} objects that cannot be graded or do not contain
	 * all the answers needed to answer certain question, as long as re-loading
	 * the executor from such a {@link SubmissionInfo} is possible.
	 * </p>
	 * 
	 * @return whether this submission will be used only for saving the
	 *         executor's state
	 */
	boolean isOnlyForSavingState();

}