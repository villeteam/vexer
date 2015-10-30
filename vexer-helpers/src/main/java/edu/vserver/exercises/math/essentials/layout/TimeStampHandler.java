package edu.vserver.exercises.math.essentials.layout;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeStampHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1442223078865521609L;
	private final ArrayList<TimeStamp> stamps;

	public static final String startExercise = "startExercise";
	public static final String submitExercise = "submitExercise";
	public static final String resetExercise = "resetExercise";
	public static final String nextButton = "NextButtonListener.buttonClick()";
	public static final String prevButton = "PrevButtonListener.buttonClick()";
	public static final String checkButton = "CheckButtonListener.buttonClick()";
	public static final String feedbackChecked = "feedbackChecked";
	public static final String separator = ";";
	
	public TimeStampHandler() {
		stamps = new ArrayList<TimeStamp>();
	}

	public void add(String eventStr) {
		stamps.add(new TimeStamp(eventStr, System.currentTimeMillis()));
	}

	public void add(TimeStamp stamp) {
		stamps.add(stamp);
	}

	public TimeStamp get(int i) {
		return stamps.get(i);
	}

	public int size() {
		return stamps.size();
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < stamps.size(); ++i) {
			result += stamps.get(i) + System.getProperty("line.separator");
		}

		return result;
	}
}
