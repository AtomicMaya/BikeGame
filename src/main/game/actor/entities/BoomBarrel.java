package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 12/8/2017 at 11:25 PM.
 */
public class BoomBarrel extends GameEntity {
    private ImageGraphics graphics;
    private ArrayList<String> boomGraphics;
    private final float animationTime;
    private float elapsedAnimationTime;
    private int graphicsCounter;
    private BasicContactListener contactListener;
    private boolean triggered, explosive;

    public BoomBarrel(ActorGame game, Vector position, boolean explosive) {
        super(game, false, position);

        this.explosive = explosive;

        this.build(new Polygon(0, 0, 1, 0, 1, 1.5f, 0, 1.5f), 1, 1, false);

        this.graphics = addGraphics(explosive ? "./res/images/barrel.red.png" : "./res/images/barrel.green.png", 1, 1.5f);
        if (explosive) {
            this.boomGraphics = new ArrayList<>(Arrays.asList("./res/images/explosion.bomb.0.png", "./res/images/explosion.bomb.1.png",
                    "./res/images/explosion.bomb.2.png", "./res/images/explosion.bomb.3.png", "./res/images/explosion.bomb.4.png",
                    "./res/images/explosion.bomb.5.png", "./res/images/explosion.bomb.6.png", "./res/images/explosion.bomb.7.png",
                    "./res/images/explosion.bomb.8.png", "./res/images/explosion.bomb.9.png", "./res/images/explosion.bomb.10.png",
                    "./res/images/explosion.bomb.11.png", "./res/images/explosion.bomb.12.png", "./res/images/explosion.bomb.13.png",
                    "./res/images/explosion.bomb.14.png", "./res/images/explosion.bomb.15.png", "./res/images/explosion.bomb.16.png",
                    "./res/images/explosion.bomb.17.png", "./res/images/explosion.bomb.18.png", "./res/images/explosion.bomb.19.png",
                    "./res/images/explosion.bomb.20.png", "./res/images/explosion.bomb.21.png", "./res/images/explosion.bomb.22.png",
                    "./res/images/explosion.bomb.23.png", "./res/images/explosion.bomb.24.png", "./res/images/explosion.bomb.25.png",
                    "./res/images/explosion.bomb.26.png", "./res/images/explosion.bomb.27.png", "./res/images/explosion.bomb.28.png",
                    "./res/images/explosion.bomb.29.png", "./res/images/explosion.bomb.30.png", "./res/images/explosion.bomb.31.png"));
        } else {
            this.boomGraphics = new ArrayList<>(Arrays.asList("./res/images/explosion.acid.0.png",  "./res/images/explosion.acid.1.png",
                    "./res/images/explosion.acid.2.png", "./res/images/explosion.acid.3.png", "./res/images/explosion.acid.4.png",
                    "./res/images/explosion.acid.5.png", "./res/images/explosion.acid.6.png", "./res/images/explosion.acid.7.png",
                    "./res/images/explosion.acid.8.png", "./res/images/explosion.acid.9.png", "./res/images/explosion.acid.10.png",
                    "./res/images/explosion.acid.11.png", "./res/images/explosion.acid.12.png", "./res/images/explosion.acid.13.png",
                    "./res/images/explosion.acid.14.png", "./res/images/explosion.acid.15.png"));
        }

        this.animationTime = .5f;
        this.elapsedAnimationTime = 0;
        this.contactListener = new BasicContactListener();
        this.addContactListener(this.contactListener);
        this.triggered = false;
    }

    @Override
    public void update(float deltaTime) {
        if(this.triggered)
            this.elapsedAnimationTime += deltaTime;

        if(this.contactListener.getEntities().size() > 0)
            for (Entity entity : this.contactListener.getEntities()) {
            if (explosive)
                entity.applyImpulse(new Vector(50, 50), Vector.ZERO);
            if (entity.getCollisionGroup() == ObjectGroup.PLAYER.group || entity.getCollisionGroup() == ObjectGroup.WHEEL.group)
                this.triggered = true;
            }


        this.graphicsCounter = (int) Math.floor(this.elapsedAnimationTime / this.animationTime * this.boomGraphics.size());
        if (this.graphicsCounter > this.boomGraphics.size() - 1) {
            this.getOwner().getPayload().triggerDeath(false);
            this.destroy();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(!this.triggered)
            this.graphics.draw(canvas);
        else {
            this.addGraphics(this.boomGraphics.get(this.graphicsCounter), 4, 4, Vector.ZERO, 1, 10).draw(canvas);

        }
    }

    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }
}
