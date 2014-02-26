package edu.vserver.exercises.stub;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;
import edu.vserver.standardutils.StandardUIFactory;

/**
 * A header-bar to show over the testing views in stub. The header-bar provides
 * a navigation button back to stub's start-view and possibly an info-text
 * describing the current view.
 * 
 * @author Riku Haavisto
 * 
 */
public class StubViewHeaderBar extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718375416856425313L;

	/**
	 * Constructs a new {@link StubViewHeaderBar}.
	 * 
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param infoText
	 *            already localized info-text to show to user of the view
	 */
	public StubViewHeaderBar(Localizer localizer, String infoText) {

		HorizontalLayout actualLayout = new HorizontalLayout();
		actualLayout.setWidth("100%");
		actualLayout.addStyleName("stub-header-bar");
		actualLayout.setMargin(true);
		actualLayout.setSpacing(true);

		Button backToStubStartView = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.BACK_TO_STUB_START),
				StandardIcon.BACK_MEDIUM.getIcon());

		backToStubStartView.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7996549476059355446L;

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().setContent(new StubStartView());

			}
		});
		actualLayout.addComponent(backToStubStartView);

		Label infoLabel = StandardUIFactory.getInformationPanel(infoText);

		actualLayout.addComponent(infoLabel);
		actualLayout.setExpandRatio(infoLabel, 1.0f);

		this.addComponent(actualLayout);
		this.addComponent(new Label("<hr/>", ContentMode.HTML));

	}

}
