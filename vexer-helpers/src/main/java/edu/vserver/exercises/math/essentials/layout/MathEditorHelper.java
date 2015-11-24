package edu.vserver.exercises.math.essentials.layout;

public class MathEditorHelper {
	
	public MathEditorHelper() {
	
	}
	
	public static boolean validateMinMax(double min, double max) {
		if (min < max) {
			return true;
		}
		return false;
	}
	
	public static boolean validateMinMax(int min, int max) {
		if (min < max) {
			return true;
		}
		return false;
	}
}
