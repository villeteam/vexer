package edu.vserver.exercises.model;

import java.io.Serializable;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

/**
 * <p>
 * An implementor of this class knows how to derive interesting statistical info
 * of submissions made to certain exercise.
 * </p>
 * <p>
 * An example of this kind of info: which of some exercises sub-questions was
 * the most difficult ie. users received on average the lowest score from.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <E>
 *            {@link ExerciseData} specific to the exercise-type
 * @param <S>
 *            {@link SubmissionInfo} specific to the exercise-type
 */
public interface SubmissionStatisticsGiver<E extends ExerciseData, S extends SubmissionInfo>
		extends Serializable {

	/**
	 * Loads the statistics-giver with data. The method initializes the
	 * statistics-giver-object.
	 * 
	 * @param exercise
	 *            {@link ExerciseData} object to be loaded; this is the exercise
	 *            all the submissions were made to
	 * @param dataObjects
	 *            a list of {@link StatisticalSubmissionInfo}-wrappers of
	 *            submissions made to given exercise that are going to be
	 *            analyzed
	 * @param localizer
	 *            {@link Localizer} making it possible to localize UI
	 * @throws ExerciseException
	 *             if there is an error in the data or in initialization in
	 *             general
	 */
	void initialize(E exercise, List<StatisticalSubmissionInfo<S>> dataObjects,
			Localizer localizer, TempFilesManager tempManager)
			throws ExerciseException;

	/**
	 * <p>
	 * Returns a UI visualizing the statistics loaded to this
	 * {@link SubmissionStatisticsGiver}
	 * </p>
	 * <p>
	 * Even though it is possible to parse a {@link Table} directly from data
	 * defined by the {@link #getAsTabularData()} this won't be done
	 * automatically, meaning that if such a table is wanted it must be added by
	 * the implementor. See package edu.vserver.exercises.helpers for more info.
	 * </p>
	 * 
	 * @return The UI through which the user can view different statistics about
	 *         the loaded data
	 */
	Component getView();

	/**
	 * <p>
	 * Returns a list of {@link StatInfoColumn}s defining interesting columns
	 * and data for those columns.
	 * </p>
	 * <p>
	 * <b>Result of this method should not include generic columns like
	 * "time-on-task"!</b> Only columns that have data truly original to given
	 * exercise-type should be added. This to avoid duplicate columns as columns
	 * like name, score, time-on-task, visualize-button etc. will be added by
	 * the framework. However these kinds of columns can be shown in a table
	 * returned by {@link #getView()} in addition to specific columns, if that
	 * might add to that view's functionality.
	 * </p>
	 * <p>
	 * The order of returned {@link StatInfoColumn}s is irrelevant, it is
	 * however absolutely required that the order of the dataObjects returned
	 * from {@link StatisticsInfoColumn #getDataObjects()} matches the order of
	 * loaded {@link StatisticalSubmissionInfo}s.
	 * </p>
	 * <p>
	 * This format augmented with generic columns like name of the user who made
	 * certain submission is used to automatically generate {@link Table}s and
	 * to export the data to different tabular data formats including Excel or
	 * CSV
	 * </p>
	 * 
	 * @return {@link List} of {@link StatisticalSubmissionInfo}s defining the
	 *         statistics as tabular data
	 */
	List<StatisticsInfoColumn<?>> getAsTabularData();

}
