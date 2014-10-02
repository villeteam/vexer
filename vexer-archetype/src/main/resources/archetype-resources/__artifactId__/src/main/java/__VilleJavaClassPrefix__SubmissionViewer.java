package ${package};

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

public class ${VilleJavaClassPrefix}SubmissionViewer extends VerticalLayout implements
		SubmissionVisualizer<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6260031633710031462L;
	private ${VilleJavaClassPrefix}ExerciseData exer;
	private ${VilleJavaClassPrefix}SubmissionInfo submInfo;

	private Localizer localizer;
	
	public ${VilleJavaClassPrefix}SubmissionViewer() {
	}

	@Override
	public void initialize(${VilleJavaClassPrefix}ExerciseData exercise,
			${VilleJavaClassPrefix}SubmissionInfo dataObject, Localizer localizer,
			TempFilesManager tempManager) throws ExerciseException {
		this.localizer = localizer;
		this.exer = exercise;
		this.submInfo = dataObject;
		doLayout();
	}

	private void doLayout() {
		this.addComponent(new Label(localizer.getUIText(${VilleJavaClassPrefix}UiConstants.QUESTION) + 
				": " + exer.getQuestion()));
		Label answ = new Label(localizer.getUIText(${VilleJavaClassPrefix}UiConstants.ANSWER) + 
				": "  + submInfo.getAnswer());
		answ.addStyleName(${VilleJavaClassPrefix}ThemeConsts.ANSWER_STYLE);
		this.addComponent(answ);
	}

	@Override
	public Component getView() {
		return this;
	}

	@Override
	public String exportSubmissionDataAsText() {
		return localizer.getUIText(${VilleJavaClassPrefix}UiConstants.QUESTION, "\n", 
				exer.getQuestion(), submInfo.getAnswer());
		
	}

}
