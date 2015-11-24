package edu.vserver.exercises.math.essentials.layout;

import java.util.ArrayList;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.standardutils.Localizer;

public abstract class AbstractMathState<E extends ExerciseData, P extends Problem>
		implements MathExerciseState<P> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5528193973308381408L;
	private int points;
	private final int rounds;
	private int currentProblem = -1;
	private final ArrayList<P> problems = new ArrayList<P>();
	protected final Localizer localizer;
	
	public AbstractMathState(E data, Localizer localizer) {
		this.localizer = localizer;
		this.rounds = loadDataAndGetAmount(data);
		initProblems();
	}
	
	/**
	 * Saves the specific ExerciseData for this exercise type for later use.
	 * 
	 * @param data
	 *            The data used to generate exercises.
	 * @return The number of exercises what will be generated.
	 */
	abstract protected int loadDataAndGetAmount(E data);
	
	/**
	 * Creates a single problem.
	 * 
	 * @return a single problem that conforms to the exercise data loaded in <b>loadDataAndGetAmount </b>
	 */
	abstract protected P createProblem();
	
	protected Localizer getLocalizer() {
		return localizer;
	}
	
	@Override
	public double getCorrectness() {
		double points = 0.0;
		points = (double) getPoints() / (double) rounds;
		return points;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints() {
		points++;
	}
	
	@Override
	public int getProblemCount() {
		return rounds;
	}
	
	@Override
	public P getProblem(int i) {
		return problems.get(i);
	}
	
	@Override
	public P getCurrentProblem() {
		return problems.get(currentProblem);
	}
	
	@Override
	public P nextProblem() {
		currentProblem++;
		return getCurrentProblem();
	}
	
	private void initProblems() {
		problems.clear();
		for (int i = 0; i < rounds; i++) {
			problems.add(createProblem());
		}
	}
	
	@Override
	public void reset() {
		points = 0;
		currentProblem = -1;
		initProblems();
		
	}
	
	@Override
	public boolean tryAnswer(AbstractMathAnswer answer) {
		if (getCurrentProblem().tryAnswer(answer)) {
			setPoints();
		}
		return getCurrentProblem().tryAnswer(answer);
	}
	
	@Override
	public boolean hasNextProblem() {
		return currentProblem < problems.size() - 1;
	}
	
}
