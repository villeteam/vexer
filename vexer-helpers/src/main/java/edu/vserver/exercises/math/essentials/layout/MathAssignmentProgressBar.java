package edu.vserver.exercises.math.essentials.layout;

import fi.utu.ville.exercises.layout.AssignmentProgressBar;
import fi.utu.ville.exercises.layout.PhasedAssignmentController.Step;

/**
 * Light logic wrapper around {@link AssignmentProgressBar} to keep compatibility with MathLayout
 * 
 * @author nipehe
 *
 */
public class MathAssignmentProgressBar extends AssignmentProgressBar {
	
	private int currentStep;
	
	public MathAssignmentProgressBar(int steps, boolean isShowCorrect) {
		super(getSteps(steps), isShowCorrect);
		currentStep = 0;
		setCurrent(currentStep);
	}
	
	private static Step[] getSteps(int count) {
		Step[] steps = new Step[count];
		for (int i = 0; i < count; i++) {
			steps[i] = new Step();
		}
		return steps;
	}
	
	public void setCurrentStepCorrect(boolean correct) {
		steps[currentStep].getStep().answered = true;
		steps[currentStep].getStep().correct = correct;
	}
	
	public void nextStep() {
		setCurrent(++currentStep);
	}
	
	public void previousStep() {
		setCurrent(--currentStep);
	}
	
	public void nextStep(boolean currentStepCorrect) {
		setCurrentStepCorrect(currentStepCorrect);
		nextStep();
	}
	
	public void reset() {
		currentStep = 0;
		for (int i = 0; i < steps.length; i++) {
			steps[i].getStep().answered = false;
			steps[i].getStep().correct = false;
		}
		setCurrent(currentStep);
	}
	
}
