package edu.vserver.exercises.math.essentials.layout;

import java.io.Serializable;

import com.rits.cloning.Cloner;

import fi.utu.ville.standardutils.Localizer;

public interface Problem extends Serializable {
	
	/**
	 * Determines the correctness of this problem by comparing the correct answer to the answer given by the user.
	 * 
	 * @param answer
	 *            the answer given by the user.
	 * @return true if the answer given by the user was correct, false otherwise
	 */
	public boolean tryAnswer(AbstractMathAnswer answer);
	
	/**
	 * Has this problem been solved correctly?
	 * 
	 * @return true if this problem has already been solved correctly, false if tryAnswer has not been called or the user's answer was wrong.
	 */
	public boolean isCorrect();
	
	/**
	 * Gets the localized question from this problem.
	 * 
	 * @param localizer
	 *            the localizer to use.
	 * @return this problem's question
	 */
	public String getQuestion(Localizer localizer);
	
	/**
	 * Get the correct answer for this problem.
	 * 
	 * @return The correct answer
	 */
	public String getCorrectAnswer();
	
	/**
	 * Get the answer given by the user
	 * 
	 * @return the user's answer
	 */
	public String getUserAnswer();
	
	/**
	 * You can override this to do custom cloning. Return null if you don't want the question to be asked again after student fails to answer the question correctly
	 * 
	 * @param problem
	 * @return
	 */
	public default <P extends Problem> P cloneThis(P problem) {
		Cloner cloner = new Cloner();
		cloner.setNullTransient(true);
		P clone = cloner.deepClone(problem);
		return clone;
	}
}
