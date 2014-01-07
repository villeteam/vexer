package edu.vserver.exercises.helpers;

import edu.vserver.exercises.model.Editor;
import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.ExerciseData;

/**
 * An implementor of {@link ExecutionSettings} that can be used for testing an
 * exercise ({@link ExerciseData}-instance) in for example an {@link Editor}
 * 
 * @author Riku Haavisto
 */
public final class TestingExecutionSettings implements ExecutionSettings {

	public static final TestingExecutionSettings INSTANCE =

	new TestingExecutionSettings();

	private TestingExecutionSettings() {

	}

	@Override
	public boolean isNoTips() {
		return false;
	}

	@Override
	public boolean isNoCorrAnswers() {
		return false;
	}

	@Override
	public boolean isExam() {
		return false;
	}

}
