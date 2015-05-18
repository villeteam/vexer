package edu.vserver.math;

import edu.vserver.exercises.math.essentials.layout.Problem;
import edu.vserver.exercises.math.essentials.level.LevelMathDataWrapper;
import edu.vserver.exercises.math.essentials.level.LevelMathSubmissionInfo;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionVisualizer;

public interface SubmissionVisualizerProvider {

	<E extends ExerciseData, S extends LevelMathSubmissionInfo<P>, P extends Problem> AbstractMathTableSubmissionViewer<LevelMathDataWrapper<E>, S, P> getMathTableSubmissionVisualizer();
	
	/**
	 * Get the default submissionViewer for the current environment.
	 * @return A submissionVisualizer
	 */
	<E extends ExerciseData, S extends LevelMathSubmissionInfo<Problem>, P extends Problem> SubmissionVisualizer<E, S> getMathSubmissionVisualizer();
	
	/**
	 * Get a submission visualizer to the current environment.
	 * @param specificImplementation the specific implementation of the submission visualizer to return or null to return default
	 * @return A submissionVisualizer
	 */
	<E extends ExerciseData, S extends LevelMathSubmissionInfo<Problem>, P extends Problem> SubmissionVisualizer<E, S> getMathSubmissionVisualizer(SubmissionVisualizer<E, S> specificImplementation);
	
	<E extends ExerciseData, S extends SubmissionInfo> SubmissionVisualizer<E, S> getSubmissionVisualizer();

}
