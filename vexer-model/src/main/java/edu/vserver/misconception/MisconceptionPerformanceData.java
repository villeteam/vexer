package edu.vserver.misconception;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * MathPerformanceData is a class that wraps assignment's data in one class.
 * ArrayList "mmd" (MathMisconcpetionData) contains all individual questions and
 * their misconception types.
 * 
 * @author chgt
 * 
 */
public interface MisconceptionPerformanceData extends Serializable{

		/**
		 * Load data from ResultSet
		 * 
		 * @param rs
		 *            Database ResultSet
		 */
		public void setData(ResultSet rs);

		/**
		 * Save new object to database
		 */

		public void save();

		/**
		 * Save (update) current object's data to database
		 */
		public void update();

		/**
		 * Delete this object from database
		 */

		public void delete();

		/**
		 * Loads all related misconception data
		 */

		public abstract void loadMisconceptionData();

		public void addMisconceptionData(MisconceptionData data);

		/**
		 * Save misconception data to database
		 */
		public void saveMisconceptions();

		/**
		 * Update misconception data to database
		 */
		public void updateMisconception();

		/**
		 * Delete misconception data from database
		 */
		public void deleteMisconception();

		public void setMathPerformanceIdToMisconception();

		/* Getters and setters */

		public int getAssigId();

		public void setAssigId(int assigId);

		public String getUserId();

		public void setUserId(String userId);

		public boolean isSubmitted();

		public void setSubmitted(boolean isSubmitted);

		public ArrayList<MisconceptionData> getMmd();

		public void setMmd(ArrayList<MisconceptionData> mmd);

		public int getId();

		public void setId(int id);

		/**
		 * Set startTime for starting calculation
		 * 
		 * @param time
		 */
		public void setStartTime();

		/**
		 * Set endTime for calculation
		 * 
		 * @param time
		 */

		public void setEndTime();

		/**
		 * Get used time to calculate one calculation
		 * 
		 * @return Time spent solving one exercise
		 */
		public long getUsedTime();

}
