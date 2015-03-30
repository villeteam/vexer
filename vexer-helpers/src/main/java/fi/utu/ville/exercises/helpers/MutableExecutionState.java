package fi.utu.ville.exercises.helpers;

import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.SubmissionType;

/**
 * <p>
 * A mutable implementor for the {@link ExecutionState} -interface.
 * </p>
 * <p>
 * This class is useful for keeping track of the current execution-state.
 * </p>
 * <p>
 * Changing some of the variables of this class does not trigger any event in
 * itself, but the user of this class must inform registered listeners at an
 * appropriate time about the changes by calling their
 * {@link ExecutionStateChangeListener #actOnStateChange(ExecutionState)}
 * -methods with this object (or any other implementor of {@link ExecutionState}
 * derived from it) as an argument.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 */
public class MutableExecutionState implements ExecutionState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5926781775292208999L;
	private boolean canReset;
	private boolean canSubmit;
	private boolean menuEnabled;
	private boolean resetShown;
	private boolean submitShown;
	private boolean hasUnsubmittedChanges;
	private boolean hasUnsubmittedGeogebraChanges;

	/**
	 * Constructs a new {@link MutableExecutionState} with default values.
	 */
	public MutableExecutionState() {
		this.canReset = true;
		this.canSubmit = true;
		this.menuEnabled = true;
		this.submitShown = true;
		this.resetShown = true;
		this.hasUnsubmittedChanges = false;
		this.hasUnsubmittedGeogebraChanges = false;
	}

	/**
	 * Constructs a new {@link MutableExecutionState} with given values.
	 * 
	 * @param canReset
	 *            initial value of allowing reset
	 * @param canSubmit
	 *            initial value of allowing submit
	 * @param resetShown
	 *            initial value of whether reset-button is shown
	 * @param submitShown
	 *            initial value of whether submit-button is shown
	 * @param menuEnabled
	 *            initial value of whether navigation menu is shown
	 * @param hasUnsubmittedChanges
	 *            initial value of whether there are unsubmitted changes in the
	 *            exercise (usually false)
	 */
	public MutableExecutionState(boolean canReset, boolean canSubmit,
			boolean resetShown, boolean submitShown, boolean menuEnabled,
			boolean hasUnsubmittedChanges) {
		this.canReset = canReset;
		this.canSubmit = canSubmit;
		this.menuEnabled = menuEnabled;
		this.submitShown = submitShown;
		this.resetShown = resetShown;
		this.hasUnsubmittedChanges = hasUnsubmittedChanges;
	}

	@Override
	public boolean isResetShown() {
		return resetShown;
	}

	@Override
	public boolean isSubmitShown() {
		return submitShown;
	}

	@Override
	public boolean isAllowReset() {
		return canReset;
	}

	@Override
	public boolean isAllowSubmit() {
		return canSubmit;
	}

	@Override
	public boolean isMenuEnabled() {
		return menuEnabled;
	}

	/**
	 * @param canReset
	 *            the canReset to set
	 */
	public void setCanReset(boolean canReset) {
		this.canReset = canReset;
	}

	/**
	 * @param canSubmit
	 *            the canSubmit to set
	 */
	public void setCanSubmit(boolean canSubmit) {
		this.canSubmit = canSubmit;
	}

	/**
	 * @param menuEnabled
	 *            the menuEnabled to set
	 */
	public void setMenuEnabled(boolean menuEnabled) {
		this.menuEnabled = menuEnabled;
	}

	/**
	 * @param resetShown
	 *            the resetShown to set
	 */
	public void setResetShown(boolean resetShown) {
		this.resetShown = resetShown;
	}

	/**
	 * @param submitShown
	 *            the submitShown to set
	 */
	public void setSubmitShown(boolean submitShown) {
		this.submitShown = submitShown;
	}

	@Override
	public boolean hasUnsubmittedChanges() {
		return hasUnsubmittedChanges;
	}

	/**
	 * <p>
	 * Clears the 'hasUnsubmittedChanges'-flag.
	 * </p>
	 * <p>
	 * Should only be called after making a submission with such a
	 * {@link SubmissionType} that returns <b>false</b> from
	 * {@link SubmissionType #isOnlyForSavingState()}-method.
	 * </p>
	 */
	public void clearUnSubmChangesFlag() {
		hasUnsubmittedChanges = false;
	}

	/**
	 * <p>
	 * Sets the 'hasUnsubmittedChanges'-flag.
	 * </p>
	 * <p>
	 * This method should be called after any change the user has made to the
	 * state of the {@link Executor} regardless of whether the flag is already
	 * set or not.
	 * </p>
	 * </p>
	 */
	public void setUnSubmChangesFlag() {
		hasUnsubmittedChanges = true;
	}

	@Override
	public boolean hasUnsubmittedGeogebraChanges() {
		return hasUnsubmittedGeogebraChanges;
	}
	
	public void clearUnSubmGeogebraChangesFlag() {
		hasUnsubmittedGeogebraChanges = false;
	}

	public void setUnSubmGeogebraChangesFlag() {
		hasUnsubmittedGeogebraChanges = true;
	}

}
