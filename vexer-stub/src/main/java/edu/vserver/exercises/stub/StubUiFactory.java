package edu.vserver.exercises.stub;

import com.vaadin.ui.Button;

import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;
import edu.vserver.standardutils.StandardUIConstants;

/**
 * A factory-class for creating certain UI-components needed by
 * ViLLE-exercise-stub.
 * 
 * @author Riku Haavisto
 * 
 */
class StubUiFactory {

	public static Button getSubmitButton(Localizer localizer) {
		Button submitButton = new Button();
		submitButton.setHeight("65px");
		submitButton.addStyleName("borderless icon-on-top");
		submitButton
				.setCaption(localizer.getUIText(StandardUIConstants.SUBMIT));
		submitButton.setIcon(StandardIcon.SUBMIT_ICON_MEDIUM.getIcon());
		submitButton.setDescription(localizer
				.getUIText(StandardUIConstants.SUBMIT));
		return submitButton;
	}

	public static Button getResetButton(Localizer localizer) {
		Button resetButton = new Button();
		resetButton.setHeight("65px");
		resetButton.addStyleName("borderless icon-on-top");
		resetButton.setCaption(localizer.getUIText(StandardUIConstants.RESET));
		resetButton.setIcon(StandardIcon.RESET_ICON_MEDIUM.getIcon());
		resetButton.setDescription(localizer
				.getUIText(StandardUIConstants.RESET));
		return resetButton;
	}

}
