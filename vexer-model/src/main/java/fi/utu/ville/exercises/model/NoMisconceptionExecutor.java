package fi.utu.ville.exercises.model;

import edu.vserver.misconception.MisconceptionPerformanceSubject;

public abstract class NoMisconceptionExecutor<E extends ExerciseData, S extends SubmissionInfo> implements Executor<E, S> {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void setMisconceptionSubject(
			MisconceptionPerformanceSubject misconceptionSubject) {
	}
	
	@Override
	public MisconceptionPerformanceSubject getMisconceptionSubject() {
		return MisconceptionPerformanceSubject.NONE;
	}
	
}
