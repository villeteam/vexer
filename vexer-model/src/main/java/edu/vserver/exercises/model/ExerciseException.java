package edu.vserver.exercises.model;

/**
 * Only checked {@link Exception} that can be thrown from interfaces of
 * {@link edu.vserver.exercises.model}. Other checked {@link Exception}s must be
 * wrapped in {@link ExerciseException}s or handled inside implementors.
 * 
 * @author Riku Haavisto
 */
public class ExerciseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7475602243410672022L;

	/**
	 * Categories to which most {@link ExerciseException}s fall into.
	 * 
	 * @author Riku Haavisto
	 */
	public enum ErrorType {

		/**
		 * Specified exercise-XML-file was not found. This error is external
		 * although related to actual exercise-types.
		 */
		EXERCISE_XML_NOT_FOUND,

		/**
		 * This type should be used if the exercise cannot be loaded because of
		 * fail to grab some critical resources or an inconsistency in the
		 * {@link ExerciseData} received (eg. deprecated version).
		 */
		EXERCISE_LOAD_ERROR,

		/**
		 * This type should be used if there is an inconsistency in the
		 * {@link SubmissionInfo} received (eg. deprecated version).
		 */
		SUBM_DATA_CONSISTENCY_ERROR,

		/**
		 * This type should be used in the unlikely event that a
		 * {@link Executor} or {@link SubmissionVisualizer} is tried to be
		 * loaded with {@link SubmissionInfo} data that does not match the
		 * {@link ExerciseData} in use. This happens if the exercise is changed
		 * after the submission is done in such a way that makes it essentially
		 * a new exercise-instance.
		 */
		SUBM_DATA_DO_NOT_MATCH_EXERCISE,

		/**
		 * Error when trying to load an {@link ExerciseData}-object from
		 * InputStream.
		 */
		EXER_LOAD_ERROR,

		/**
		 * Error when trying to save an {@link ExerciseData}-object to
		 * OutputStream.
		 */
		EXER_WRITE_ERROR,

		/**
		 * Error when trying to load an {@link ExerciseData}-object from
		 * InputStream.
		 */
		SUBM_LOAD_ERROR,

		/**
		 * Error when trying to save an {@link ExerciseData}-object to
		 * OutputStream.
		 */
		SUBM_WRITE_ERROR,

		/**
		 * Can be used for other kind of errors.
		 */
		OTHER;
	}

	private final ErrorType type;

	/**
	 * Shorthand for
	 * {@link ExerciseException #ExerciseException(ErrorType, String, Throwable)
	 * ExerciseException(errorType, message, cause)} with (extra-)message="" .
	 */
	public ExerciseException(ErrorType errorType, Throwable cause) {
		this(errorType, "", cause);
	}

	/**
	 * Constructs a new {@link ExerciseException}.
	 * 
	 * @param errorType
	 *            {@link ErrorType} specifying the category of this error
	 * @param message
	 *            additional explanation of the error
	 * @param cause
	 *            underlying {@link Throwable} causing the error or null if
	 *            there is no underlying cause
	 */
	public ExerciseException(ErrorType errorType, String message,
			Throwable cause) {

		super(errorType.name() + ": " + message, cause);
		this.type = errorType;

	}

	/**
	 * @return {@link ErrorType} of this {@link ExerciseException}
	 */
	public ErrorType getType() {
		return type;
	}

}
