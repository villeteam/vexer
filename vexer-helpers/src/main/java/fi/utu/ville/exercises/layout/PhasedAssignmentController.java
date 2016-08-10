package fi.utu.ville.exercises.layout;

import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.utu.ville.exercises.helpers.StandardSubmissionType;
import fi.utu.ville.exercises.model.SubmissionType;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.UIConstants;

public class PhasedAssignmentController {
	
	private final PhasedExecutor exec;

	private int currentStep;
	private final Step[] steps;
	private boolean navigable;
	private boolean retriable;

	private final AssignmentProgressBar bar;
	private final PhasedAssignmentNavigation navigator;

	private final LayoutClickListener barListener;
	
	private final Localizer localizer;

	public PhasedAssignmentController(PhasedExecutor exec,
			int numberOfSteps, boolean showCorrect,
			boolean showPrev, Localizer localizer) {
		this.exec = exec;
		this.localizer = localizer;
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

	public void setRetriable(boolean enabled) {
		retriable = enabled;
		navigator.setCheckButtonEnabled(retriable || !steps[currentStep].answered);
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

	public int getCurrentStep() {
		return currentStep;
	}

	public boolean isAnswered(int index) {
		return index >= 0
				&& index < steps.length
				&& steps[index].answered;
	}

	public void check() {
		steps[currentStep].answered = true;
		steps[currentStep].correct = exec.isCorrect(currentStep);
		navigator.setCheckButtonEnabled(retriable);

		if (currentStep < steps.length - 1) {
			navigator.setNextButtonEnabled(true);
			navigator.focusNext();
		} else {
			final Window confirm = new Window(localizer
					.getUIText(UIConstants.SUBMIT_QUESTION));
			confirm.setModal(true);
			confirm.setWidth("600px");
			confirm.setHeight("330px");
			confirm.setCaption("");
			confirm.setStyleName("opaque");
			confirm.addStyleName("unclosable-window");
			confirm.setClosable(false);
			confirm.addStyleName("submit-window");

			VerticalLayout content = new VerticalLayout();
			content.setSizeFull();
			content.setMargin(true);
			Label question = new Label(localizer
					.getUIText(UIConstants.SUBMIT_QUESTION));
			question.setContentMode(ContentMode.HTML);
			question.addStyleName("big-text-white");
			question.setSizeUndefined();
			content.addComponent(question);
			content.setComponentAlignment(question,
					Alignment.TOP_CENTER);
			confirm.setContent(content);

			// Button holder (back to round, replay, next)
			HorizontalLayout buttons = new HorizontalLayout();
			buttons.addStyleName("submit-buttons-container");
			buttons.setWidth("100%");
			buttons.setSpacing(true);

			content.addComponent(buttons);
			content.setComponentAlignment(buttons,
					Alignment.TOP_CENTER);

			Button submit = StandardUIFactory
					.getRoundButton(Icon.SUBMIT);
			submit.setDescription(
					localizer.getUIText(UIConstants.SUBMIT));
			Button cancel = StandardUIFactory
					.getRoundButton(Icon.CLOSE);
			cancel.setDescription(
					localizer.getUIText(UIConstants.CLOSE));

			buttons.addComponents(submit, cancel);
			buttons.setComponentAlignment(submit,
					Alignment.MIDDLE_CENTER);
			buttons.setComponentAlignment(cancel,
					Alignment.MIDDLE_CENTER);

			submit.addClickListener(e -> {
				exec.askSubmit(StandardSubmissionType.NORMAL);
				confirm.close();
			});

			cancel.addClickListener(e -> confirm.close());

			UI.getCurrent().addWindow(confirm);
		}
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

	public void removeClickShortcuts() {
		navigator.removeClickShortcuts();
	}

	/***** Private implementations *****/
	private void addNavigatorLogic() {
		navigator.addPrevButtonListener(e -> prev());
		navigator.addCheckButtonListener(e -> {
			check();
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
			navigator.setCheckButtonEnabled(retriable || !steps[currentStep].answered);
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
		
		public abstract void askSubmit(SubmissionType type);
	}
}
