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

public class StubViewHeaderBar extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718375416856425313L;

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
