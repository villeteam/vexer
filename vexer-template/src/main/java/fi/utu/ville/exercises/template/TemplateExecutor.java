package fi.utu.ville.exercises.template;

import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.helpers.ExerciseExecutionHelper;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.NoMisconceptionExecutor;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionType;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

public class TemplateExecutor extends NoMisconceptionExecutor<TemplateExerciseData, TemplateSubmissionInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2682119786422750060L;

	private final ExerciseExecutionHelper<TemplateSubmissionInfo> execHelper =

	new ExerciseExecutionHelper<TemplateSubmissionInfo>();

	private final TextField answerField = new TextField("answer:");

	private VerticalLayout exerLayout = new VerticalLayout();
	
	public TemplateExecutor() {

	}

	@Override
	public void initialize(Localizer localizer,
			TemplateExerciseData exerciseData, TemplateSubmissionInfo oldSubm,
			TempFilesManager materials, ExecutionSettings fbSettings)
			throws ExerciseException {
		doLayout(exerciseData, oldSubm != null ? oldSubm.getAnswer() : "");
	}

	private void doLayout(TemplateExerciseData exerciseData, String oldAnswer) {
		if (exerciseData.getImgFile() != null) {
			exerLayout.addComponent(new Image(null, exerciseData.getImgFile()
					.getAsResource()));
		}
		exerLayout.addComponent(new Label(exerciseData.getQuestion()));
		answerField.setValue(oldAnswer);
		exerLayout.addComponent(answerField);
	}

	@Override
	public void registerSubmitListener(
			SubmissionListener<TemplateSubmissionInfo> submitListener) {
		execHelper.registerSubmitListener(submitListener);
	}

	@Override
	public Layout getView() {
		return exerLayout;
	}

	@Override
	public void shutdown() {
		// nothing to do here
	}

	@Override
	public void askReset() {
		// nothing to do here
	}

	@Override
	public ExecutionState getCurrentExecutionState() {
		return execHelper.getState();
	}

	@Override
	public void askSubmit(SubmissionType submType) {
		double corr = 1.0;

		String answer = answerField.getValue();
		execHelper.informOnlySubmit(corr, new TemplateSubmissionInfo(answer),
				submType, null);

	}

	@Override
	public void registerExecutionStateChangeListener(
			ExecutionStateChangeListener execStateListener) {
		execHelper.registerExerciseExecutionStateListener(execStateListener);

	}

}
