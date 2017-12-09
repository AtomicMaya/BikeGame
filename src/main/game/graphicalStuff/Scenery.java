package main.game.graphicalStuff;

import main.game.ActorGame;
import main.game.GameObjects.Rectangle;
import main.game.actor.Actor;
import main.game.actor.entities.GameEntity;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.geom.AffineTransform;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class Scenery implements Actor {
    private Vector minimumCoords, maximumCoords, minimumSpawn;
    private Preset preset;
    private ArrayList<GraphicalObjects> graphics;
    private float length, height;
    private Random random;
    private ActorGame game;
    private float elapsedTime, timeUntilAnimationStart;
    public Scenery(ActorGame game) {
        this.game = game;
        this.graphics = new ArrayList<>();
        this.random = new Random();

        elapsedTime = 0;
        timeUntilAnimationStart = 2;

    }

    public Scenery(int preset) {

    }

    public void resetViewPointPosition() {
        AffineTransform windowTransform = this.game.getRelativeTransform().getAffineTransform();
        this.length = (float) (windowTransform.getScaleX() * 2.2f);
        this.height = (float) windowTransform.getScaleY() * 2f;
        this.minimumCoords = new Vector(((GameEntity) game.getPayload()).getPosition().x - length * .2f, (float) (windowTransform.getTranslateY() - this.height * .1f));
        this.maximumCoords = this.minimumCoords.add(this.length * 1.8f, this.height * .5f);
        this.minimumSpawn = this.minimumCoords.add(this.length * 1.6f, 0);
    }

    private void instantiate() {
        this.preset = Preset.Breezy;
        ArrayList<String> classPaths = this.preset.getObjectNames();
        float ratio = game.getRelativeTransform().m00;
        this.graphics = new ArrayList<>();
        int counter = 0;
        for(Integer quantities : this.preset.getObjectQuantities()) {
            Float[] speeds = this.preset.getSpeedBounds().get(counter);
            Float[] sizes = this.preset.getSizeBounds().get(counter);
            for(int i = 0; i < quantities; i++) {
                float randX = this.minimumSpawn.x + this.random.nextFloat() * (this.maximumCoords.x - this.minimumSpawn.x);
                float randY = this.minimumSpawn.y + this.random.nextFloat() * (this.maximumCoords.y - this.minimumSpawn.y);
                float randSpeedX = speeds[0] + this.random.nextFloat() * (speeds[2] - speeds[0]) * ratio / 30f;
                float randSpeedY = speeds[1] + this.random.nextFloat() * (speeds[3] - speeds[1]) * ratio / 30f;
                float sizeX = sizes[1] + this.random.nextFloat() * (sizes[3] - sizes[1]) * ratio / 30f;
                float sizeY = sizes[0] + this.random.nextFloat() * (sizes[2] - sizes[0]) * ratio / 30f;
                try {
                    this.graphics.add((GraphicalObjects) Class.forName(classPaths.get(counter))
                            .getConstructor(Vector.class, Rectangle.class, Vector.class)
                            .newInstance(new Vector(randX, randY), new Rectangle(sizeX, sizeY), new Vector(randSpeedX, randSpeedY)));
                } catch (NoSuchMethodException|ClassNotFoundException|InstantiationException|IllegalAccessException|InvocationTargetException ignored) { }
            }
            counter += 1;
        }
    }

    @Override
    public void update(float deltaTime) {
        if (elapsedTime < timeUntilAnimationStart) {
            resetViewPointPosition();
            instantiate();
            elapsedTime += deltaTime;
            return;
        }
        resetViewPointPosition();
        for(GraphicalObjects object : this.graphics) {
            if(object.getPosition().x < this.minimumCoords.x || object.getPosition().y < this.minimumCoords.y ||
                    object.getPosition().x > this.maximumCoords.x || object.getPosition().y > this.maximumCoords.y) {
                float newX = this.minimumSpawn.x + this.random.nextFloat() * (this.maximumCoords.x - this.minimumSpawn.x);
                float newY = this.minimumSpawn.y + this.random.nextFloat() * (this.maximumCoords.y - this.minimumSpawn.y);
                object.setPosition(new Vector(newX,  newY));
            }
            object.update(deltaTime);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void draw(Canvas canvas) {
        if (elapsedTime < timeUntilAnimationStart)
            return;
        for(GraphicalObjects object : this.graphics) {
            object.draw(canvas);
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

    @Override
    public Vector getPosition() {
        return null;
    }
}
