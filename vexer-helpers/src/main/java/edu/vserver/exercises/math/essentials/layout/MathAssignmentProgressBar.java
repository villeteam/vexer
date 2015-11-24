package edu.vserver.exercises.math.essentials.layout;

import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

/**
 * Class for displaying a progress bar in assignment view.
 * 
 * @author ertaka
 * 
 */
public class MathAssignmentProgressBar extends HorizontalLayout {
	
	private static final long serialVersionUID = -599885191285347355L;
	
	/* Number of steps */
	private final int numberOfSteps;
	
	/* Current step */
	private int currentStep;
	
	/* Mode of the step */
	public enum StepMode {
		DISABLED,
		CURRENT,
		CORRECT,
		INCORRECT;
	}
	
	private StepMode[] steps;
	
	/* Graphical representation of steps */
	private Embedded[] stepIcons;
	
	/* Folder for step icons */
	private static final String ICONS_FOLDER = "../ville-standardutils/images/prog_bar/";
	
	/* Maximum number of steps */
	private static final int MAX_NUMBER_OF_STEPS = 20;
	
	private boolean isShowCorrect;
	
	/**
	 * Creates a new progress bar
	 * 
	 * @param numberOfSteps
	 *            the total number of steps, between 1 and 20
	 */
	public MathAssignmentProgressBar(int numberOfSteps, boolean isShowCorrect) {
		if (numberOfSteps < 1 || numberOfSteps > MAX_NUMBER_OF_STEPS) {
			throw new IllegalArgumentException(
					"The allowed number of steps is between 1 and "
							+ MAX_NUMBER_OF_STEPS + ". " + numberOfSteps
							+ " is not allowed");
		}
		this.numberOfSteps = numberOfSteps;
		this.isShowCorrect = isShowCorrect;
		currentStep = 0;
		initSteps();
		doLayout();
	}
	
	/**
	 * <p>
	 * (re)draws this component and all subcomponents;
	 * <p>
	 * Note, that there usually is no need to call this method manually.
	 */
	public void doLayout() {
		removeAllComponents();
		setHeight("30px");
		setStyleName("math-progress-margin");
		setMargin(false);
		setSpacing(true);
		for (Embedded e : stepIcons) {
			addComponent(e);
			setComponentAlignment(e, Alignment.MIDDLE_LEFT);
		}
		
	}
	
	/**
	 * Initializes the steps used in the bar
	 */
	private void initSteps() {
		steps = new StepMode[numberOfSteps];
		for (int i = 1; i < steps.length; i++) {
			steps[i] = StepMode.DISABLED;
		}
		steps[0] = StepMode.CURRENT;
		
		stepIcons = new Embedded[numberOfSteps];
		for (int i = 0; i < stepIcons.length; i++) {
			stepIcons[i] = new Embedded(null, getStepIcon(steps[i], i + 1));
			stepIcons[i].setEnabled(false);
			stepIcons[i].setWidth("10px");
			stepIcons[i].setHeight("10px");
		}
		stepIcons[0].setEnabled(true); // enable first
	}
	
	/**
	 * Updates the step icons to match the step states.
	 */
	private void updateStepIcons() {
		for (int i = 0; i < stepIcons.length; i++) {
			stepIcons[i].setSource(getStepIcon(steps[i], i + 1));
		}
	}
	
	/**
	 * Returns an icon for the step
	 * 
	 * @param mode
	 *            the mode of the step
	 * @param number
	 *            the number displayed in icon, starting from 1
	 * @return an icon representing a step
	 */
	private ThemeResource getStepIcon(StepMode mode, int number) {
		
		if (isShowCorrect) {
			switch (mode) {
			case DISABLED:
				return new ThemeResource(ICONS_FOLDER + number + ".png");
			case CURRENT:
				return new ThemeResource(ICONS_FOLDER + number + ".png");
			case CORRECT:
				return new ThemeResource(ICONS_FOLDER + number + "c.png");
			case INCORRECT:
				return new ThemeResource(ICONS_FOLDER + number + "w.png");
			default:
				// not accessible
				return null;
				
			}
		} else {
			switch (mode) {
			case DISABLED:
				return new ThemeResource(ICONS_FOLDER + number + ".png");
			case CURRENT:
				return new ThemeResource(ICONS_FOLDER + number + ".png");
			case CORRECT:
				return new ThemeResource(ICONS_FOLDER + number + ".png");
			case INCORRECT:
				return new ThemeResource(ICONS_FOLDER + number + ".png");
			default:
				// not accessible
				return null;
				
			}
		}
	}
	
	/**
	 * Animates the given icon by flashing it a few times.
	 * 
	 * @param icon
	 *            the icon animated
	 */
	private void animateIcon(Embedded icon) {
		AnimatorProxy animatorProxy = new AnimatorProxy();
		addComponent(animatorProxy);
		animatorProxy.animate(icon, AnimType.FADE_IN).setDuration(500);
		animatorProxy.animate(icon, AnimType.FADE_OUT).setDuration(500)
				.setDelay(500);
		animatorProxy.animate(icon, AnimType.FADE_IN).setDuration(500)
				.setDelay(1000);
	}
	
	/**
	 * <p>
	 * Advances to next step; sets the current step's icon to correct or incorrect based on the students answer, and enables the next step (if any left) in the
	 * bar.
	 * <p>
	 * If the bar is already at the last step, the state of that step is changed.
	 * 
	 * @param currentStepCorrect
	 *            was the previous answer correct or not
	 */
	public void nextStep(boolean currentStepCorrect) {
		steps[currentStep] = currentStepCorrect ? StepMode.CORRECT
				: StepMode.INCORRECT;
		if (currentStep < numberOfSteps - 1) {
			currentStep++;
			steps[currentStep] = StepMode.CURRENT;
			stepIcons[currentStep].setEnabled(true);
			updateStepIcons();
			animateIcon(stepIcons[currentStep - 1]);
		} else {
			updateStepIcons();
			animateIcon(stepIcons[currentStep]);
		}
		
	}
	
	/**
	 * Returns to previous step; the current step's state is set to unanswered. If the bar is already at the first step, the method does nothing.
	 */
	public void previousStep() {
		if (currentStep > 0) {
			steps[currentStep] = StepMode.DISABLED;
			stepIcons[currentStep].setEnabled(false);
			currentStep--;
			steps[currentStep] = StepMode.CURRENT;
			updateStepIcons();
		}
	}
	
	/**
	 * Resets the state of the progress bar.
	 */
	public void reset() {
		currentStep = 0;
		initSteps();
		doLayout();
	}
	
	/**
	 * Returns true, if the current step is the final step on this bar.
	 * 
	 * @return
	 */
	public boolean isLastStep() {
		return currentStep == numberOfSteps - 1;
	}
}
