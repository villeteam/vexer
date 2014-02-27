package fi.utu.ville.exercises.stub;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

import fi.utu.ville.exercises.helpers.StandardSubmissionType;
import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.standardutils.Localizer;

/**
 * A class allowing user to call submit and reset of {@link Executor} through
 * GUI. The look of the view is approximately the same as the what the menu
 * looks in real ViLLE.
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

	/**
	 * Constructs a new {@link ResetSubmitControlView} for controlling the given
	 * {@link Executor}.
	 * 
	 * @param localizer
	 *            {@link Localizer} to localize the GUI
	 * @param exerciseExec
	 *            {@link Executor} to control
	 */
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
