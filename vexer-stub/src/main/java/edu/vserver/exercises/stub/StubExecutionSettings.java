package edu.vserver.exercises.stub;

import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.Executor;

/**
 * An enum-listing of a few {@link ExecutionSettings}-implementations that can
 * be used for testing {@link Executor}s in a slightly different situations.
 * 
 * @author Riku Haavisto
 * 
 */
enum StubExecutionSettings implements ExecutionSettings {

	STUB_NORMAL(false, false, false),

	STUB_EXAM(true, true, true);

	private final boolean noTips;
	private final boolean noCorrAnsw;
	private final boolean isExam;

	private StubExecutionSettings(boolean noTips, boolean noCorrAnsw,
			boolean isExam) {
		this.noTips = noTips;
		this.noCorrAnsw = noCorrAnsw;
		this.isExam = isExam;
	}

	@Override
	public boolean isNoTips() {
		return noTips;
	}

	@Override
	public boolean isNoCorrAnswers() {
		return noCorrAnsw;
	}

	@Override
	public boolean isExam() {
		return isExam;
	}

}
