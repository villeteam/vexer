package fi.utu.ville.exercises.model;

import edu.vserver.misconception.MisconceptionPerformanceSubject;

public abstract class AbstractMisconceptionExecutor<E extends ExerciseData, S extends SubmissionInfo> implements Executor<E, S> {

	private static final long serialVersionUID = 1L;

	private MisconceptionPerformanceSubject performanceSubject;
	
	@Override
	public void setMisconceptionSubject(
			MisconceptionPerformanceSubject misconceptionSubject) {
		performanceSubject = misconceptionSubject;
		
	}

	@Override
	public MisconceptionPerformanceSubject getMisconceptionSubject() {
		return performanceSubject;
	}
}
