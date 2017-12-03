package main.game.actor.entities;


import main.game.ActorGame;
import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/**
 * Created on 12/2/2017 at 7:10 PM.
 */
public class BetterTextGraphics extends GameEntity {
    ArrayList<ImageGraphics> graphics;

    public BetterTextGraphics(ActorGame game, Vector position, String text, int fontSize, float containerWidth, float containerHeight) {
        super(game, true, position);

        float charWidth = fontSize * 0.13f;
        String[] lines = new String[text.split(" ").length];
        for(int i = 0; i < lines.length; i++) lines[i] = "";
        float lineMaxLength = containerWidth - 2 * charWidth;
        float lineCurrentLength = 0;

        int lineCounter = 0;
        int wordCounter = 0;
        for(String word : text.split(" ")) {
            lineCurrentLength += word.length() * charWidth;
            if (lineCurrentLength + charWidth <= lineMaxLength) {
                lines[lineCounter] += word + " ";
                wordCounter += 1;
            } else if (lineCurrentLength <= lineMaxLength) {
                lines[lineCounter] += word;
                lineCounter += 1;
                wordCounter = 0;
            } else if (lineCurrentLength > lineMaxLength && wordCounter == 0) {
                lines[lineCounter] += word;
                lineCounter += 1;
            } else {
                lineCounter += 1;
                lineCurrentLength = word.length() * charWidth;
                lines[lineCounter] += word;
                lineCounter += 1;
            }
        }

        ArrayList<String> lines2 = new ArrayList<>();
        for (String s : lines) {
            if(!s.equals("")) lines2.add(s);
        }

        int[] indices = new int[lines2.size()];
        int counter = 0;
        for (String s : lines2) {
            if (counter > 0) indices[counter] = indices[counter - 1] + s.length();
            else indices[counter] = s.length();
            counter += 1;
        }

        ArrayList<String> fileLocations = getFileLocations(text.toUpperCase());

        this.graphics = new ArrayList<>();
        counter = 0;
        float startY = (containerHeight - charWidth) / 2 + (lines2.size() * charWidth) / 2 - charWidth * 0.75f;
        for (String s : lines2) {
            float startX = -(containerWidth - 2 * charWidth) / 2 - (s.length() * (charWidth + 0.01f)) / 2 + containerWidth + charWidth / 2;
            System.out.println(startX + ", " + startY);
            Vector offset = new Vector(-startX, -startY);
            for(int i = counter == 0 ? 0 : indices[counter - 1]; i < indices[counter]; i++) {
                this.graphics.add(this.addGraphics(fileLocations.get(i),fontSize * .13f, fontSize * .13f, offset, 1.f, -0.1f));
                offset = offset.add(-fontSize * .13f, 0);
            }
            counter += 1;
            startY -= fontSize * .14f;
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
