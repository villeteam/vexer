package edu.vserver.exercises.math.essentials.layout;

import java.util.ArrayList;
import java.util.List;

import fi.utu.ville.exercises.model.SubmissionInfo;

public interface MathSubInfo<E extends Problem> extends SubmissionInfo {
	
	public List<E> getProblems();
	
	void setUserProblemsAndAnswers(ArrayList<E> userProblemsAndAnswers);
	
	void setTimeStamps(TimeStampHandler timeStamps);
	
	TimeStampHandler getTimeStamps();
}
