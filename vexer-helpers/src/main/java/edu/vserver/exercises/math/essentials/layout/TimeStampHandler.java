package edu.vserver.exercises.math.essentials.layout;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeStampHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1442223078865521609L;
	private final ArrayList<TimeStamp> stamps;

	public static String startExercise = "startExercise";
	public static String submitExercise = "submitExercise";
	public static String resetExercise = "resetExercise";
	public static String nextButton = "NextButtonListener.buttonClick()";
	public static String prevButton = "PrevButtonListener.buttonClick()";
	public static String checkButton = "CheckButtonListener.buttonClick()";
	public static String feedbackChecked = "feedbackChecked";
	public static String separator = ";";
	
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
