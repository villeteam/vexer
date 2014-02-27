package fi.utu.ville.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementor of this interface tells what kind of feedback can be shown to the
 * user during certain execution of an exercise. Also tells the executor whether
 * it should be in "exam"-mode, which is a mode that most exercises do not have
 * to react to if they respect the other directives given by an implementor of
 * this class.
 * </p>
 * <p>
 * If user feedback is shown only in the Feedback-component returned in the
 * {@link SubmissionResult} these settings can mostly be ignored as that
 * Feedback-component will be hidden by the system if it should not be shown.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 */
public interface ExecutionSettings extends Serializable {

	/**
	 * In certain situations (eg. possibly in exams) no feed-back on the
	 * correctness of the users answers should be shown to the user during the
	 * execution of an exercise. Then this method will return true.
	 * 
	 * @return whether any feed-back should not be shown to the user through the
	 *         executor
	 */
	boolean isNoTips();

	/**
	 * When this is true, the correct answers should not be shown to the user
	 * during execution. General-tips are however allowed, if the isNoTips()
	 * returns false.
	 * 
	 * @return whether correct answers should not be shown as such in the
	 *         user-interface
	 */
	boolean isNoCorrAnswers();

	/**
	 * Some exercises might have settings that should be changed if the executor
	 * is run under exam-settings that do not strictly fall into any exact and
	 * general categories. Most exercise-types can however ignore this value.
	 * 
	 * @return whether the executor is run under exam-mode
	 */
	boolean isExam();

}