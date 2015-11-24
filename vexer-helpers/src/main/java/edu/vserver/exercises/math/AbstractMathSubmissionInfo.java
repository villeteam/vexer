package edu.vserver.exercises.math;

import java.util.ArrayList;

import edu.vserver.exercises.math.essentials.layout.Problem;
import edu.vserver.exercises.math.essentials.layout.TimeStampHandler;
import edu.vserver.exercises.math.essentials.level.DiffLevel;
import edu.vserver.exercises.math.essentials.level.LevelMathSubmissionInfo;

public abstract class AbstractMathSubmissionInfo<P extends Problem> implements
		LevelMathSubmissionInfo<P> {
		
	/**
	* 
	*/
	private static final long serialVersionUID = 8911303192857088550L;
	
	private TimeStampHandler timeStamps;
	
	private ArrayList<P> userProblemsAndAnswers;
	private DiffLevel usedLevel;
	
	public AbstractMathSubmissionInfo() {
	
	}
	
	/**
	 * @param userProblemsAndAnswers
	 *            the userProblemsAndAnswers to set
	 */
	@Override
	public void setUserProblemsAndAnswers(ArrayList<P> userProblemsAndAnswers) {
		this.userProblemsAndAnswers = userProblemsAndAnswers;
	}
	
	/**
	 * @param timeStamps
	 *            the timeStamps to set
	 */
	@Override
	public void setTimeStamps(TimeStampHandler timeStamps) {
		this.timeStamps = timeStamps;
	}
	
	@Override
	public void setDiffLevel(DiffLevel usedLevel) {
		this.usedLevel = usedLevel;
		
	}
	
	@Override
	public ArrayList<P> getProblems() {
		return userProblemsAndAnswers;
	}
	
	@Override
	public DiffLevel getDiffLevel() {
		return usedLevel;
	}
	
	/**
	 * @return the timeStamps
	 */
	@Override
	public TimeStampHandler getTimeStamps() {
		return timeStamps;
	}
	
}
