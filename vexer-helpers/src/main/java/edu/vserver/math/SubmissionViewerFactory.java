package edu.vserver.math;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionVisualizer;

public enum SubmissionViewerFactory {
	NORMAL,
	TABBED;
	
	public static <E extends ExerciseData, S extends SubmissionInfo> SubmissionVisualizer<E, S> getVisualizer(SubmissionViewerFactory type) {
		switch (type) {
		case NORMAL:
			//return SubmissionVisualizer<E, S>
			break;
		case TABBED:
			break;
		default:
			break;
			
		}
		return null;
	}
}
