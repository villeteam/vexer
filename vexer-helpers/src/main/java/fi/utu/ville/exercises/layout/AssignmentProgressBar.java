package fi.utu.ville.exercises.layout;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import fi.utu.ville.exercises.layout.PhasedAssignmentController.Step;

/**
 * Class for displaying a progress bar in assignment view.
 * 
 * @author ertaka
 * 
 */
public class AssignmentProgressBar extends CssLayout {
	
	private static final long serialVersionUID = -599885191285347355L;
	
	protected StepComponent[] steps;
	
	private boolean isShowCorrect;
	
	/**
	 * Creates a new progress bar
	 */
	public AssignmentProgressBar(Step[] steps, boolean isShowCorrect) {
		if (steps.length < 1) {
			throw new IllegalArgumentException("Minimum number of steps is 1.");
		}
		this.isShowCorrect = isShowCorrect;
		initSteps(steps);
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
		addStyleName("assignment-progress-bar");
		if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
			addStyleName("mobile");
		}
		for (StepComponent e : steps) {
			addComponent(e);
		}
		
	}
	
	/**
	 * Initializes the steps used in the bar
	 */
	private void initSteps(Step[] steps) {
		this.steps = new StepComponent[steps.length];
		for (int i = 0; i < steps.length; i++) {
			this.steps[i] = new StepComponent(steps[i], i);
		}
	}
	
	public void setCurrent(int step) {
		for (int i = 0; i < steps.length; i++) {
			steps[i].refresh(i == step);
		}
	}
	
	public int getClickedIndex(LayoutClickEvent e) {
		for (int i = 0; i < steps.length; i++) {
			if (e.getChildComponent() != null
					&& e.getChildComponent().equals(steps[i])) {
				return i;
			}
		}
		return -1;
	}
	
	protected void setNumbersVisible(boolean visible) {
		for (StepComponent c : steps) {
			c.setIndexLabelVisible(visible);
		}
	}
	
	public class StepComponent extends CssLayout {
		private final Step step;
		private final Label indexLabel;
		
		StepComponent(Step step, int index) {
			this.step = step;
			indexLabel = new Label(index + 1 + "");
			indexLabel.setSizeUndefined();
			addComponent(indexLabel);
			setId("step-component-" + index);
		}
		
		private void setIndexLabelVisible(boolean visible) {
			indexLabel.setVisible(visible);
		}
		
		public Step getStep() {
			return step;
		}
		
		void refresh(boolean current) {
			if (current) {
				style(StepMode.CURRENT);
			} else if (step.answered) {
				if (!isShowCorrect) {
					style(StepMode.ANSWERED);
				} else if (step.correct) {
					style(StepMode.CORRECT);
				} else {
					style(StepMode.INCORRECT);
				}
			} else {
				style(StepMode.DISABLED);
			}
		}
		
		private void style(StepMode style) {
			setStyleName(style.getStylename());
		}
	}
	
	/* Mode of the step, used mainly for styling */
	public enum StepMode {
		DISABLED("step-disabled"),
		CORRECT("step-correct"),
		INCORRECT("step-incorrect"),
		CURRENT("step-current"),
		ANSWERED("step-answered");
		
		private final String stylename;
		
		private StepMode(String stylename) {
			this.stylename = stylename;
		}
		
		public String getStylename() {
			return stylename;
		}
	}
	
}
