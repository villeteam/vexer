package edu.vserver.exercises.math.essentials.layout;

import java.io.Serializable;

public interface MathExerciseState<P extends Problem> extends Serializable {
	
	public double getCorrectness();
	
	public int getProblemCount();
	
	public P getCurrentProblem();
	
	public P nextProblem();
	
	public P getProblem(int i);
	
	public void reset();
	
	public boolean tryAnswer(AbstractMathAnswer answer);
	
	public boolean hasNextProblem();
}
