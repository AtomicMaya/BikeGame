package main.game.actor.entities;


import main.game.actor.Graphics;
import main.math.Attachable;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

public class BetterTextGraphics extends Node implements Attachable, Graphics {
    private final ArrayList<String> graphics;
    private final ArrayList<Vector> offsets;
    private float charSize;
    private Vector modifier;

    public BetterTextGraphics(Vector position, String text, float fontSize, float containerWidth, float containerHeight) {
        this.charSize = fontSize - fontSize % 0.5f;
        this.graphics = getFileLocations(text.toUpperCase());
        this.offsets = new ArrayList<>();
        if(this.charSize < .5f) this.charSize = .5f;
        else if (5f < this.charSize) this.charSize = 5f;

        this.offsets.add(new Vector(position.x + (containerWidth - this.graphics.size()) / 2f, position.y + (containerHeight - this.charSize) / 2f).mul(charSize));
        for (int i = 0; i < this.graphics.size() - 1; i++) {
            this.offsets.add(this.offsets.get(i).add(new Vector(this.charSize, 0)));
        }

        ArrayList<Vector> mods = new ArrayList<>(Arrays.asList(new Vector(position.x + 10.5f, position.y), new Vector(position.x + 0, position.y),
                new Vector(position.x - 10.25f, position.y), new Vector(position.x - 20.5f, position.y),
                new Vector(position.x - 30.75f, position.y), new Vector(position.x - 41, position.y),
                new Vector(position.x - 51.25f, position.y), new Vector(position.x - 61.5f, position.y),
                new Vector(position.x - 71.75f, position.y), new Vector(position.x - 82f, position.y)));
        this.modifier = mods.get((int) ((charSize - 0.5f) * 2));
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
            canvas.drawImage(canvas.getImage(this.graphics.get(i)), Transform.I.scaled(charSize).translated(this.offsets.get(i).add(charSize* modifier.x, charSize * modifier.y)), 1, -.01f);
        }
    }
}
