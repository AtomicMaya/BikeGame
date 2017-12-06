package main.game.actor.entities;

import java.util.ArrayList;
import java.util.Locale;

import main.game.ActorGame;
import main.game.actor.Graphics;
import main.math.Attachable;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class BetterTextGraphics extends Node implements Attachable, Graphics {
	private ArrayList<String> graphics;
	private ArrayList<Vector> offsets;
	private ArrayList<float[]> charSizes = new ArrayList<>();

	// text parameters
	private String text;
	private float charSize = 1;
	private float textLength;
	private float alpha = 1, debth = -.01f;
	private float letterRatio = 73f;
	private float inBeetweenCharOffset = 0;

	private Canvas canvas;

	public BetterTextGraphics(ActorGame game, String text, float fontSize) {
		canvas = game.getCanvas();
		setText(text, fontSize);
	}

	/**
	 * Gets all related paths to letters of font...
	 * 
	 * @param text : The input text
	 * @return an arrayList containing paths to the font image.
	 */
	private ArrayList<String> getFileLocations(String text) {
		ArrayList<String> fileLocations = new ArrayList<>();
		for (char c : text.toCharArray()) {
			if (c >= 'A' && c <= 'Z')
				fileLocations.add("./res/font/l" + c + ".png");
			else if (c >= '0' && c <= '9')
				fileLocations.add("./res/font/n" + c + ".png");
			else {
				switch (c) {
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

			canvas.drawImage(canvas.getImage(this.graphics.get(i)), t, this.alpha, this.debth);

		}
	}

	/**
	 * @return the text of this better text graphics
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @return the charSize of this better text graphics
	 */
	public float getCharSize() {
		return this.charSize;
	}

	/**
	 * Change the text of this better text graphics
	 * 
	 * @param text new text
	 * @param charSize new char size, negative value will flip the text through f(x)
	 *            = -x;
	 */
	public void setText(String text, float fontSize) {
		this.charSize = fontSize;
		this.text = (text == null) ? "" : text;
		this.graphics = getFileLocations(text.toUpperCase(Locale.ROOT));
		this.offsets = new ArrayList<>();
		this.charSizes = new ArrayList<>();
		this.text = text;

		float o = 0;
		for (int i = 0; i < this.graphics.size(); i++) {
			float letterWidth = canvas.getImage(graphics.get(i)).getWidth() / letterRatio;
			this.offsets.add(new Vector(o, 0));
			this.charSizes.add(new float[] { letterWidth * this.charSize, this.charSize });
			o += letterWidth * this.charSize + inBeetweenCharOffset;

		}
		textLength = o - inBeetweenCharOffset; // - inBeetweenCharOffset pour corigé, il y en a un en trop
	}

	/**
	 * Change the text of this better text graphics
	 * 
	 * @param text new text
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
	 * @param depth new depth for the text
	 */
	public void setDepth(float depth) {
		this.debth = depth;
	}

	/**
	 * @return the total text length of this better text graphics
	 */
	public float getTotalWidth() {
		return textLength;
	}

	/**
	 * Add a space between each char in the text
	 * 
	 * @param value space value
	 */
	public void setInBetweenCharTextOffset(float value) {
		this.inBeetweenCharOffset = value;
		setText(text);
	}
}
