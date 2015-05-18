package edu.vserver.exercises.math.essentials.level;

import java.io.Serializable;

import fi.utu.ville.exercises.model.ExerciseData;

public class LevelMathDataWrapper<E extends ExerciseData> implements
		ExerciseData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4324121022053916859L;
	private final E easy;
	private final E normal;
	private final E hard;

	public LevelMathDataWrapper(E easy, E normal, E hard) {
		this.easy = easy;
		this.normal = normal;
		this.hard = hard;
	}

	public E getForLevel(DiffLevel level) {
		if (level == DiffLevel.EASY) {
			return easy;
		} else if (level == DiffLevel.NORMAL) {
			return normal;
		} else if (level == DiffLevel.HARD) {
			return hard;
		} else {
			throw new IllegalArgumentException(
					"Difficulty level not implemented in class " + getClass());
		}
	}

}
