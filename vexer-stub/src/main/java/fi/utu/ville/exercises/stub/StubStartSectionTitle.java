package fi.utu.ville.exercises.stub;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

import fi.utu.ville.standardutils.StandardIcon;
import fi.utu.ville.standardutils.StandardUIFactory;

/**
 * A simple UI-element wrapping a title and a button for opening help text in a
 * pop-up.
 * 
 * @author Riku Haavisto
 * 
 */
class StubStartSectionTitle extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1819498224390930865L;

	/**
	 * Constructs a new {@link StubStartSectionTitle}
	 * 
	 * @param title
	 *            title to show
	 * @param helpText
	 *            text to show if help pop-up is opened
	 */
	public StubStartSectionTitle(String title, final String helpText) {
		setWidth("100%");
		setMargin(true);

		Label titleLabel = new Label(title);
		titleLabel.addStyleName("stub-section-title");
		titleLabel.setSizeUndefined();
		addComponent(titleLabel);

		Button showHelp = new Button(null);
		showHelp.setIcon(StandardIcon.INFO.getIcon());
		showHelp.setStyleName(BaseTheme.BUTTON_LINK);

		showHelp.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -9209700206964686363L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window helpWindow = new Window();
				helpWindow.addStyleName("opaque");

				helpWindow.setWidth("500px");
				helpWindow.center();
				helpWindow.setResizable(false);

				VerticalLayout content = new VerticalLayout();
				content.setMargin(true);
				content.addComponent(new Label("<br/>", ContentMode.HTML));
				content.addComponent(StandardUIFactory
						.getInformationPanel(helpText));

				helpWindow.setContent(content);

				UI.getCurrent().addWindow(helpWindow);

			}
		});

		addComponent(showHelp);
		setComponentAlignment(showHelp, Alignment.MIDDLE_RIGHT);

		setExpandRatio(showHelp, 1.0f);

	}
}
