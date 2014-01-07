package edu.vserver.exercises.model;

import java.io.Serializable;

/**
 * <p>
 * Implementing this interface identifies the implementing class as an exercise
 * type specific submisssion info class.
 * </p>
 * <p>
 * Objects of an implementing class record certain submission made to an
 * exercise of given type. These submissions can be visualized with
 * {@link SubmissionVisualizer} and analyzed with
 * {@link SubmissionStatisticsGiver}, and also loaded as initial state to a
 * {@link Executor}.
 * </p>
 * <p>
 * The persistent storage form of {@link SubmissionInfo}-objects of certain
 * exercise-type is determined by the exercise type's {@link PersistenceHandler}
 * -implementor.
 * </p>
 * 
 * @author Riku Haavisto, Johannes Holvitie
 * 
 */
public interface SubmissionInfo extends Serializable {

}
