package main.game.actor.graphicalStuff;

import main.game.actor.Graphics;
import main.math.Node;
import main.math.Vector;
import main.window.Canvas;

/**
 * Created on 12/4/2017 at 11:32 AM.
 */
public class Cloud extends Node implements Graphics {
    private String file;
    private Vector position;
    private float length, height;

    public Cloud(Vector position, float length, float height) {
        this.file = "";
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage();
    }
}
