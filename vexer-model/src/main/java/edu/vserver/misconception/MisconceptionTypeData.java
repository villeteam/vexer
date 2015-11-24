package edu.vserver.misconception;

import java.io.Serializable;
import java.util.ArrayList;

public interface MisconceptionTypeData extends Serializable {
	
	/**
	 * Saves misconception type. This usually gets called by MathMisconceptionData.
	 */
	public void save();
	
	/**
	 * Saves (updates) misconception type. This usually gets called by MathMisconceptionData.
	 */
	public void update();
	
	/**
	 * Deletes misconception type. This usually gets called by MathMisconceptionData.
	 */
	public void delete();
	
	/**
	 * Helper method for getting an arraylist from a single MathMisconceptionTypeData
	 * 
	 * @return an ArrayList containing this single instance
	 */
	public ArrayList<MisconceptionTypeData> toList();
	
	/* Getters and setters */
	
	public int getId();
	
	public void setId(int id);
	
	public int getMisconceptionId();
	
	public void setMisconceptionId(int misconceptionId);
	
	public Misconception getType();
	
	public void setType(Misconception type);
	
	public String typeToString();
	
	public void setTypeFromString(String str);
}
