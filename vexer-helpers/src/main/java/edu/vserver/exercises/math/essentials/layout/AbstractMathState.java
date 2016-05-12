package edu.vserver.exercises.math.essentials.layout;

import java.util.ArrayList;
import java.util.Random;

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
	private final boolean[] problemsReplaced;
	protected final Localizer localizer;
	private final int AMOUNT_OF_TRIES = 6;
	
	public AbstractMathState(E data, Localizer localizer) {
		this.localizer = localizer;
		this.rounds = loadDataAndGetAmount(data);
		problemsReplaced = new boolean[rounds];
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
			P prob = createUnequalProblem(AMOUNT_OF_TRIES);
			if (prob != null) {
				problems.add(prob);
				problemsReplaced[i] = false;
			} else {
				break;
			}
		}
	}
	
	private P createUnequalProblem(int amountOfTries) {
		try {
			if (amountOfTries < 1) {
				return createProblem();
			}
			
			P problemProposition = createProblem();
			for (P p : problems) {
				if (p.getQuestion(localizer).equals(problemProposition.getQuestion(localizer))) {
					//				System.out.println("Found an equal problem: " + p.getQuestion(localizer));
					return createUnequalProblem(--amountOfTries);
				}
			}
			//		System.out.println("Not equal problems: " + problemProposition.getQuestion(localizer));
			if (problemProposition == null) {
				System.out.println("problem proposition = null");
			}
			return problemProposition;
		} catch (Exception e) {
			return null;
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
	public boolean tryAnswer(AbstractMathAnswer answer, boolean isExam) {
		if (getCurrentProblem().tryAnswer(answer)) {
			setPoints();
		} else {
			if (!isExam) {
				//				System.out.println("CurrentP: " + problems.get(currentProblem).getQuestion(localizer));
				//				System.out.println("NextP: " + problems.get(currentProblem + 1).getQuestion(localizer));
				if (problems.size() - currentProblem >= 3) {
					//					Random rand = new Random();
					int randomIndex = getRandomUnusedInt();
					if (problems.size() > randomIndex || randomIndex < 1) {
						P clone = getCurrentProblem().cloneThis(getCurrentProblem());
						problems.set(randomIndex, clone);
					} else {
						//						System.out.println("Faulty random number generated: " + randomIndex);
					}
				}
			}
		}
		return getCurrentProblem().tryAnswer(answer);
	}
	
	@Override
	public boolean hasNextProblem() {
		return currentProblem < problems.size() - 1;
	}
	
	private int getRandomUnusedInt() {
		Random rand = new Random();
		int randomIndex = 0;
		for (int i = 0; i < 4; i++) {
			randomIndex = rand.nextInt((problems.size() - 1) - (currentProblem + 2) + 1) + currentProblem + 2;
			if (problemsReplaced[randomIndex] == false) {
				//				System.out.println("problem inserted to: " + randomIndex);
				problemsReplaced[randomIndex] = true;
				return randomIndex;
			} else {
				//				System.out.println("already modified: " + randomIndex);
			}
		}
		
		return randomIndex;
	}
	
}
