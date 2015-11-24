package fi.utu.ville.exercises.model;

import java.io.Serializable;

/**
 * A class containing general info of the exercise-instance under edit. This class makes it possible for the testing views to show correct name and description
 * for the exercise even though it is not yet saved persistently as a new exercise-instance.
 * 
 * @author Riku Haavisto
 * 
 */
public interface GeneralExerciseInfo extends Serializable {
	
	/**
	 * @return name of the exercise-instance
	 */
	String getName();
	
	/**
	 * @return description for the exercise-instance
	 */
	String getDescription();
}
