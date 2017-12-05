package main.game.actor.entities;


import main.game.actor.Graphics;
import main.math.Attachable;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

public class BetterTextGraphics extends Node implements Attachable, Graphics {
    private ArrayList<String> graphics;
    private ArrayList<Vector> offsets;
    //private Vector position;
    private float charSize;
    private Vector modifier;
    private float textLength;
    private float alpha = 1, debth = .01f;
    
    public BetterTextGraphics(/*Vector position, */String text, float fontSize) {
        this.graphics = getFileLocations(text.toUpperCase());
        this.offsets = new ArrayList<>();
        this.textLength = text.length();
     //   this.position = position;
        this.charSize = fontSize;

        this.offsets.add(new Vector(charSize, 0.25f * charSize));
        for (int i = 0; i < this.graphics.size() - 1; i++) {
            this.offsets.add(this.offsets.get(i).add(new Vector(this.charSize, 0)));
        }
    }
    
    public void setText(String text) {
    	this.graphics = getFileLocations(text.toUpperCase());
    	this.offsets = new ArrayList<>();
    	this.offsets.add(new Vector(charSize, 0.25f * charSize));
        for (int i = 0; i < this.graphics.size() - 1; i++) {
            this.offsets.add(this.offsets.get(i).add(new Vector(this.charSize, 0)));
        }
    }

    public void setAlpha(float alpha) {
    	this.alpha = alpha;
    }
    
    public void setDepth(float depth) {
    	
    }

    /**
     * Gets all related paths to letters of font...
     * @param text : The input text
     * @return an arrayList containing paths to the font image.
     */
    private ArrayList<String> getFileLocations(String text) {
        ArrayList<String> fileLocations = new ArrayList<>();
        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') fileLocations.add("./res/font/l" + c + ".png");
            else if (c >= '0' && c <= '9') fileLocations.add("./res/font/n" + c + ".png");
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
        for(int i = 0; i < this.graphics.size(); i++) {
        	// j'ai changé avec getPosition pour avoir la position du parent (donc Vector.ZERO si pas de parent)
        	//Transform t = new Transform(charSize, 0, position.x + offsets.get(i).x, 0, charSize, this.position.y + offsets.get(i).y);
        	Transform t = new Transform(charSize, 0, getPosition().x + offsets.get(i).x, 0, charSize, getPosition().y + offsets.get(i).y);
            canvas.drawImage(canvas.getImage(this.graphics.get(i)), t/*.transformed(getTransform())*/, alpha, debth);

        }
    }
}
