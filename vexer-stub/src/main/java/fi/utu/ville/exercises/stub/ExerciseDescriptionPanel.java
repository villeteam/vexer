package fi.utu.ville.exercises.stub;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;

public class ExerciseDescriptionPanel extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final boolean showWindowButton;

	private final String description;

	private enum ContentHeight {
		MINIMIZED("0px"), DEFAULT("150px"), MAXIMIZED("400px");

		private final String height;

		ContentHeight(String height) {
			this.height = height;
		}

		public String getHeight() {
			return height;
		}
	}

	private ContentHeight contentHeight = ContentHeight.DEFAULT;

	private HorizontalLayout content;

	private Panel infoPanel;

	private Localizer localizer;

	public ExerciseDescriptionPanel(Localizer localizer, String description) {
		this.showWindowButton = false;
		this.description = description;
		this.localizer = localizer;
		doLayout();
	}

	public void doLayout() {
		this.removeAllComponents();
		addTitleBar();
		addContentLayout();
	}

	/**
	 * Minimize the description panel to maximize exercise area. Used in math
	 * driller.
	 */
	public void minimizeDescriptionPanl() {
		contentHeight = ContentHeight.MINIMIZED;
		setContentSize();

	}

	private void addTitleBar() {
		HorizontalLayout titleBar = StandardUIFactory.getHeaderBarBlue();
		titleBar.setWidth("100%");
		Label descLabel = new Label(
				localizer.getUIText(StandardUIConstants.DESCRIPTION));
		titleBar.addComponent(descLabel);
		titleBar.setComponentAlignment(descLabel, Alignment.MIDDLE_LEFT);

		HorizontalLayout controlLayout = new HorizontalLayout();
		controlLayout.setSpacing(true);

		Button minimizeButton = StandardUIFactory
				.getWindowControlButton(Icon.WINDOW_MINIMIZE);
		minimizeButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (contentHeight == ContentHeight.MAXIMIZED) {
					contentHeight = ContentHeight.DEFAULT;
					setContentSize();
				} else if (contentHeight == ContentHeight.DEFAULT) {
					contentHeight = ContentHeight.MINIMIZED;
					setContentSize();
				}
			}

		});
		Button maximizeButton = StandardUIFactory
				.getWindowControlButton(Icon.WINDOW_MAXIMIZE);

		maximizeButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (contentHeight == ContentHeight.MINIMIZED) {
					contentHeight = ContentHeight.DEFAULT;
					setContentSize();
				} else if (contentHeight == ContentHeight.DEFAULT) {
					contentHeight = ContentHeight.MAXIMIZED;
					setContentSize();
				}
			}

		});

		controlLayout.addComponent(minimizeButton);
		controlLayout.addComponent(maximizeButton);

		titleBar.addComponent(controlLayout);
		titleBar.setComponentAlignment(controlLayout, Alignment.MIDDLE_RIGHT);

		addComponent(titleBar);
	}

	private void addContentLayout() {
		content = new HorizontalLayout();
		content.setWidth("100%");
		setContentSize();

		Label descLabel = new Label(description, ContentMode.HTML);
		Panel descPanel = StandardUIFactory.getExpandablePanel();
		descPanel.setHeight("100%");
		descPanel.setContent(new VerticalLayout());
		((VerticalLayout) descPanel.getContent()).addComponent(descLabel);

		content.addComponent(descPanel);
		content.setExpandRatio(descPanel, 1);

		infoPanel = StandardUIFactory.getExpandablePanel();
		infoPanel.setWidth("400px");
		infoPanel.setHeight("100%");

		content.addComponent(infoPanel);

		addComponent(content);
	}

	public void addComponentToInfoPanel(Component component) {
		((VerticalLayout) infoPanel.getContent()).addComponent(component);
	}

	private void setContentSize() {
		content.setHeight(contentHeight.getHeight());
	}

	public void clearInfoPanel() {
		((VerticalLayout) infoPanel.getContent()).removeAllComponents();
	}

}
