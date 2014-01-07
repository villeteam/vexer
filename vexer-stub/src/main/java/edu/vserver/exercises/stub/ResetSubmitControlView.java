package edu.vserver.exercises.stub;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

import edu.vserver.exercises.helpers.StandardSubmissionType;
import edu.vserver.exercises.model.ExecutionState;
import edu.vserver.exercises.model.ExecutionStateChangeListener;
import edu.vserver.exercises.model.Executor;
import edu.vserver.standardutils.Localizer;

/**
 * A class wrapping to submit-and-reset functionalities of {@link Executor} and
 * looking approximately the same as the menu in real ViLLE does.
 * 
 * @author Riku Haavisto
 */
class ResetSubmitControlView extends HorizontalLayout implements
		Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5645968253156502722L;

	private final Executor<?, ?> exerciseExec;

	/* Buttons */
	private final Button resetButton, submitButton;

	public ResetSubmitControlView(Localizer localizer,
			Executor<?, ?> exerciseExec) {
		this.exerciseExec = exerciseExec;
		setMargin(true);
		setSpacing(true);

		submitButton = StubUiFactory.getSubmitButton(localizer);
		submitButton.addClickListener(this);
		submitButton.setId("stub.submitbutton");

		resetButton = StubUiFactory.getResetButton(localizer);
		resetButton.addClickListener(this);
		resetButton.setId("stub.resetbutton");

		setWidth("100%");
		addStyleName("exercisemenu-buttons-panel");

		HorizontalLayout middleButtons = new HorizontalLayout();

		middleButtons.addComponent(submitButton);

		middleButtons.addComponent(resetButton);

		addComponent(middleButtons);

		setComponentAlignment(middleButtons, Alignment.MIDDLE_CENTER);

		exerciseExec
				.registerExecutionStateChangeListener(new ExecutionStateChangeListener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = -7161676290058492324L;

					@Override
					public void actOnStateChange(ExecutionState newState) {
						resetButton.setEnabled(newState.isAllowReset());
						submitButton.setEnabled(newState.isAllowSubmit());
						ResetSubmitControlView.this.setEnabled(newState
								.isMenuEnabled());
						resetButton.setVisible(newState.isResetShown());
						submitButton.setVisible(newState.isSubmitShown());

					}

				});

	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == resetButton) {
			exerciseExec.askReset();
		} else if (event.getButton() == submitButton) {
			exerciseExec.askSubmit(StandardSubmissionType.NORMAL);
		}
	}

}
