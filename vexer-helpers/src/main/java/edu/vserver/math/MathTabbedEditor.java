package edu.vserver.math;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import edu.vserver.exercises.math.essentials.level.LevelMathDataWrapper;
import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.EditorHelper;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.VilleContent;
import fi.utu.ville.exercises.model.VilleUI;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardIcon.IconVariant;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.StandardUIFactory.Border;
import fi.utu.ville.standardutils.StandardUIFactory.PanelStyle;
import fi.utu.ville.standardutils.UIConstants;

public abstract class MathTabbedEditor<E extends ExerciseData> extends
		VilleContent implements ClickListener, Editor<LevelMathDataWrapper<E>> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String GENERAL_PANEL_WIDTH = "400px";
	
	private HorizontalLayout contentLayout;
	private VerticalLayout settingsLayout; // for each exercise their own
	private VerticalLayout generalSettingsLayout; // name, descr etc.
	
	private TabSheet tabPanel = new TabSheet();
	
	private HorizontalLayout tabs;
	private Component buttonPanel;
	
	// private HorizontalSplitPanel content;
	
	private VerticalLayout e;
	private VerticalLayout n;
	private VerticalLayout h;
	
	private Button easy;
	private Button normal;
	private Button hard;
	
	private String activeLevel = "easy";
	
	private EditorHelper<LevelMathDataWrapper<E>> editorHelper;
	
	protected abstract MathTabbedEditorWrap<E> getEasyEditor();
	
	protected abstract MathTabbedEditorWrap<E> getNormalEditor();
	
	protected abstract MathTabbedEditorWrap<E> getHardEditor();
	
	protected abstract Executor<LevelMathDataWrapper<E>, ?> getExecutor();
	
	private Localizer localizer;
	
	public MathTabbedEditor() {
		super(null);
		setSizeFull();
		setMargin(false);
		
		contentLayout = new HorizontalLayout();
		contentLayout.setSizeFull();
		contentLayout.setMargin(false);
	}
	
	protected Localizer getLocalizer() {
		return localizer;
	}
	
	public void drawEditor() {
		contentLayout.removeAllComponents();
		
		// Green header bar
		HorizontalLayout headerBar = StandardUIFactory.getHeaderBarGreen();
		Label titleLabel = new Label(getEasyEditor().setTitleText());
		headerBar.addComponent(titleLabel);
		headerBar.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);
		this.addComponent(headerBar);
		
		Component generalControls = editorHelper
				.getControlbar(forSaving -> {
					if (validateEditor(getEasyEditor()) && validateEditor(getNormalEditor()) && validateEditor(getHardEditor())) {
						LevelMathDataWrapper<E> newData = new LevelMathDataWrapper<E>(
								getEasyEditor().getCurrData(),
								getNormalEditor().getCurrData(),
								getHardEditor().getCurrData());
						return newData;
					}
					return null; // returning null cancels save or test
				});
				
		buttonPanel = generalControls;
		
		this.addComponent(buttonPanel);
		
		// Add level selection
		drawTabs();
		tabs = StandardUIFactory.getButtonPanelForLevels(easy, normal, hard);
		
		// Add content area
		this.addComponent(contentLayout);
		setExpandRatio(contentLayout, 1);
		
		// ---- Generate content into different layouts
		// name, description
		drawGeneralSettingsPanel();
		// settings for exercise
		drawSettingsPanel();
		// easy, normal and hard containers
		drawSettings();
	}
	
	private void drawGeneralSettingsPanel() {
		generalSettingsLayout = StandardUIFactory
				.getVerticalGrayContentLayout(PanelStyle.DEFAULT);
		generalSettingsLayout.setSpacing(true);
		generalSettingsLayout.setWidth("100%");
		generalSettingsLayout.setHeight("100%");
		
		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setWidth(GENERAL_PANEL_WIDTH);
		wrapper.setHeight("100%");
		wrapper.addStyleName("background-color-gray");
		wrapper.addStyleName(Border.RIGHT.getValue());
		
		wrapper.addComponent(generalSettingsLayout);
		
		generalSettingsLayout.addComponent(getEditorHelper()
				.getInfoEditorView());
				
		contentLayout.addComponent(wrapper);
	}
	
	private void drawSettingsPanel() {
		settingsLayout = new VerticalLayout();
		settingsLayout.setWidth("100%");
		settingsLayout.setSpacing(true);
		settingsLayout.setMargin(true);
		
		contentLayout.addComponent(settingsLayout);
		contentLayout.setExpandRatio(settingsLayout, 1);
	}
	
	protected Layout getHelper() {
		String header = "<h1 class=\"color-h1\">Otsikko</h1>";
		String s = "<h2 class=\"basic-h2\">Alaotsikko</h2>"
				+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>"
				+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>"
				+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>"
				+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>"
				+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>"
				+ "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>"
				+ "<p> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget neque eget odio elementum congue vel a enim. Suspendisse eget dui lorem. Cras scelerisque enim ac purus auctor feugiat. Ut aliquam consequat sem pretium viverra. Nullam ac tellus mauris, sodales vestibulum risus. Integer vitae tellus sit amet quam convallis placerat in nec lectus. Vivamus facilisis fringilla ultrices. Nulla facilisi. Praesent posuere fringilla purus ultrices laoreet. Morbi ac ante dolor. </p>";
				
		return new MathEditorHelp(header, s, "Tavoitteet", "Ongelmakohdat",
				"Ideoita");
	}
	
	public void drawTabs() {
		easy = StandardUIFactory.getButton(localizer.getUIText(UIConstants.EASY), Icon.MATH_LEVEL_EASY);
		normal = StandardUIFactory.getButton(localizer.getUIText(UIConstants.MODERATE), Icon.MATH_LEVEL_NORMAL);
		hard = StandardUIFactory.getButton(localizer.getUIText(UIConstants.HARD), Icon.MATH_LEVEL_HARD);
		
		easy.addClickListener(this);
		normal.addClickListener(this);
		hard.addClickListener(this);
		
	}
	
	public void drawSettings() {
		e = new VerticalLayout();
		e.addComponent(getEasyEditor().drawEditorLayout());
		
		n = new VerticalLayout();
		n.addComponent(getNormalEditor().drawEditorLayout());
		
		h = new VerticalLayout();
		h.addComponent(getHardEditor().drawEditorLayout());
		
		tabPanel.addTab(e, Icon.MATH_LEVEL_EASY.getIcon().variant(IconVariant.GREEN) + " " + localizer.getUIText(UIConstants.EASY));
		tabPanel.addTab(n, Icon.MATH_LEVEL_NORMAL.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.MODERATE));
		tabPanel.addTab(h, Icon.MATH_LEVEL_HARD.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.HARD));
		
		tabPanel.setTabCaptionsAsHtml(true);
		tabPanel.addStyleName("course-editor-tabsheet");
		
		tabPanel.addSelectedTabChangeListener(event -> {
			TabSheet tabSheet = event.getTabSheet();
			int tabIndex = tabSheet.getTabPosition(tabSheet.getTab(tabSheet.getSelectedTab()));
			if (tabIndex == 0) {
				tabSheet.getTab(0)
						.setCaption(Icon.MATH_LEVEL_NORMAL.getIcon().variant(IconVariant.GREEN) + " " + localizer.getUIText(UIConstants.EASY));
				tabSheet.getTab(1).setCaption(Icon.MATH_LEVEL_NORMAL.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.MODERATE));
				tabSheet.getTab(2).setCaption(Icon.MATH_LEVEL_HARD.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.HARD));
				
			} else if (tabIndex == 1) {
				tabSheet.getTab(0).setCaption(Icon.MATH_LEVEL_EASY.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.EASY));
				tabSheet.getTab(1)
						.setCaption(Icon.MATH_LEVEL_NORMAL.getIcon().variant(IconVariant.GREEN) + " " + localizer.getUIText(UIConstants.MODERATE));
				tabSheet.getTab(2).setCaption(Icon.MATH_LEVEL_HARD.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.HARD));
			} else if (tabIndex == 2) {
				tabSheet.getTab(0).setCaption(Icon.MATH_LEVEL_EASY.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.EASY));
				tabSheet.getTab(1).setCaption(Icon.MATH_LEVEL_NORMAL.getIcon().variant(IconVariant.BLACK) + " " + localizer.getUIText(UIConstants.MODERATE));
				tabSheet.getTab(2)
						.setCaption(Icon.MATH_LEVEL_NORMAL.getIcon().variant(IconVariant.GREEN) + " " + localizer.getUIText(UIConstants.HARD));
			}
		});
		
		tabPanel.setWidthUndefined();
		
		// Easy editor as a default option
		settingsLayout.removeAllComponents();
		settingsLayout.addComponent(tabPanel);
		settingsLayout.addStyleName("course-editor-tabsheet-holder");
		//		settingsLayout.addComponent(tabs);
		//		settingsLayout.addComponent(e);
		this.easy.addStyleName("math-tabbed-button-selected");
		
	}
	
	private void showDefaultValidationError() {
		Notification.show(localizer.getUIText(UIConstants.EXERCISE_NOT_READY_FOR_SAVING_OR_TESTING));
	}
	
	private boolean validateEditor(MathTabbedEditorWrap<E> editor) {
		if (editor == null) {
			return false;
		}
		
		if (editor.validateData() == null) {
			return false;
		} else if (editor.validateData() == false) {
			showDefaultValidationError();
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (!validateEditor(getCurrentEditor())) {
			return;
		}
		if (event.getButton() == easy && !activeLevel.equals("easy")) {
			
			activeLevel = "easy";
			easy.addStyleName("math-tabbed-button-selected");
			settingsLayout.replaceComponent(settingsLayout.getComponent(1), e);
			
			normal.setStyleName("");
			hard.setStyleName("");
		}
		
		if (event.getButton() == normal && !activeLevel.equals("normal")) {
			activeLevel = "normal";
			normal.addStyleName("math-tabbed-button-selected");
			settingsLayout.replaceComponent(settingsLayout.getComponent(1), n);
			
			easy.setStyleName("");
			hard.setStyleName("");
		}
		
		if (event.getButton() == hard && !activeLevel.equals("hard")) {
			activeLevel = "hard";
			hard.addStyleName("math-tabbed-button-selected");
			settingsLayout.replaceComponent(settingsLayout.getComponent(1), h);
			
			easy.setStyleName("");
			normal.setStyleName("");
		}
	}
	
	protected MathTabbedEditorWrap<E> getEditorForLevel(String level) {
		switch (level) {
		case "easy":
			return getEasyEditor();
		case "normal":
			return getNormalEditor();
		case "hard":
			return getHardEditor();
		}
		return null;
	}
	
	private MathTabbedEditorWrap<E> getCurrentEditor() {
		return getEditorForLevel(activeLevel);
	}
	
	public Window getEditorWindow() {
		return this.findAncestor(Window.class);
	}
	
	@Override
	public VilleContent getView() {
		return this;
	}
	
	protected abstract void typeInitialize(LevelMathDataWrapper<E> oldData);
	
	@Override
	public void initialize(VilleUI ui, Localizer localizer,
			LevelMathDataWrapper<E> oldData,
			EditorHelper<LevelMathDataWrapper<E>> exerPanel) {
			
		this.editorHelper = exerPanel;
		this.localizer = localizer;
		typeInitialize(oldData);
		init(ui);
	}
	
	protected EditorHelper<LevelMathDataWrapper<E>> getEditorHelper() {
		return editorHelper;
	}
	
}
