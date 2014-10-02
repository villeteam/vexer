package fi.utu.ville.exercises.helpers;

import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.SubmissionType;

/**
 * <p>
 * The {@link #NORMAL}-submission-type should be used as {@link SubmissionType}
 * when a submission is prompted directly through the executor without
 * {@link Executor#askSubmit(SubmissionType)} being called. For example when a
 * student has answered the last question of an exercise it might be nice to
 * immediately ask the student whether she/he wants to submit the answers.
 * </p>
 * <p>
 * In production ViLLE there might be lots of other {@link SubmissionType}s used
 * to pass around some information about how certain submission should be
 * handled. The implementor should always pass the {@link SubmissionType}-object
 * it receives when its {@link Executor #askSubmit(SubmissionType) askSubmit()}
 * -method is called as such.
 * </p>
 * <p>
 * {@link #AUTO_SAVE}-type might be dropped as prompting for auto-saves should
 * be on the responsibility of the surrounding ViLLE-system not on the
 * responsibility of certain exercise-type.
 * </p>
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
