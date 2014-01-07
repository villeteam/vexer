package edu.vserver.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementing this interface identifies the implementing class as an exercise-
 * type specific data-class.
 * </p>
 * <p>
 * Objects of an implementing class record certain instance of certain
 * exercise-type and can be loaded to a suitable {@link Executor} for execution.
 * Instances can be created, edited and saved with a suitable {@link Editor}.
 * </p>
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 */
public interface ExerciseData extends Serializable {

}
