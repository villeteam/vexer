package edu.vserver.exercises.template;

import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.helpers.ExerciseExecutionHelper;
import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.ExecutionState;
import edu.vserver.exercises.model.ExecutionStateChangeListener;
import edu.vserver.exercises.model.Executor;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.SubmissionListener;
import edu.vserver.exercises.model.SubmissionType;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

public class TemplateExecutor extends VerticalLayout implements
		Executor<TemplateExerciseData, TemplateSubmissionInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2682119786422750060L;

	private final ExerciseExecutionHelper<TemplateSubmissionInfo> execHelper =

	new ExerciseExecutionHelper<TemplateSubmissionInfo>();

	private final TextField answerField = new TextField("answer:");

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
			this.addComponent(new Image(null, exerciseData.getImgFile()
					.getAsResource()));
		}
		this.addComponent(new Label(exerciseData.getQuestion()));
		answerField.setValue(oldAnswer);
		this.addComponent(answerField);
	}

	@Override
	public void registerSubmitListener(
			SubmissionListener<TemplateSubmissionInfo> submitListener) {
		execHelper.registerSubmitListener(submitListener);
	}

	@Override
	public Layout getView() {
		return this;
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
