package main.game.actor.entities;


import main.game.ActorGame;
import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/**
 * Created on 12/2/2017 at 7:10 PM.
 */
public class BetterTextGraphics extends GameEntity {
    ArrayList<ImageGraphics> graphics;

    public BetterTextGraphics(ActorGame game, Vector position, Vector basisOffset, String text, int fontSize, Shape shape) {
        super(game, true, position);

        this.build(shape, -1f, -1f, true);

        ArrayList<String> fileLocations = getFileLocations(text.toUpperCase());
        this.graphics = new ArrayList<>();
        Vector offset = basisOffset;
        for(String file : fileLocations) {
            this.graphics.add(this.addGraphics(file,fontSize * .13f, fontSize * .13f, offset, 1.f, -0.1f));
            offset = offset.add(-fontSize * .10f, 0);
        }
    }

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
        for(Graphics g : this.graphics) {
            g.draw(canvas);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
