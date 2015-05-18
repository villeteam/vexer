package edu.vserver.misconception;

import java.io.Serializable;
import java.util.ArrayList;


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
	 * Loads all related misconception types
	 */
	public void loadMisconceptionTypeData();

	/**
	 * Save the misconceptionId to MathMisconeptionTypeData
	 * 
	 * @param id
	 *            The ID of this class in DB.
	 */
	public void setMisconceptionIdToMisconceptionTypeData(int id);

	/**
	 * Save misconception type data to database
	 */
	public void saveMisconceptionTypeData();

	/**
	 * Update misconception type data to database
	 */
	public void updateMisconceptionTypeData();
	
	/**
	 * Delete misconception type data from database
	 */
	public void deleteMisconceptionTypeData();

	public void addMisconceptionTypes(
			ArrayList<MisconceptionTypeData> newTypes);

	public void addMIsconceptionType(MisconceptionTypeData type);

	/* Getters and setters */

	public ArrayList<MisconceptionTypeData> getTypes();

	public void setTypes(ArrayList<MisconceptionTypeData> types);

	public boolean isCorrect();

	public void setCorrect(boolean isCorrect);

	public String getProblem();

	public void setProblem(String problem);

	public String getAnswer();

	public void setAnswer(String answer);

	public int getPerformance_id();

	public void setPerformance_id(int performance_id);

	public int getId();

	public void setId(int id);
	public String getCorrectAnswer();

	public void setCorrectAnswer(String correctAnswer);

	public long getTime();

	public void setTime(long time);
}
