package edu.vserver.math;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.math.essentials.layout.MathExerciseView;
import edu.vserver.exercises.math.essentials.layout.Problem;
import edu.vserver.exercises.math.essentials.level.LevelMathDataWrapper;
import edu.vserver.exercises.math.essentials.level.LevelMathSubmissionInfo;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

public class StubSubmissionVisualizerProvider implements SubmissionVisualizerProvider {
	
	@Override
	public <E extends ExerciseData, S extends SubmissionInfo> SubmissionVisualizer<E, S> getSubmissionVisualizer() {
		return new SubmissionVisualizer<E, S>() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void initialize(E exercise, S dataObject,
					Localizer localizer, TempFilesManager tempManager)
							throws ExerciseException {
			}
			
			@Override
			public Component getView() {
				return new VerticalLayout();
			}
			
			@Override
			public String exportSubmissionDataAsText() {
				return "";
			}
		};
	}
	
	@Override
	public <E extends ExerciseData, S extends LevelMathSubmissionInfo<P>, P extends Problem> AbstractMathTableSubmissionViewer<LevelMathDataWrapper<E>, S, P> getMathTableSubmissionVisualizer() {
		return new AbstractMathTableSubmissionViewer<LevelMathDataWrapper<E>, S, P>() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected MathExerciseView<P> getExerView(
					LevelMathDataWrapper<E> exerData, S submInfo) {
				return getExerView(exerData, submInfo);
			}
		};
	}
	
	@Override
	public <E extends ExerciseData, S extends LevelMathSubmissionInfo<Problem>, P extends Problem> SubmissionVisualizer<E, S> getMathSubmissionVisualizer() {
		return getMathSubmissionVisualizer(null);
		
	}
	
	@Override
	public <E extends ExerciseData, S extends LevelMathSubmissionInfo<Problem>, P extends Problem> SubmissionVisualizer<E, S> getMathSubmissionVisualizer(
			SubmissionVisualizer<E, S> specificImplementation) {
		if (specificImplementation == null) {
			return new AbstractLevelMathSubmissionViewer<E, S, Problem>() {
				
				private static final long serialVersionUID = 1L;
				
				@Override
				protected Layout doAdditionalSubProbLayout(Problem subProblem) {
					return new VerticalLayout();
				}
				
				@Override
				protected String doAdditionalExportInfo(Problem subProblem) {
					return "";
				}
			};
		} else {
			return specificImplementation;
		}
		
	}
	
}
