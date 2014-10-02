package fi.utu.ville.exercises.stub;

import com.vaadin.ui.Button;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;

/**
 * A factory-class for creating certain UI-components needed by
 * ViLLE-exercise-stub.
 * 
 * @author Riku Haavisto
 * 
 */
class StubUiFactory {

	public static Button getSubmitButton(Localizer localizer) {
		Button submitButton = StandardUIFactory.getButton(
				localizer.getUIText(StandardUIConstants.SUBMIT), Icon.SUBMIT);
		submitButton.setDescription(localizer
				.getUIText(StandardUIConstants.SUBMIT));
		return submitButton;
	}

	public static Button getResetButton(Localizer localizer) {
		Button resetButton = StandardUIFactory.getButton(
				localizer.getUIText(StandardUIConstants.RESET), Icon.RESET);

		resetButton.setDescription(localizer
				.getUIText(StandardUIConstants.RESET));
		return resetButton;
	}
}
