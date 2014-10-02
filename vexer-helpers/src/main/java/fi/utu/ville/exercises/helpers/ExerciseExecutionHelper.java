package fi.utu.ville.exercises.helpers;

import java.io.Serializable;
import java.util.HashSet;

import com.vaadin.ui.Component;

import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionResult;
import fi.utu.ville.exercises.model.SubmissionType;

/**
 * A helper-class to which a lot of functionality of an {@link Executor}
 * -implementor can be delegated to. This includes handling
 * {@link ExecutionState}, {@link ExecutionStateChangeListener}s and
 * {@link SubmissionListener}s.
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            {@link SubmissionInfo}-implementor
 */
public class ExerciseExecutionHelper<S extends SubmissionInfo> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8263504176402794874L;

	private final HashSet<SubmissionListener<S>> submitListeners = new HashSet<SubmissionListener<S>>();

	private final HashSet<ExecutionStateChangeListener> stateListeners = new HashSet<ExecutionStateChangeListener>();

	private final MutableExecutionState stateHelper = new MutableExecutionState();

	private long startTime;

	/**
	 * Constructs a new {@link ExerciseExecutionHelper} instance and records
	 * start-timestamp.
	 */
	public ExerciseExecutionHelper() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * Registers a {@link SubmissionListener}.
	 * {@link Executor #registerSubmitListener(SubmissionListener)} can be
	 * delegated to this method.
	 * 
	 * @param submitListener
	 *            {@link SubmissionListener} to be registered
	 */
	public void registerSubmitListener(SubmissionListener<S> submitListener) {
		submitListeners.add(submitListener);
	}

	/**
	 * A quite standard {@link ExecutionState}-change after reset. Enables reset
	 * and submit, and informs registered {@link ExecutionStateChangeListener}s.
	 */
	public void informResetDefault() {

		stateHelper.setCanReset(true);
		stateHelper.setCanSubmit(true);

		informStateListeners();

	}

	/**
	 * Returns currently spent timeOnTask and resets the counter if the
	 * {@link SubmissionType} used is not "only for saving state".
	 * 
	 * @param submType
	 *            {@link SubmissionType} used
	 * @return timeOnTask currently spent time-on-task (seconds)
	 */
	private int getTimeOnTask(SubmissionType submType) {
		int timeOnTask = (int) (System.currentTimeMillis() - startTime) / 1000;
		if (!submType.isOnlyForSavingState()) {
			startTime = System.currentTimeMillis();
		}
		return timeOnTask;
	}

	/**
	 * Informs registered {@link SubmissionListener}s with
	 * {@link SubmissionResult} constructed from the parameters, and performs a
	 * quite standard {@link ExecutionState}-change (disable submit, enable
	 * reset) and informs registered {@link ExecutionStateChangeListener}s.
	 * 
	 * @param correctness
	 *            double presenting correctness of submission in range 0.0 - 1.0
	 * @param data
	 *            {@link SubmissionInfo}-object about the submission
	 * @param submType
	 *            used {@link SubmissionType}
	 * @param fbComponent
	 *            possible {@link Component} shown as feedback to the user or
	 *            null
	 */
	public void informSubmitDefault(double correctness, S data,
			SubmissionType submType, Component fbComponent) {

		for (SubmissionListener<S> sListener : submitListeners) {
			sListener.submitted(new SubmissionResult<S>(correctness,
					getTimeOnTask(submType), data, fbComponent, submType));
		}

		stateHelper.setCanReset(true);
		stateHelper.setCanSubmit(false);

		informStateListeners();
	}

	/**
	 * <p>
	 * Informs registered {@link SubmissionListener}s with
	 * {@link SubmissionResult} constructed from the parameters.
	 * </p>
	 * <p>
	 * Does not perform any {@link ExecutionState}-changes.
	 * </p>
	 * 
	 * @param correctness
	 *            double presenting correctness of submission in range 0.0 - 1.0
	 * @param data
	 *            {@link SubmissionInfo}-object about the submission
	 * @param submType
	 *            used {@link SubmissionType}
	 * @param fbComponent
	 *            possible {@link Component} shown as feedback to the user or
	 *            null
	 */
	public void informOnlySubmit(double correctness, S data,
			SubmissionType submType, Component fbComponent) {
		for (SubmissionListener<S> sListener : submitListeners) {
			sListener.submitted(new SubmissionResult<S>(correctness,
					getTimeOnTask(submType), data, fbComponent, submType));
		}
	}

	/**
	 * <p>
	 * Informs registered {@link SubmissionListener}s with given
	 * {@link SubmissionResult}.
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

	/**
	 * Informs all the registered {@link ExecutionStateChangeListener}s. Call
	 * this after making changes to {@link ExecutionState} that can be accessed
	 * through {@link #getState()} to prompt the listeners to act upon the
	 * changes.
	 */
	public void informStateListeners() {
		for (ExecutionStateChangeListener eStateList : stateListeners) {
			eStateList.actOnStateChange(stateHelper);
		}
	}

	/**
	 * Registers a {@link ExecutionStateChangeListener}. Implementing method
	 * {@link Executor #registerExecutionStateChangeListener(ExecutionStateChangeListener)}
	 * can be delegated to this method.
	 * 
	 * @param execStateListener
	 *            {@link ExecutionStateChangeListener} to be registered
	 */
	public void registerExerciseExecutionStateListener(
			ExecutionStateChangeListener execStateListener) {
		stateListeners.add(execStateListener);
	}

	/**
	 * Returns the current {@link ExecutionState}.
	 * {@link Executor #getCurrentExecutionState()} can be delegated to this
	 * method.
	 * 
	 * @return current {@link ExecutionState}
	 */
	public MutableExecutionState getState() {
		return stateHelper;
	}

}
