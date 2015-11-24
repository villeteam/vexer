package edu.vserver.math;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.math.essentials.layout.MathSubInfo;
import edu.vserver.exercises.math.essentials.layout.Problem;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.OldIcon;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.TempFilesManager;

public abstract class AbstractMathSubmissionViewer<E extends ExerciseData, F extends MathSubInfo<G>, G extends Problem>
		extends VerticalLayout implements SubmissionVisualizer<E, F> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4531189915766596917L;
	private E exercise;
	private F submInfo;
	private Localizer localizer;
	
	public AbstractMathSubmissionViewer() {
		this.setWidth("100%");
	}
	
	protected abstract Layout doAdditionalSubProbLayout(G subProblem);
	
	protected abstract String doAdditionalExportInfo(G subProblem);
	
	protected abstract Layout getStartLayout(F subInfo);
	
	protected abstract String getStartString(F subInfo);
	
	protected Layout getQuestionLayout(G problem) {
		HorizontalLayout res = new HorizontalLayout();
		res.addComponent(new Label(problem.getQuestion(localizer).replaceAll(
				"\n", "<br>"), ContentMode.HTML));
		// res.setWidth("100%");
		return res;
	}
	
	protected Layout getUserAnswerLayout(G problem) {
		HorizontalLayout res = new HorizontalLayout();
		res.addComponent(new Label("<strong>"
				+ localizer.getUIText(StandardUIConstants.ANSWER)
				+ "</strong>: " + problem.getUserAnswer() != null ? problem
						.getUserAnswer() : "N/A",
				ContentMode.HTML));
		// res.setWidth("100%");
		return res;
	}
	
	protected Layout getCorrectAnswerLayout(G problem) {
		
		HorizontalLayout res = new HorizontalLayout();
		res.addComponent(new Label("<strong>"
				+ localizer.getUIText(StandardUIConstants.CORRECT)
				+ "</strong>: " + problem.getCorrectAnswer(), ContentMode.HTML));
		// res.setWidth("100%");
		return res;
	}
	
	protected E getExercise() {
		return exercise;
	}
	
	protected F getSubmInfo() {
		return submInfo;
	}
	
	protected Localizer getLocalizer() {
		return localizer;
	}
	
	@Override
	public final void initialize(E exercise, F dataObject, Localizer localizer,
			TempFilesManager tempFilesManager) {
		this.exercise = exercise;
		this.submInfo = dataObject;
		this.localizer = localizer;
		doLayout();
	}
	
	@Override
	public final Layout getView() {
		return this;
	}
	
	@Override
	public final String exportSubmissionDataAsText() {
		
		String start = getStartString(submInfo);
		String res = start == null ? "" : start;
		int index = 1;
		for (G prob : submInfo.getProblems()) {
			res = res + "Sub-question " + (index++) + " :  -----------------"
					+ "\n";
			res = res + prob.getQuestion(localizer) + "\n";
			res = res + "Correct: " + prob.getCorrectAnswer() + "\n";
			res = res + "User: " + prob.getUserAnswer() + "\n";
			
			String additionalExportInfo = doAdditionalExportInfo(prob);
			
			if (additionalExportInfo != null) {
				res = res + additionalExportInfo;
			}
			
			res = res + "-----------------";
		}
		
		return res;
	}
	
	private void doLayout() {
		
		setSpacing(true);
		setMargin(true);
		setWidth("100%");
		
		Layout startLayout = getStartLayout(submInfo);
		if (startLayout != null) {
			addComponent(startLayout);
		}
		
		for (G problem : submInfo.getProblems()) {
			if (problem.isCorrect()) {
				// init panel for correct answer
				final HorizontalLayout hl = new HorizontalLayout();
				hl.setWidth("100%");
				hl.setSpacing(true);
				hl.setMargin(true);
				hl.setStyleName("stat-answer-panel-correct");
				final Embedded icon = new Embedded("",
						OldIcon.CORRECT_MEDIUM.getIcon());
				hl.addComponent(icon);
				hl.setComponentAlignment(icon, Alignment.TOP_LEFT);
				
				final VerticalLayout vl = new VerticalLayout();
				vl.setSpacing(true);
				vl.addComponent(getQuestionLayout(problem));
				
				vl.addComponent(getUserAnswerLayout(problem));
				
				Layout additLayout = doAdditionalSubProbLayout(problem);
				if (additLayout != null) {
					vl.addComponent(additLayout);
				}
				
				hl.addComponent(vl);
				hl.setExpandRatio(vl, 1.0f);
				
				addComponent(hl);
				
			} else {
				// init panel for incorrect answer
				final HorizontalLayout hl = new HorizontalLayout();
				hl.setWidth("100%");
				hl.setSpacing(true);
				hl.setMargin(true);
				hl.setStyleName("stat-answer-panel-incorrect");
				final Embedded icon = new Embedded("",
						OldIcon.INCORRECT_MEDIUM.getIcon());
				hl.addComponent(icon);
				hl.setComponentAlignment(icon, Alignment.TOP_LEFT);
				
				final VerticalLayout vl = new VerticalLayout();
				vl.setSpacing(true);
				vl.addComponent(getQuestionLayout(problem));
				
				vl.addComponent(getUserAnswerLayout(problem));
				
				vl.addComponent(getCorrectAnswerLayout(problem));
				
				Layout additLayout = doAdditionalSubProbLayout(problem);
				if (additLayout != null) {
					vl.addComponent(additLayout);
				}
				
				hl.addComponent(vl);
				hl.setExpandRatio(vl, 1.0f);
				
				addComponent(hl);
			}
		}
		
	}
	
	public static <E extends ExerciseData, F extends MathSubInfo<G>, G extends Problem> String getSimpleTextExport(
			E exercise, F submInfo, Localizer localizer, String start) {
			
		String res = start == null ? "" : start;
		int index = 1;
		for (G prob : submInfo.getProblems()) {
			res = res + "Sub-question " + (index++) + " :  -----------------"
					+ "\n";
			res = res + prob.getQuestion(localizer) + "\n";
			res = res + "Correct: " + prob.getCorrectAnswer() + "\n";
			res = res + "User: " + prob.getUserAnswer() + "\n";
			
			res = res + "-----------------";
		}
		
		return res;
	}
}
