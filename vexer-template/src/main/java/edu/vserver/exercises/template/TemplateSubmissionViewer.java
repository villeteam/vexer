package edu.vserver.exercises.template;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

public class TemplateSubmissionViewer extends VerticalLayout implements
		SubmissionVisualizer<TemplateExerciseData, TemplateSubmissionInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6260031633710031462L;
	private TemplateExerciseData exer;
	private TemplateSubmissionInfo submInfo;

	public TemplateSubmissionViewer() {
	}

	@Override
	public void initialize(TemplateExerciseData exercise,
			TemplateSubmissionInfo dataObject, Localizer localizer,
			TempFilesManager tempManager) throws ExerciseException {
		this.exer = exercise;
		this.submInfo = dataObject;
		doLayout();
	}

	private void doLayout() {
		this.addComponent(new Label("Question: " + exer.getQuestion()));
		Label answ = new Label("Answer: " + submInfo.getAnswer());
		answ.addStyleName("template-exercise-answer");
		this.addComponent(answ);
	}

	@Override
	public Component getView() {
		return this;
	}

	@Override
	public String exportSubmissionDataAsText() {
		return "Question: " + exer.getQuestion() + "\n" + "Answer: "
				+ submInfo.getAnswer() + "\n";
	}

}
