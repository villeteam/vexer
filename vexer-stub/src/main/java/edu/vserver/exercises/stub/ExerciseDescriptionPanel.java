package edu.vserver.exercises.stub;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;
import edu.vserver.standardutils.StandardUIConstants;
import edu.vserver.standardutils.StandardUIFactory;

/**
 * Quick and dirty implementation of the general look of
 * ViLLE-ExerciseDescriptionPanel. Meant to provide an exercise-type developer
 * with a quite real-like view of what the new exercise-type should look like.
 * 
 * @author Riku Haavisto
 */
class ExerciseDescriptionPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6052200434268942950L;

	/* Buttons */
	private final Button expandButton;
	private final Panel descPanel;

	/* fields & labels */
	private final Label descField;

	private boolean descriptionExpanded = false;

	public ExerciseDescriptionPanel(Localizer localizer, String descText) {

		this.setWidth("100%");
		this.setSpacing(true);

		descPanel = StandardUIFactory.getBorderlessLightPanel();
		descPanel.setWidth("100%");
		descPanel.setHeight("100px");

		VerticalLayout descLayout = new VerticalLayout();

		descField = new Label();
		descField.setWidth("100%");
		descField.setContentMode(ContentMode.HTML);
		descField.setStyleName("text");
		descField.setValue(descText);
		descLayout.addComponent(descField);

		descPanel.setContent(descLayout);

		expandButton = new Button();

		expandButton.setId("stub.expandbutton");

		expandButton.addStyleName(BaseTheme.BUTTON_LINK);
		expandButton.setDescription(localizer
				.getUIText(StandardUIConstants.EXPAND_SHRINK_DESCRIPTION));
		expandButton.setIcon(StandardIcon.VERY_SMALL_ARROW_DOWN.getIcon());
		expandButton.setWidth("40px");
		expandButton.setHeight("14px");
		expandButton.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 9021072515972864218L;

			@Override
			public void buttonClick(ClickEvent event) {
				handlePanelExpand();
			}
		});

		addComponent(descPanel);
		setExpandRatio(descPanel, 1.0f);
		addComponent(expandButton);
		setComponentAlignment(expandButton, Alignment.BOTTOM_CENTER);
	}

	private void handlePanelExpand() {
		if (descriptionExpanded) {
			descPanel.setHeight("100px");
			expandButton.setIcon(StandardIcon.VERY_SMALL_ARROW_DOWN.getIcon());
		} else {
			descPanel.setHeight("400px");
			expandButton.setIcon(StandardIcon.VERY_SMALL_ARROW_UP.getIcon());
		}
		descriptionExpanded = !descriptionExpanded;

	}

}
