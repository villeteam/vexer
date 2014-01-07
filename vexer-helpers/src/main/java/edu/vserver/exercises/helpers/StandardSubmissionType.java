package edu.vserver.exercises.helpers;

import edu.vserver.exercises.model.Executor;
import edu.vserver.exercises.model.SubmissionType;

/**
 * The {@link #NORMAL}-submission-type should be used as {@link SubmissionType}
 * when a submission is prompted directly through the executor without
 * {@link Executor#askSubmit(SubmissionType)} being called. For example when a
 * student has answered the last question of an exercise it might be nice to
 * immediately ask the student whether she/he wants to submit the answers.
 * 
 * @author Riku Haavisto
 * 
 */
public enum StandardSubmissionType implements SubmissionType {

	NORMAL(false, false), AUTO_SAVE(true, true);

	private final boolean bgSubm;
	private final boolean onlyForSaving;

	private StandardSubmissionType(boolean bgSubm, boolean onlyForSaving) {
		this.bgSubm = bgSubm;
		this.onlyForSaving = onlyForSaving;
	}

	@Override
	public boolean isBackgroundSubmission() {
		return bgSubm;
	}

	@Override
	public boolean isOnlyForSavingState() {
		return onlyForSaving;
	}

}
