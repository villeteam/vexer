package edu.vserver.misconception;

import java.io.Serializable;
import java.util.List;

/**
 * MathPerformanceData is a class that wraps assignment's data in one class. ArrayList "mmd" (MathMisconcpetionData) contains all individual questions and their
 * misconception types.
 * 
 * @author chgt
 * 
 */
public interface MisconceptionPerformanceData extends Serializable {
	
	/**
	 * Save new object to database
	 */
	
	public void save();
	
	public void addMisconceptionData(MisconceptionData data);
	
	/* Getters and setters */
	
	public int getAssigId();
	
	public void setAssigId(int assigId);
	
	public boolean isSubmitted();
	
	public void setSubmitted(boolean isSubmitted);
	
	public List<MisconceptionData> getMmd();
	
	public void setMmd(List<MisconceptionData> mmd);
	
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
	
}
