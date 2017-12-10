package main.game.levels;

import main.game.actor.Actor;
import main.game.graphics.Graphics;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/**
 * Created on 12/10/2017 at 11:28 AM.
 */
public class CinematicStage implements Actor {

    public ArrayList<Graphics> graphics = new ArrayList<>();

    public void addGraphic(Graphics graphics) {
        this.graphics.add(graphics);
    }

    public ArrayList<Graphics> getGraphics() {
        ArrayList<Graphics> temporary = new ArrayList<>(this.graphics);
        this.graphics.clear();
        return temporary;
    }

    @Override
    public void draw(Canvas canvas) {
        for(Graphics graphics : this.graphics) {
            graphics.draw(canvas);
        }
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
