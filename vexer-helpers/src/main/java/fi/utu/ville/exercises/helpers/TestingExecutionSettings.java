package fi.utu.ville.exercises.helpers;

import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.EditorHelper;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExerciseData;

/**
 * <p>
 * An implementor of {@link ExecutionSettings} that can be used for testing an
 * exercise ({@link ExerciseData}-instance) in for example an {@link Editor}
 * </p>
 * <p>
 * Most exercise-types have no reason to use this class directly as they can use
 * {@link EditorHelper #getControlbar(fi.utu.ville.exercises.model.EditorHelper.EditedExerciseGiver)
 * EditorHelper.getControlbar()} which provides a testing view to editor.
 * </p>
 * 
 * @author Riku Haavisto
 */
public final class TestingExecutionSettings implements ExecutionSettings {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4799031380567118684L;

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
