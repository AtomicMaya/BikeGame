package main.game.actor.entities;

import main.game.actor.Actor;
import main.game.graphics.Graphics;
import main.game.graphics.ImageGraphics;
import main.game.graphics.ShapeGraphics;
import main.math.Entity;
import main.math.PartBuilder;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

public abstract class CinematicEntity implements Actor {

    Entity entity;
    private ArrayList<Graphics> graphics = new ArrayList<>();
    private PartBuilder partBuilder = null;

    CinematicEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void draw(Canvas window) {
        for (Graphics g : graphics)
            g.draw(window);
    }

    /**
     * Add Graphics to a given entity
     */
    protected void setGraphics(ArrayList<Graphics> graphics) {
        for (Graphics g : graphics)
            setGraphics(g);
    }

    protected void setGraphics(Graphics g) {
        if (g != null) {
            if (g instanceof ShapeGraphics) {
                ((ShapeGraphics) g).setParent(entity);
            } else if (g instanceof ImageGraphics) {
                ((ImageGraphics) g).setParent(entity);
            }
            this.graphics.add(g);
        }
    }

    @Override
    public Vector getPosition() {
        return entity.getPosition();
    }


    @Override
    public Vector getVelocity() {
        return entity.getVelocity();
    }

    @Override
    public Transform getTransform() {
        return entity.getTransform();
    }
}
