package fi.utu.ville.exercises.stub;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.AlignmentInfo.Bits;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.Util;
import fi.utu.ville.standardutils.ui.DynamicStyles.DynamicStylesEditor;
import fi.utu.ville.standardutils.ui.DynamicStyles.StyleSettings;
import fi.utu.ville.standardutils.ui.DynamicStyles.StyleSettings.BorderStyle;

/**
 * Stub-implementor for {@link DynamicStylesEditor}.
 * 
 * @author Erkki Kaila, Riku Haavisto
 * 
 */
class StubStyleSettingsEditor extends VerticalLayout implements
		DynamicStylesEditor {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -831660054425691649L;
	
	/* main & state */
	private final Localizer main;
	
	/* colors */
	private ColorPicker background;
	private CheckBox transparentBG;
	private ColorPicker foreground;
	
	/* borders */
	private NativeSelect borderStyle;
	private TextField borderWidth;
	private ColorPicker borderColor;
	private TextField rounded;
	
	/* fonts */
	private NativeSelect fontFamily;
	private NativeSelect fontSize;
	private CheckBox fontBolded;
	private CheckBox fontItalic;
	private final static String[] FONTS = { "sans-serif", "serif", "monospace", "cursive", "fantasy" };
	private final static String[] FONT_SIZES = { "100%", "75%", "120%", "8px", "10px", "12px", "13px", "14px", "16px", "18px", "20px", "22px", "24px", "26px", "30px", "36px", "40px", "56px", "72px", "96px" };
	
	/* margin & padding */
	private CheckBox marginSymmetric;
	private TextField marginTop;
	private TextField marginBottom;
	private TextField marginLeft;
	private TextField marginRight;
	private CheckBox paddingSymmetric;
	private TextField paddingTop;
	private TextField paddingBottom;
	private TextField paddingLeft;
	private TextField paddingRight;
	
	/**
	 * Create new style settings dialog for creating new style settings
	 * 
	 * @param main
	 *            ViLLE main server object
	 */
	public StubStyleSettingsEditor(Localizer main) {
		this.main = main;
		// Loads "default" styles and inits the UI
		loadStyles(new StubStyleSettings());
	}
	
	@Override
	public Component getStylesEditor() {
		return this;
	}
	
	@Override
	public void loadStyles(StyleSettings toLoad) {
		removeAllComponents();
		addColorPickers(toLoad);
		addBorderSettings(toLoad);
		addFontSettings(toLoad);
		addMarginSettings(toLoad);
		addPaddingSettings(toLoad);
	}
	
	/**
	 * Adds the color picker section to dialog
	 */
	private void addColorPickers(StyleSettings styles) {
		final Panel p = new Panel(main.getUIText(StandardUIConstants.COLORS));
		final Layout pL = new VerticalLayout();
		p.setContent(pL);
		p.setStyleName("light");
		
		final Color initColor = styles.getBackgroundColor().equals(
				"transparent") ? Color.WHITE : Util.colorFromHexString(styles
						.getBackgroundColor());
		background = new ColorPicker(
				main.getUIText(StandardUIConstants.BACKGROUND), initColor);
		// this is probably redundant (and not supported) since 7
		// background.setButtonCaption(main.getUIText(UIConstants.BACKGROUND));
		
		transparentBG = new CheckBox(
				main.getUIText(StandardUIConstants.TRANSPARENT));
		transparentBG.setImmediate(true);
		transparentBG.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -3985282405650622452L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				background.setEnabled(!transparentBG.getValue());
			}
		});
		transparentBG.setValue(styles.getBackgroundColor()
				.equals("transparent"));
				
		foreground = new ColorPicker(main.getUIText(StandardUIConstants.TEXT),
				Util.colorFromHexString(styles.getForegroundColor()));
		// foreground.setButtonCaption(main.getUIText(UIConstants.TEXT));
		
		final HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.setWidth("100%");
		hl.addComponent(background);
		hl.addComponent(transparentBG);
		hl.setComponentAlignment(transparentBG, Alignment.BOTTOM_LEFT);
		final Label spacer = new Label(" ");
		hl.addComponent(spacer);
		hl.addComponent(foreground);
		hl.setComponentAlignment(foreground,
				new Alignment(Bits.ALIGNMENT_RIGHT));
				
		hl.setExpandRatio(spacer, 1.0f);
		pL.addComponent(hl);
		addComponent(p);
	}
	
	/**
	 * Adds the border settings panel in dialog
	 */
	private void addBorderSettings(StyleSettings styles) {
		final Panel p = new Panel(main.getUIText(StandardUIConstants.BORDER));
		final Layout pL = new VerticalLayout();
		p.setContent(pL);
		p.setStyleName("light");
		
		borderStyle = new NativeSelect(
				main.getUIText(StandardUIConstants.STYLE));
		borderStyle.addItem(BorderStyle.NONE);
		borderStyle.addItem(BorderStyle.SOLID);
		borderStyle.addItem(BorderStyle.DOTTED);
		
		borderStyle.setNullSelectionAllowed(false);
		borderStyle.setImmediate(true);
		borderStyle.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 3420396761480784196L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (borderStyle.getValue().equals(BorderStyle.NONE)) {
					borderColor.setEnabled(false);
					borderWidth.setEnabled(false);
					rounded.setEnabled(false);
				} else {
					borderColor.setEnabled(true);
					borderWidth.setEnabled(true);
					rounded.setEnabled(true);
				}
			}
		});
		borderColor = new ColorPicker(
				main.getUIText(StandardUIConstants.COLOR),
				Util.colorFromHexString(styles.getBorderColor()));
		borderWidth = new TextField(main.getUIText(StandardUIConstants.WIDTH)
				+ " (px)");
		borderWidth.setValue("" + styles.getBorderWidth());
		rounded = new TextField(main.getUIText(StandardUIConstants.ROUNDING)
				+ " (px)");
		rounded.setValue("" + styles.getBorderRounded());
		
		final GridLayout gl = new GridLayout(2, 2);
		gl.setWidth("100%");
		
		gl.addComponent(borderStyle);
		gl.addComponent(borderColor);
		gl.setComponentAlignment(borderColor, new Alignment(
				Bits.ALIGNMENT_BOTTOM));
		gl.addComponent(borderWidth);
		gl.addComponent(rounded);
		
		pL.addComponent(gl);
		
		addComponent(p);
		borderStyle.select(styles.getBorderStyle());
	}
	
	/**
	 * Adds the font settings panel in dialog
	 */
	private void addFontSettings(StyleSettings styles) {
		final Panel p = new Panel(main.getUIText(StandardUIConstants.FONT));
		final Layout pL = new VerticalLayout();
		p.setContent(pL);
		p.setStyleName("light");
		
		fontFamily = new NativeSelect(
				main.getUIText(StandardUIConstants.FONT_FAMILY));
		fontFamily.setNullSelectionAllowed(false);
		for (String font : FONTS) {
			fontFamily.addItem(font);
		}
		fontFamily.select(styles.getFontFamily());
		
		fontSize = new NativeSelect(
				main.getUIText(StandardUIConstants.FONT_SIZE));
		fontSize.setNullSelectionAllowed(false);
		for (String size : FONT_SIZES) {
			fontSize.addItem(size);
		}
		fontSize.select(styles.getFontSize());
		
		fontBolded = new CheckBox(main.getUIText(StandardUIConstants.BOLDED));
		fontBolded.setValue(styles.isFontBolded());
		
		fontItalic = new CheckBox(main.getUIText(StandardUIConstants.ITALIC));
		fontItalic.setValue(styles.isFontItalic());
		
		final GridLayout gl = new GridLayout(4, 1);
		gl.setWidth("100%");
		
		gl.addComponent(fontFamily);
		gl.addComponent(fontSize);
		gl.addComponent(fontBolded);
		gl.setComponentAlignment(fontBolded, new Alignment(
				Bits.ALIGNMENT_BOTTOM));
		gl.addComponent(fontItalic);
		gl.setComponentAlignment(fontItalic, new Alignment(
				Bits.ALIGNMENT_BOTTOM));
				
		pL.addComponent(gl);
		
		addComponent(p);
		
	}
	
	/**
	 * Adds the margin settings panel in dialog
	 */
	private void addMarginSettings(StyleSettings styles) {
		final Panel p = new Panel(main.getUIText(StandardUIConstants.MARGIN)
				+ " (" + main.getUIText(StandardUIConstants.PIXELS) + ")");
		final Layout pL = new VerticalLayout();
		p.setContent(pL);
		p.setStyleName("light");
		
		marginSymmetric = new CheckBox(
				main.getUIText(StandardUIConstants.SYMMETRIC));
		marginSymmetric.setImmediate(true);
		marginSymmetric.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 2302045222059530058L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (marginSymmetric.getValue()) {
					marginBottom.setEnabled(false);
					marginLeft.setEnabled(false);
					marginRight.setEnabled(false);
				} else {
					marginBottom.setEnabled(true);
					marginLeft.setEnabled(true);
					marginRight.setEnabled(true);
				}
				
			}
		});
		
		final int[] margin = styles.getMargin();
		marginTop = new TextField(main.getUIText(StandardUIConstants.TOP));
		marginTop.setImmediate(true);
		marginTop.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7807166620568865492L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (marginSymmetric.getValue()) {
					marginBottom.setValue(marginTop.getValue());
					marginLeft.setValue(marginTop.getValue());
					marginRight.setValue(marginTop.getValue());
				}
			}
		});
		
		marginRight = new TextField(main.getUIText(StandardUIConstants.RIGHT));
		marginBottom = new TextField(main.getUIText(StandardUIConstants.BOTTOM));
		marginLeft = new TextField(main.getUIText(StandardUIConstants.LEFT));
		
		marginTop.setValue("" + margin[0]);
		marginRight.setValue("" + margin[1]);
		marginBottom.setValue("" + margin[2]);
		marginLeft.setValue("" + margin[3]);
		
		final String width = "75px";
		marginTop.setWidth(width);
		marginRight.setWidth(width);
		marginBottom.setWidth(width);
		marginLeft.setWidth(width);
		
		// margin is symmetric, if all fields have same value
		marginSymmetric.setValue(margin[0] == margin[1]
				&& margin[0] == margin[2] && margin[0] == margin[3]);
				
		final GridLayout gl = new GridLayout(4, 1);
		gl.setWidth("100%");
		gl.addComponent(marginTop);
		gl.addComponent(marginRight);
		gl.addComponent(marginBottom);
		gl.addComponent(marginLeft);
		
		pL.addComponent(marginSymmetric);
		pL.addComponent(gl);
		
		addComponent(p);
		
	}
	
	/**
	 * Adds the padding settings panel in dialog
	 */
	private void addPaddingSettings(StyleSettings styles) {
		final Panel p = new Panel(main.getUIText(StandardUIConstants.PADDING)
				+ " (" + main.getUIText(StandardUIConstants.PIXELS) + ")");
		final Layout pL = new VerticalLayout();
		p.setContent(pL);
		p.setStyleName("light");
		
		paddingSymmetric = new CheckBox(
				main.getUIText(StandardUIConstants.SYMMETRIC));
		paddingSymmetric.setImmediate(true);
		paddingSymmetric.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 2302045222059530058L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (paddingSymmetric.getValue()) {
					paddingBottom.setEnabled(false);
					paddingLeft.setEnabled(false);
					paddingRight.setEnabled(false);
				} else {
					paddingBottom.setEnabled(true);
					paddingLeft.setEnabled(true);
					paddingRight.setEnabled(true);
				}
				
			}
		});
		
		final int[] padding = styles.getPadding();
		paddingTop = new TextField(main.getUIText(StandardUIConstants.TOP));
		paddingTop.setImmediate(true);
		paddingTop.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 5149244173955440799L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (paddingSymmetric.getValue()) {
					paddingBottom.setValue(paddingTop.getValue());
					paddingLeft.setValue(paddingTop.getValue());
					paddingRight.setValue(paddingTop.getValue());
				}
			}
		});
		
		paddingRight = new TextField(main.getUIText(StandardUIConstants.RIGHT));
		paddingBottom = new TextField(
				main.getUIText(StandardUIConstants.BOTTOM));
		paddingLeft = new TextField(main.getUIText(StandardUIConstants.LEFT));
		
		paddingTop.setValue("" + padding[0]);
		paddingRight.setValue("" + padding[1]);
		paddingBottom.setValue("" + padding[2]);
		paddingLeft.setValue("" + padding[3]);
		
		final String width = "75px";
		paddingTop.setWidth(width);
		paddingRight.setWidth(width);
		paddingBottom.setWidth(width);
		paddingLeft.setWidth(width);
		
		// Padding is symmetric, if all fields have same value
		paddingSymmetric.setValue(padding[0] == padding[1]
				&& padding[0] == padding[2] && padding[0] == padding[3]);
				
		final GridLayout gl = new GridLayout(4, 1);
		gl.setWidth("100%");
		gl.addComponent(paddingTop);
		gl.addComponent(paddingRight);
		gl.addComponent(paddingBottom);
		gl.addComponent(paddingLeft);
		
		pL.addComponent(paddingSymmetric);
		pL.addComponent(gl);
		
		addComponent(p);
		
	}
	
	/**
	 * Copies the style settings from the dialog to the object associated with this dialog
	 */
	@Override
	public StyleSettings getCurrStyles() {
		StyleSettings styles = new StubStyleSettings();
		// colors
		if (transparentBG.getValue()) {
			styles.setBackgroundColor("transparent");
		} else {
			styles.setBackgroundColor(Util.colorToHexString(background
					.getColor()));
		}
		styles.setForegroundColor(Util.colorToHexString(foreground.getColor()));
		
		// font
		styles.setFont((String) fontFamily.getValue(),
				(String) fontSize.getValue(), fontBolded.getValue(),
				fontItalic.getValue());
				
		try {
			// border
			styles.setBorder((BorderStyle) borderStyle.getValue(),
					Integer.parseInt(borderWidth.getValue()),
					Util.colorToHexString(borderColor.getColor()),
					Integer.parseInt(rounded.getValue()));
					
			// margin
			styles.setMargin(Integer.parseInt(marginTop.getValue()),
					Integer.parseInt(marginRight.getValue()),
					Integer.parseInt(marginBottom.getValue()),
					Integer.parseInt(marginLeft.getValue()));
					
			// padding
			styles.setPadding(Integer.parseInt(paddingTop.getValue()),
					Integer.parseInt(paddingRight.getValue()),
					Integer.parseInt(paddingBottom.getValue()),
					Integer.parseInt(paddingLeft.getValue()));
					
		} catch (NumberFormatException nfe) {
			Notification
					.show(main
							.getUIText(StandardUIConstants.INVALID_VALUE_IN_NUMBER_FIELDS),
							Notification.Type.ERROR_MESSAGE);
		}
		return styles;
	}
	
}
