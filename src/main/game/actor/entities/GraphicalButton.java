package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Graphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;
import main.window.Mouse;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created on 12/2/2017 at 5:53 PM.
 */
public class GraphicalButton extends GameEntity {
    private ActorGame game;
    private Mouse mouse;

    private ArrayList<Graphics> graphics;
    private ArrayList<Float> time;
    private ArrayList<Runnable> actions;

    private float minX, minY, maxX, maxY;

    private boolean hovered, clicked;
    private boolean buttonBusy = false;

    private float timeToActionEnd, elapsedActionTime = 0.f;
    private String text;
    private BetterTextGraphics textGraphics;


    public GraphicalButton(ActorGame game, Vector position, Polygon shape, String text, int fontSize) {
        super(game, true, position);
        this.game = game;
        this.mouse = game.getMouse();
        this.text = text;

        this.graphics = new ArrayList<>();
        this.graphics.add(addGraphics(shape, Color.GREEN, Color.ORANGE, .1f,0.6f, -0.2f));
        this.graphics.add(addGraphics(shape, Color.RED, Color.ORANGE, .1f,0.6f, -0.2f));
        this.graphics.add(addGraphics(shape, Color.PINK, Color.ORANGE, .1f, 0.6f, -0.2f));

        this.actions = new ArrayList<>();
        this.time = new ArrayList<>();

        Vector maxPosition = shape.getPoints().get((shape.getPoints().size()) / (2));
        this.minX = position.x;
        this.minY = position.y;
        this.maxX = this.minX + maxPosition.x;
        this.maxY = this.minY + maxPosition.y;

        textGraphics = new BetterTextGraphics(game, position, text, fontSize, maxX - minX, maxY - minY);

        build(shape, -1, -1, true);

    }

    @Override
    public void update(float deltaTime) {
        Vector mousePosition = this.mouse.getPosition();
        float mouseX = mousePosition.x, mouseY = mousePosition.y;
        if (this.minX <= mouseX && mouseX <= this.maxX && this.minY < mouseY && mouseY < this.maxY) {
            this.hovered = true;
            this.clicked = this.mouse.getLeftButton().isPressed();
        } else {
            this.hovered = false;
        }

        if (this.clicked & !this.buttonBusy) {
            for(int i = 0; i < this.actions.size(); i++) {
                this.runAction(this.actions.get(i), this.time.get(i));
            }
        }

        if (this.buttonBusy) {
            this.elapsedActionTime += deltaTime;
            if (this.elapsedActionTime > this.timeToActionEnd) {
                this.buttonBusy = false;
                this.elapsedActionTime = 0.f;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.clicked) this.graphics.get(2).draw(canvas);
        else if (this.hovered) this.graphics.get(1).draw(canvas);
        else this.graphics.get(0).draw(canvas);
        textGraphics.draw(canvas);
    }

    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    public void setNewGraphics(String idleGraphics, String hoverGraphics, String clickGraphics) {
        this.graphics = new ArrayList<>();
        this.graphics.add(addGraphics(idleGraphics, this.maxX - this.minX, this.maxY - this.minY, Vector.ZERO, 1, -0.2f));
        this.graphics.add(addGraphics(hoverGraphics, this.maxX - this.minX, this.maxY - this.minY, Vector.ZERO, 1, -0.2f));
        this.graphics.add(addGraphics(clickGraphics, this.maxX - this.minX, this.maxY - this.minY, Vector.ZERO, 1, -0.2f));
    }

    public void addOnClickAction(Runnable action, float expirationTime) {
        this.actions.add(action);
        this.time.add(expirationTime);
    }

    private void runAction(Runnable runnable, float time) {
        this.buttonBusy = true;
        this.timeToActionEnd = time;
        Runner.generateWorker(runnable).execute();
    }


}
