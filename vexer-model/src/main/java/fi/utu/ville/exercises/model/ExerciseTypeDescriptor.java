package fi.utu.ville.exercises.model;

import java.io.Serializable;

import com.vaadin.server.Resource;

import fi.utu.ville.standardutils.Localizer;

/**
 * Class that binds together information of certain exercise-type that is needed
 * for using the type.
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <E>
 *            {@link ExerciseData} the data object defining a certain exercise
 *            of this type
 * @param <S>
 *            {@link SubmissionInfo} the object that stores all the needed
 *            information for analyzing a certain submission of this
 *            exercise-type
 */
public interface ExerciseTypeDescriptor<E extends ExerciseData, S extends SubmissionInfo>
		extends Serializable {

	/**
	 * Returns an instance of the {@link PersistenceHandler} class that is
	 * responsible on loading from and storing to persistent form E (
	 * {@link ExerciseData}) and S ({@link SubmissionInfo}) -instances.
	 * 
	 * @return an instance of suitable {@link PersistenceHandler}
	 */
	PersistenceHandler<E, S> newExerciseXML();

	/**
	 * Returns an instance of a {@link Executor} class that is suitable for
	 * executing instances of this exercise type (ie. drawing UI with controls
	 * to the student and parsing correct submission information when the
	 * student submits the exercise).
	 * 
	 * @return new instance of a suitable {@link Executor}
	 */
	Executor<E, S> newExerciseExecutor();

	/**
	 * Returns an instance of an {@link Editor} that can be used to create and
	 * edit instances of E ({@link ExerciseData})
	 * 
	 * @return new instance of a suitable {@link Editor}
	 */
	Editor<E> newExerciseEditor();

	/**
	 * The {@link Class} -token of the data object {@link ExerciseData} defining
	 * a certain exercise of this type
	 * 
	 * @return The {@link Class} -token of suitable {@link ExerciseData}
	 */
	Class<E> getTypeDataClass();

	/**
	 * The {@link Class} -token of the data object {@link SubmissionInfo} that
	 * stores all the needed information for analyzing a certain submission of
	 * this exercise-type
	 * 
	 * @return The {@link Class} -token of suitable {@link SubmissionInfo}
	 */
	Class<S> getSubDataClass();

	/**
	 * Returns an instance of a {@link SubmissionStatisticsGiver} that can be
	 * used to show various statistics of a set of {@link SubmissionInfo}
	 * objects suitable to this class and to a certain {@link ExerciseData}
	 * -instance as well as parsing exportable data from that set.
	 * 
	 * @return new instance of a suitable {@link SubmissionStatisticsGiver}
	 */
	SubmissionStatisticsGiver<E, S> newStatisticsGiver();

	/**
	 * Returns an instance of a {@link SubmissionVisualizer} that can visualize
	 * a submission recorded as a suitable {@link SubmissionInfo} object
	 * 
	 * @return new instance of a suitable {@link SubmissionVisualizer}
	 */
	SubmissionVisualizer<E, S> newSubmissionVisualizer();

	/**
	 * Returns a localized name of the exercise-type described by this
	 * {@link ExerciseTypeDescriptor}.
	 * 
	 * @param localizer
	 *            {@link Localizer} to localize the name
	 * @return localized name of the exercise
	 */
	String getTypeName(Localizer localizer);

	/**
	 * <p>
	 * Returns a localized description of the exercise-type described by this
	 * {@link ExerciseTypeDescriptor}.
	 * </p>
	 * <p>
	 * The returned string should be a short description (at most a few
	 * sentences) describing what students do in this exercise-type. Eg.
	 * "In derivation exercise students calculate derivatives of generated functions"
	 * .
	 * </p>
	 * 
	 * @param localizer
	 *            {@link Localizer} to localize the description
	 * @return localized description of the exercise
	 */
	String getTypeDescription(Localizer localizer);

	/**
	 * 16 * 16 px
	 * 
	 * @return small icon representing this exercise-type
	 */
	Resource getSmallTypeIcon();

	/**
	 * 32 * 32 px
	 * 
	 * @return medium icon representing this exercise-type
	 */
	Resource getMediumTypeIcon();

	/**
	 * 96 * 96 px
	 * 
	 * @return large icon representing this exercise-type
	 */
	Resource getLargeTypeIcon();

	/**
	 * <p>
	 * Returns true if submissions made to this exercise-type require manual
	 * grading ie. if they cannot be wholly automatically assessed.
	 * </p>
	 * <p>
	 * Convention is to return 1.0 as correctness of submissions to
	 * exercise-types that are wholly manually graded. The manual-grading given
	 * by teacher will override or be added to the score given by returned
	 * correctness value.
	 * </p>
	 * 
	 * @return true if submissions to this exercise-type require manual grading
	 */
	boolean isManuallyGraded();
	
	/**
	 * For example: return FontAwesome.COGS.getHtml().
	 * FontAwesome 4.1.0 is supported.
	 * 
	 * @return FontIcon as HTML String
	 */
	String getHTMLIcon();

}
