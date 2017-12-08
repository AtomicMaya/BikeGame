package main.game.graphicalStuff;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class EndGameGraphics implements Graphics, Actor {
    private ImageGraphics graphics;

    public EndGameGraphics(ActorGame game, String fileName) {
        this.graphics =  new ImageGraphics(fileName, game.getViewScale() * 0.75f, game.getViewScale() * 0.70f, Vector.ZERO, 1, 100);
    }

    @Override
    public void draw(Canvas canvas) {
       this.graphics.draw(canvas);
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    @Override
    public Vector getPosition() {
        return null;
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
