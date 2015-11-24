package fi.utu.ville.exercises.model;

import java.io.Serializable;

import com.vaadin.ui.Component;

import edu.vserver.misconception.MisconceptionPerformanceSubject;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * An implementor of this class knows how to execute certain exercise-instance defined by certain {@link ExerciseData} of given exercise-type.
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <E>
 *            {@link ExerciseData} instances of which can be executed with this implementor
 * @param <S>
 *            {@link SubmissionInfo} in which this executor stores info about submissions
 */
public interface Executor<E extends ExerciseData, S extends SubmissionInfo>
		extends Serializable {
		
	/**
	 * Loads the executor with data. This method must be called before calling other methods of the executor to initialize the executor.
	 * 
	 * @param localizer
	 *            {@link Localizer} making it possible to localize UI
	 * @param exerciseData
	 *            {@link ExerciseData} object to be loaded for the executor
	 * @param oldSubm
	 *            {@link SubmissionInfo} of old submission that can be shown to the user (optional); null if there is no prior submission
	 * @param tempManager
	 *            {@link TempFilesManager} giving access to a private temporary-folder if needed
	 * @param execSettings
	 *            {@link ExecutionSettings} that might affect how the {@link Executor} should behave
	 * @throws ExerciseException
	 *             if something goes wrong in the initialization (eg. a failure to fetch critical resources)
	 */
	void initialize(Localizer localizer, E exerciseData, S oldSubm,
			TempFilesManager tempManager, ExecutionSettings execSettings)
					throws ExerciseException;
					
	/**
	 * Listeners registered through this method are called when a successful submission is made (can also be a save-submission as defined by
	 * {@link SubmissionType}).
	 * 
	 * @param submitListener
	 *            the {@link SubmissionListener} to register
	 */
	void registerSubmitListener(SubmissionListener<S> submitListener);
	
	/**
	 * @return The GUI through which the user can execute certain exercise-instance
	 */
	Component getView();
	
	/**
	 * <p>
	 * Informs the {@link Executor} that it will be shut-down and it should perform any clean-up it might need.
	 * </p>
	 * <p>
	 * This method might be called more than once. Calls after clean-up is done must be ignored.
	 * </p>
	 */
	void shutdown();
	
	/**
	 * Asks the executor to reset the current situation of the execution and load the state again from the info given in
	 * {@link #initialize(Localizer, ExerciseData, SubmissionInfo, ExerciseMaterialManager, ExecutionSettings) | initialize()}-method with the exception also
	 * oldSubm should be discarded on reset. Succeeds if the executor is in suitable {@link ExecutionState}.
	 */
	void askReset();
	
	/**
	 * <p>
	 * Asks the executor to submit a {@link SubmissionResult}-object from the current state of the execution and inform listeners registered through
	 * {@link #registerSubmitListener(SubmissionListener)}. Succeeds if the executor is in suitable {@link ExecutionState}.
	 * </p>
	 * <p>
	 * {@link SubmissionType} given as a parameter must be passed to the {@link SubmissionResult}-object used as such as the external system uses that info to
	 * know how to handle each submission (are they for example only saving the current state of the exercise or are they real submissions asked by the user for
	 * which to show feedback etc.). It should also be noted that no error messages etc. should be shown to the user in case of a back-ground submission (
	 * {@link SubmissionType #isBackgroundSubmission()}) that fails because of the state of the executor.
	 * </p>
	 * 
	 * @param askedSubmType
	 *            What kind of submissions should be done
	 */
	void askSubmit(SubmissionType askedSubmType);
	
	/**
	 * Listeners registered through this method are called whenever the {@link ExecutionState} of the executor changes.
	 * 
	 * @param execStateListener
	 *            {@link ExecutionStateChangeListener} to register
	 */
	void registerExecutionStateChangeListener(
			ExecutionStateChangeListener execStateListener);
			
	/**
	 * @return the current {@link ExecutionState} of the executor
	 */
	ExecutionState getCurrentExecutionState();
	
	/**
	 * Sets the subject for which misconceptions are collected. (e.g. Math)<br>
	 * Does not need an implementation if the exercise returns only one type of misconception.
	 * 
	 * @param misconceptionSubject
	 *            the school subject for misconceptions. Null or NONE for no collection.
	 */
	void setMisconceptionSubject(
			MisconceptionPerformanceSubject misconceptionSubject);
			
	/**
	 * The subject for which misconceptions are collected for this exercise.
	 * 
	 * @return This exercise's MisconceptionPerformanceSubject.
	 */
	MisconceptionPerformanceSubject getMisconceptionSubject();
}
