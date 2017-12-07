package main.window.swing;

/**
 * Use to store a font for the text
 */
public class MyFont {

	private static String font = null;

	private static FontList fontList = FontList.DEFAULT;

	/**
	 * Set the font to use
	 */
	public static void setFont(FontList fl) {
		font = fl.toString();
		fontList = fl;
	}

	/**
	 * @return the font name
	 */
	public static String getFontName() {
		return font;
	}

	/**
	 * @return fontList
	 */
	public static FontList getFont() {
		return fontList;
	}

	/**
	 * Reset the font
	 */
	public void reset() {
		font = null;
	}
}
