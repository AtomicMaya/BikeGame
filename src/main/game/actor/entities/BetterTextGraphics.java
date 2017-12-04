package main.game.actor.entities;


import main.game.ActorGame;
import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

public class BetterTextGraphics extends GameEntity {
    ArrayList<ImageGraphics> graphics;

    public BetterTextGraphics(ActorGame game, Vector position, String text, int fontSize, float containerWidth, float containerHeight) {
        super(game, true, position);

        // Standard FontSize to Pixel ratio.
        float charSize = fontSize * 0.13f;

        String[] textSplit = text.toUpperCase().split(" ");
        String[] lines = new String[textSplit.length];
        for(int i = 0; i < lines.length; i++) lines[i] = "";            // Initialize the array so as not to have null written in the text.

        ArrayList<String> lines2 = new ArrayList<>();
        for (String s : lines) if(!s.equals("")) lines2.add(s);         // Remove unnecessary empty lines.


        int[] indices = new int[lines2.size()];                         // Calculate where in the sentence the cut took place
        int counter = 0;
        for (String s : lines2) {
            if (counter > 0) indices[counter] = indices[counter - 1] + s.length();
            else indices[counter] = s.length();
            counter += 1;
        }

        this.graphics = new ArrayList<>();
        int counter2 = 0;
        float startY = (containerHeight - charSize) / 2 + (lines2.size() * charSize) / 2 - charSize * 0.75f;            // TODO fix math because transform is weird
        for (String s : lines2) {
            float startX = -(containerWidth - 2 * charSize) / 2 - (s.length() * (charSize + 0.01f)) / 2 + containerWidth + charSize / 2; // TODO fix math because transform is weird
            System.out.println(startX + ", " + startY);
            Vector offset = new Vector(-startX, -startY);
            for(String file : getFileLocations(s)) {
                this.graphics.add(this.addGraphics(file,fontSize * .13f, fontSize * .13f, offset, 1.f, -0.1f));
                offset = offset.add(-fontSize * .13f, 0);
            }
            counter2 += 1;
            startY -= fontSize * .14f;
        }
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
        for(Graphics g : this.graphics) {
            g.draw(canvas);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
