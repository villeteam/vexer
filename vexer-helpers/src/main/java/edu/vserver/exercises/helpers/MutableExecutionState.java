package edu.vserver.exercises.helpers;

import edu.vserver.exercises.model.ExecutionState;
import edu.vserver.exercises.model.ExecutionStateChangeListener;

/**
 * <p>
 * A mutable implementor for the {@link ExecutionState} -interface.
 * </p>
 * <p>
 * This class is useful for keeping track of the current execution-state.
 * </p>
 * <p>
 * Changing some of the variables of this class does not tricker any event in
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

	public MutableExecutionState() {
		this.canReset = true;
		this.canSubmit = true;
		this.menuEnabled = true;
		this.submitShown = true;
		this.resetShown = true;
		this.hasUnsubmittedChanges = false;
	}

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

	public void clearUnSubmChangesFlag() {
		hasUnsubmittedChanges = false;
	}

	public void setUnSubmChangesFlag() {
		hasUnsubmittedChanges = true;
	}

}
