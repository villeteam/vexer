package fi.utu.ville.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementors of this interface registered through {@link EditorHelper #registerExerSaveListener(ExerciseSaveListener)} are called with a new
 * {@link ExerciseData} instance when a user prompts the editor to save the data
 * </p>
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 * @param <E>
 *            {@link ExerciseData} suitable for exercise-type
 */
public interface ExerciseSaveListener<E extends ExerciseData> extends
		Serializable {
		
	/**
	 * Called with a new {@link ExerciseData} instance when a user prompts the editor to save the data.
	 * 
	 * @param dataToSave
	 *            {@link ExerciseData} that the implementor should act upon
	 */
	void actOnExerciseTypeSave(E dataToSave);
	
}
