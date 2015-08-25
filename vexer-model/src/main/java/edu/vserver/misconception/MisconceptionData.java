package edu.vserver.misconception;

import java.io.Serializable;
import java.util.List;


public interface MisconceptionData extends Serializable {

	/**
	 * Save new object to database. Id gets signed when the object is saved.
	 * This metod also saves relate MisconceptionTypeData.
	 */
	public void save();

	/**
	 * Save (update) current object's data to database. Saves (updates) also
	 * related MisconceptionTypeData.
	 */
	public void update();

	/**
	 * Delete this object from database. Deletes also related
	 * MisconceptionTypeData
	 */
	public void delete();
	
	/**
	 * Analyzes the problem and answer stored in this object for common misconceptions. 
	 * If either the problem or answer is null, an empty list is returned
	 * 
	 *  @return a list of misconceptions found
	 */
	public List<MisconceptionTypeData> analyzeForCommonMisconceptions();
	
	public void addMisconceptionTypes(
			List<MisconceptionTypeData> newTypes);

	public void addMIsconceptionType(MisconceptionTypeData type);

	/* Getters and setters */

	public List<MisconceptionTypeData> getTypes();
	
	public boolean isCorrect();

	public void setCorrect(boolean isCorrect);

	public String getProblem();

	public void setProblem(String problem);

	public String getAnswer();

	public void setAnswer(String answer);

	public int getPerformance_id();

	public void setPerformance_id(int performance_id);
	
	public String getCorrectAnswer();

	public void setCorrectAnswer(String correctAnswer);

	public long getTime();

	public void setTime(long time);
	
}
