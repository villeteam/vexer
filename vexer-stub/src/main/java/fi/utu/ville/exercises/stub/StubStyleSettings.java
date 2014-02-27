package fi.utu.ville.exercises.stub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.utu.ville.standardutils.ui.DynamicStyles.StyleSettings;

/**
 * Stub-implementor for {@link StyleSettings}
 * 
 * @author Erkki Kaila, Riku Haavisto
 * 
 */
class StubStyleSettings implements StyleSettings {

	/* colors */
	private String backgroundColor;
	private String foregroundColor;

	private BorderStyle borderStyle;
	private int borderWidth;
	private String borderColor;
	private int borderRounded;

	/* margins & paddings */
	private String marginString;
	private String paddingString;
	private final int[] margin = { 0, 0, 0, 0 };
	private final int[] padding = { 0, 0, 0, 0 };

	/* fonts */
	private String fontFamily;
	private String fontSize;
	private boolean fontBolded;
	private boolean fontItalic;

	/**
	 * Creates new style settings with default values for each property.
	 */
	public StubStyleSettings() {
		backgroundColor = "transparent";
		foregroundColor = "#000000";
		borderStyle = BorderStyle.NONE;
		borderWidth = 0;
		borderColor = "#000000";
		borderRounded = 0;
		marginString = "0";
		paddingString = "0";
		fontFamily = "sans-serif";
		fontSize = "12px";
		fontBolded = false;
		fontItalic = false;
	}

	/**
	 * Sets the border property of this style
	 * 
	 * @param style
	 *            the style of the border
	 * @param width
	 *            the width of the border in pixels
	 * @param borderColor
	 *            the color of the border in CSS compatible hexadecimal
	 *            representation, starting with #
	 * @param roundedAmount
	 *            the amount of rounding applied in pixels. Note, that this may
	 *            not work in all browsers.
	 */
	@Override
	public void setBorder(BorderStyle style, int width, String borderColor,
			int roundedAmount) {
		borderStyle = style;
		borderWidth = width;
		borderRounded = roundedAmount;
		if (!borderColor.startsWith("#")) {
			borderColor = "#" + borderColor;
		}
		this.borderColor = borderColor;
	}

