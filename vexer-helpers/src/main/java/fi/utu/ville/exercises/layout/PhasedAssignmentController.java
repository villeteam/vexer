package fi.utu.ville.exercises.layout;

import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;

import fi.utu.ville.standardutils.Localizer;

public class PhasedAssignmentController {
	
	private final PhasedExecutor exec;
	
	private int currentStep;
	private final Step[] steps;
	private boolean navigable;
	
	private final AssignmentProgressBar bar;
	private final PhasedAssignmentNavigation navigator;
	
	private final LayoutClickListener barListener;
	
	public PhasedAssignmentController(PhasedExecutor exec,
			int numberOfSteps, boolean showCorrect,
			boolean showPrev, Localizer localizer) {
		this.exec = exec;
		steps = new Step[numberOfSteps];
		
		for (int i = 0; i < steps.length; i++) {
			steps[i] = new Step();
		}
		
		bar = new AssignmentProgressBar(steps, showCorrect);
		navigator = new PhasedAssignmentNavigation(localizer, showPrev);
		
		currentStep = 0;
		bar.setCurrent(currentStep);
		
		barListener = e -> stepTo(bar.getClickedIndex(e));
		
		navigator.setNextButtonEnabled(false);
		
		addNavigatorLogic();
	}
	
	//***** Public API *****//
	
	public void setNavigable(boolean enabled) {
		navigable = enabled;
		
		navigator.setPrevButtonEnabled(navigable && currentStep > 0);
		navigator.setPrevButtonVisible(navigable);
		
		navigator.setNextButtonEnabled(navigable && currentStep < steps.length - 1);
	}
	
	public void setClickNavigable(boolean enabled) {
		if (enabled) {
			bar.addLayoutClickListener(barListener);
			bar.addStyleName("clickable");
		} else {
			bar.removeLayoutClickListener(barListener);
			bar.removeStyleName("clickable");
		}
	}
	
	public void showNumbers(boolean enabled) {
		bar.setNumbersVisible(enabled);
	}
	
	public void reset() {
		for (int i = 0; i < steps.length; i++) {
			steps[i].answered = false;
			steps[i].correct = false;
		}
		stepTo(0);
	}
	
	public Component getProgressBar() {
		return bar;
	}
	
	public Component getNavigator() {
		return navigator;
	}
	
	public void prev() {
		stepTo(currentStep - 1);
	}
	
	public void check() {
		steps[currentStep].answered = true;
		steps[currentStep].correct = exec.isCorrect(currentStep);
		navigator.setNextButtonEnabled(currentStep < steps.length - 1);
		navigator.focusNext();
	}
	
	public void next() {
		if (steps[currentStep].answered == false && !navigable) {
			steps[currentStep].answered = true;
			steps[currentStep].correct = exec.isCorrect(currentStep);
		}
		stepTo(currentStep + 1);
	}
	
	public void setCheckButtonVisible(boolean visible) {
		navigator.setCheckButtonVisible(visible);
	}
	
	/***** Private implementations *****/
	private void addNavigatorLogic() {
		navigator.addPrevButtonListener(e -> prev());
		navigator.addCheckButtonListener(e -> {
			check();
			navigator.focusNext();
		});
		navigator.addNextButtonListener(e -> next());
	}
	
	private void stepTo(int step) {
		if (step >= 0 && step < steps.length) {
			currentStep = step;
			bar.setCurrent(currentStep);
			navigator.setNextButtonEnabled((currentStep < steps.length - 1)
					&& (navigable || steps[currentStep].answered));
			navigator.setPrevButtonEnabled(currentStep > 0);
			exec.drawProblem(currentStep);
		}
	}
	
	public static class Step {
		public boolean answered;
		public boolean correct;
	}
	
	public interface PhasedExecutor {
		public void drawProblem(int index);
		
		public boolean isCorrect(int index);
	}
}
