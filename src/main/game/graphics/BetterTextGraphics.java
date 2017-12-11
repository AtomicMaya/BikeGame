package main.game.graphics;

import main.game.ActorGame;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Class handling the display of an awesome font.
 */
public class BetterTextGraphics extends Node implements Graphics {
	private ArrayList<String> graphics;
	private ArrayList<Vector> offsets;
	private ArrayList<float[]> charSizes = new ArrayList<>();

	// Text parameters
	private String text = "";
	private float charSize = 1;
	private float textLength;
	private float alpha = 1f, depth = -42+.01f;
	private float letterRatio = 73f;
	private float inBetweenCharOffset = 0;

	private Canvas canvas;

    /**
     * @param game : The {@linkplain ActorGame} instance where this will be displayed.
     * @param text : A {@linkplain String} containing the text to be displayed
     * @param fontSize : A {@linkplain Float} representing the font size.
     * @param anchor : A position {@linkplain Vector}, where this should be anchored.
     * @param alpha : A {@linkplain Float} representing the alpha value.
     */
    public BetterTextGraphics(ActorGame game, String text, float fontSize, Vector anchor, float alpha) {
        this.canvas = game.getCanvas();
        this.setRelativeTransform(Transform.I.translated(anchor));
        this.setText(text, fontSize);
        this.setAlpha(alpha);
    }

    /** @see #BetterTextGraphics(ActorGame, String, float, Vector, float) */
	public BetterTextGraphics(ActorGame game, String text, float fontSize, Vector anchor) {
        this(game, text, fontSize, anchor, 1f);
	}


	/**
	 * Gets all related paths to the letters of font.
	 * @param text : The {@linkplain String} containing the text.
	 * @return a {@linkplain ArrayList<String>} containing the paths to the font image.
	 */
	private ArrayList<String> getFileLocations(String text) {
		ArrayList<String> fileLocations = new ArrayList<>();
		for (char character : text.toCharArray()) {
			if (character >= 'A' && character <= 'Z')
				fileLocations.add("./res/font/l" + character + ".png");
			else if (character >= '0' && character <= '9')
				fileLocations.add("./res/font/n" + character + ".png");
			else {
				switch (character) {
					case '\'':
						fileLocations.add("./res/font/sApostrophe.png");
					    break;
					case '\\':
						fileLocations.add("./res/font/sBackslash.png");
					    break;
					case '/':
						fileLocations.add("./res/font/sSlash.png");
					    break;
					case ':':
						fileLocations.add("./res/font/sColon.png");
					    break;
					case ',':
						fileLocations.add("./res/font/sComma.png");
					    break;
					case '.':
						fileLocations.add("./res/font/sDot.png");
					    break;
					case '-':
						fileLocations.add("./res/font/sDash.png");
					    break;
					case '+':
						fileLocations.add("./res/font/sPlus.png");
					    break;
					case '!':
						fileLocations.add("./res/font/sEMark.png");
                        break;
					case '?':
						fileLocations.add("./res/font/sQMark.png");
					    break;
					case '(':
						fileLocations.add("./res/font/sLParen.png");
					    break;
					case ')':
						fileLocations.add("./res/font/sRParen.png");
					    break;
					case '"':
						fileLocations.add("./res/font/sQuot.png");
					    break;
					case ' ':
						fileLocations.add("./res/font/sSpace.png");
					    break;
					default:
                        break;
				}
			}
		}
		return fileLocations;
	}

	@Override
	public void draw(Canvas canvas) {
		for (int i = 0; i < this.graphics.size(); i++) {
			Transform t = new Transform(this.charSizes.get(i)[0], 0, this.getPosition().x + this.offsets.get(i).x, 0,
					this.charSizes.get(i)[1], this.getPosition().y + this.offsets.get(i).y);
			canvas.drawImage(canvas.getImage(this.graphics.get(i)), t, this.alpha, this.depth);		}
	}

	/**
	 * @return the text of this better text graphics.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @return the charSize of this better text graphics.
	 */
	public float getCharSize() {
		return this.charSize;
	}

	/**
	 * Change the text of this better text graphics.
	 * @param text : The new text.
	 * @param fontSize : The new font size, a negative value will be inverted.
	 */
	public void setText(String text, float fontSize) {
		this.charSize = fontSize;
		this.text = (text == null) ? this.text : text;
		this.graphics = getFileLocations(this.text.toUpperCase(Locale.ROOT));
		this.offsets = new ArrayList<>();
		this.charSizes = new ArrayList<>();
		this.text = text;

		float offset = 0;
		for (int i = 0; i < this.graphics.size(); i++) {
			float letterWidth = this.canvas.getImage(this.graphics.get(i)).getWidth() / this.letterRatio;
			this.offsets.add(new Vector(offset, 0));
			this.charSizes.add(new float[] { letterWidth * this.charSize, this.charSize });
			offset += letterWidth * this.charSize + this.inBetweenCharOffset;
		}
		this.textLength = offset - this.inBetweenCharOffset;
	}

	/**
	 * Change the text of this better text graphics.
     * @param text : The new text.
	 */
	public void setText(String text) {
		this.setText(text, this.charSize);
	}

	/**
	 * @param alpha new alpha
	 */
	public void setAlpha(float alpha) {
		this.alpha = (alpha < 0) ? 1 : alpha;
	}

	/**
     *
	 * @param depth : new depth for the {@param text}.
	 */
	public void setDepth(float depth) {
		this.depth = depth;
	}

	/**
     * Gets the length of the text.
	 * @return the total text length of this {@linkplain BetterTextGraphics}.
	 */
	public float getTotalWidth() {
		return this.textLength;
	}

	/**
	 * Add a space between each char in the text.
	 * @param value : The space value.
	 */
	public void setInBetweenCharTextOffset(float value) {
		this.inBetweenCharOffset = value;
		this.setText(this.text);
	}

	/**
	 * Sets text anchor, i.e. how to orient it.
	 * @param anchor : The text anchor
	 */
	public void setAnchor(Vector anchor) {
		this.setRelativeTransform(Transform.I.translated(anchor));
	}
}