	/**
	 * Sets the color properties of this style. Colors are given in CSS
	 * compatible hexadecimal representation, starting with #
	 * 
	 * @param foregroundColor
	 *            the text color
	 * @param backgroundColor
	 *            the background color or "transparent"
	 */
	@Override
	public void setColors(String foregroundColor, String backgroundColor) {
		if (!foregroundColor.startsWith("#")) {
			foregroundColor = "#" + foregroundColor;
		}
		if (!backgroundColor.startsWith("#")
				&& !backgroundColor.equals("transparent")) {
			backgroundColor = "#" + backgroundColor;
		}
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Sets all margins in symmetric form.
	 * 
	 * @param marginInPixels
	 *            margin in pixels
	 */
	@Override
	public void setMargin(int marginInPixels) {
		margin[0] = marginInPixels;
		margin[1] = marginInPixels;
		margin[2] = marginInPixels;
		margin[3] = marginInPixels;
		marginString = marginInPixels + "px";
	}

	/**
	 * Sets the padding property in symmetric form.
	 * 
	 * @param paddingInPixels
	 *            padding in pixels
	 */
	@Override
	public void setPadding(int paddingInPixels) {
		padding[0] = paddingInPixels;
		padding[1] = paddingInPixels;
		padding[2] = paddingInPixels;
		padding[3] = paddingInPixels;
		paddingString = paddingInPixels + "px";
	}

	/**
	 * Sets the margin individually for all sides.
	 * 
	 * @param top
	 *            top margin in pixels
	 * @param right
	 *            right margin in pixels
	 * @param bottom
	 *            bottom margin in pixels
	 * @param left
	 *            left margin in pixels
	 */
	@Override
	public void setMargin(int top, int right, int bottom, int left) {
		margin[0] = top;
		margin[1] = right;
		margin[2] = bottom;
		margin[3] = left;
		marginString = top + "px " + right + "px " + bottom + "px " + left
				+ "px";
	}

	/**
	 * Sets the padding individually for all sides.
	 * 
	 * @param top
	 *            top padding in pixels
	 * @param right
	 *            right padding in pixels
	 * @param bottom
	 *            bottom padding in pixels
	 * @param left
	 *            left padding in pixels
	 */
	@Override
	public void setPadding(int top, int right, int bottom, int left) {
		padding[0] = top;
		padding[1] = right;
		padding[2] = bottom;
		padding[3] = left;
		paddingString = top + "px " + right + "px " + bottom + "px " + left
				+ "px";
	}

	/**
	 * <p>
	 * Sets the font property for this style.
	 * <p>
	 * Note, that components other properties may override these settings.
	 * Typical example is using the rich text editor to enter label content.
	 * 
	 * @param fontFamily
	 *            font family; please prefer generic font names
	 * @param fontSize
	 *            size of the font with unit added (such as "21px", "105%" or
	 *            "1.5em")
	 * @param bolded
	 *            if true, font is displayed in bold
	 * @param italic
	 *            if true, font is displayed in bold.
	 */
	@Override
	public void setFont(String fontFamily, String fontSize, boolean bolded,
			boolean italic) {
		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
		fontBolded = bolded;
		fontItalic = italic;
	}

	/**
	 * Returns these settings as a CSS string.
	 * 
	 * @return settings in CSS.
	 */
	@Override
	public String getAsCss() {
		String css = "";
		if (borderColor.equals("#0")) {
			borderColor = "#000";
		}
		// set colors
		css += "color: " + foregroundColor + ";";
		css += "background-color:" + backgroundColor + ";";

		// set borders
		if (borderStyle != BorderStyle.NONE) {
			css += "border: " + borderStyle.getStyle() + " " + borderWidth
					+ "px " + borderColor + ";";
			if (borderRounded > 0) {
				final String bpx = borderRounded + "px;";
				css += "border-radius:" + bpx;
				css += "-webkit-border-radius:" + bpx;
				css += "-moz-border-radius:" + bpx;
			}
		}

		// set margin and padding
		css += "margin:" + marginString + ";";
		css += "padding:" + paddingString + ";";

		// set font
		css += "font-family:" + fontFamily + ";";
		css += "font-size:" + fontSize + ";";
		css += "font-weight:" + (fontBolded ? "bold;" : "normal;");
		css += "font-style:" + (fontItalic ? "italic;" : "normal;");

		return css;

	}

	@Override
	public String getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public String getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	@Override
	public String getBorderColor() {
		return borderColor;
	}

	@Override
	public int getBorderWidth() {
		return borderWidth;
	}

	@Override
	public int getBorderRounded() {
		return borderRounded;
	}

	@Override
	public BorderStyle getBorderStyle() {
		return borderStyle;
	}

	@Override
	public String getFontFamily() {
		return fontFamily;
	}

	@Override
	public String getFontSize() {
		return fontSize;
	}

	@Override
	public boolean isFontBolded() {
		return fontBolded;
	}

	@Override
	public boolean isFontItalic() {
		return fontItalic;
	}

	@Override
	public int[] getMargin() {
		return margin;
	}

	@Override
	public int[] getPadding() {
		return padding;
	}

	/**
	 * Copies all settings from given StyleSettings object to <code>this</code>
	 * 
	 * @param ss
	 *            the settings copied to this object
	 */
	public void copySettings(StyleSettings ss) {
		// colors
		setColors(ss.getForegroundColor(), ss.getBackgroundColor());
		// font
		setFont(ss.getFontFamily(), ss.getFontSize(), ss.isFontBolded(),
				ss.isFontItalic());
		// border
		setBorder(ss.getBorderStyle(), ss.getBorderWidth(),
				ss.getBorderColor(), ss.getBorderRounded());
		// margin
		setMargin(ss.getMargin()[0], ss.getMargin()[1], ss.getMargin()[2],
				ss.getMargin()[3]);
		// padding
		setPadding(ss.getPadding()[0], ss.getPadding()[1], ss.getPadding()[2],
				ss.getPadding()[3]);
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();

		try {
			json.put("bgColor", backgroundColor);
			json.put("fgColor", foregroundColor);
			json.put("borderStyle", borderStyle.getStyle());
			json.put("borderWidth", borderWidth);
			json.put("borderColor", borderColor);
			json.put("borderRounded", borderRounded);
			JSONArray margins = new JSONArray(margin);
			json.put("margins", margins);
			JSONArray paddings = new JSONArray(padding);
			json.put("paddings", paddings);
			json.put("fontFamily", fontFamily);
			json.put("fontSize", fontSize);
			json.put("fontBolded", fontBolded);
			json.put("fontItalic", fontItalic);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

	private static int[] parseIntJSONArr(JSONArray toParse)
			throws JSONException {

		int size = toParse.length();
		int[] res = new int[size];
		for (int i = 0; i < size; i++) {
			res[i] = toParse.getInt(i);
		}
		return res;

	}

	@Override
	public void fromJson(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			backgroundColor = json.getString("bgColor");
			foregroundColor = json.getString("fgColor");
			borderStyle = BorderStyle.getFromStyle(json
					.getString("borderStyle"));
			borderWidth = json.getInt("borderWidth");
			borderColor = json.getString("borderColor");
			borderRounded = json.getInt("borderRounded");
			int[] margins = parseIntJSONArr(json.getJSONArray("margins"));
			setMargin(margins[0], margins[1], margins[2], margins[3]);
			int[] paddings = parseIntJSONArr(json.getJSONArray("paddings"));
			setPadding(paddings[0], paddings[1], paddings[2], paddings[3]);
			fontFamily = json.getString("fontFamily");
			fontSize = json.getString("fontSize");
			fontBolded = json.getBoolean("fontBolded");
			fontItalic = json.getBoolean("fontItalic");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
