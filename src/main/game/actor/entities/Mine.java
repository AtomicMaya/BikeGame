package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.sensors.ProximitySensor;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 12/9/2017 at 10:53 AM.
 */
public class Mine extends GameEntity {
    private ArrayList<String> stateGraphics;
    private ArrayList<String> boomGraphics;
    private final float beepTime, boomAnimationTime;
    private final int beeps;
    private float elapsedTime, elapsedBoomTime;
    private boolean triggered, blowingUp, state, previousState;
    private int boomGraphicsCounter, currentBeep;

    private ProximitySensor sensor;

    public Mine(ActorGame game, Vector position) {
        super(game, true, position);

        this.beepTime = 2f;
        this.boomAnimationTime = .3f;
        this.beeps = 3;
        this.state = false;
        this.previousState = false;

        this.build(new Polygon(0, 0, .5f, 0, .5f, 1, 0, 1), -1, -1, false);

        this.stateGraphics = new ArrayList<>(Arrays.asList("./res/images/mine.0.png", "./res/images/mine.1.png"));
        this.boomGraphics = new ArrayList<>(Arrays.asList( "./res/images/mine.explosion.0.png", "./res/images/mine.explosion.1.png",
                "./res/images/mine.explosion.2.png", "./res/images/mine.explosion.3.png", "./res/images/mine.explosion.4.png",
                "./res/images/mine.explosion.5.png", "./res/images/mine.explosion.6.png", "./res/images/mine.explosion.7.png",
                "./res/images/mine.explosion.8.png", "./res/images/mine.explosion.9.png", "./res/images/mine.explosion.10.png",
                "./res/images/mine.explosion.11.png", "./res/images/mine.explosion.12.png", "./res/images/mine.explosion.13.png",
                "./res/images/mine.explosion.14.png", "./res/images/mine.explosion.15.png"));

        this.triggered = false;
        this.sensor = new ProximitySensor(game, position.add(-.5f,1), new Polygon(0, 0, 1.5f, 0, 1.5f, 3, 0 ,3));

        game.addActor(this.sensor);
    }

    @Override
    public void update(float deltaTime) {
        if (this.sensor.getSensorDetectionStatus())
            this.triggered = true;

        if (this.triggered) {
            this.elapsedTime += deltaTime;
            this.state = (int) Math.floor(this.elapsedTime / this.beepTime * this.boomGraphics.size() * this.beeps) % this.boomGraphics.size() == 1;
        }

        if (this.state != this.previousState && this.state)
            currentBeep += 1;

        if (this.currentBeep >= this.beeps)
            this.blowingUp = true;

        if (this.blowingUp) {
            this.elapsedBoomTime += deltaTime;
            this.boomGraphicsCounter = (int) Math.floor(this.elapsedBoomTime / this.boomAnimationTime * (this.boomGraphics.size()));
        }
        if (this.boomGraphicsCounter > this.boomGraphics.size() - 1) {
            if(this.sensor.getSensorDetectionStatus())
                this.getOwner().getPayload().triggerDeath(false);
            this.destroy();
        }

        this.previousState = this.state;
    }

    @Override
    public void destroy() {
        this.sensor.destroy();
        super.destroy();
        this.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        if(this.blowingUp)
            this.sensor.addGraphics(this.boomGraphics.get(boomGraphicsCounter), 1.5f, 3).draw(canvas);
        if (this.triggered)
            this.addGraphics(stateGraphics.get(this.state ? 1 : 0), .5f, 1, Vector.ZERO, 1, 100).draw(canvas);
    }
}
