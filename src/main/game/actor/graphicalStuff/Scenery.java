package main.game.actor.graphicalStuff;

import main.game.GameObjects.Rectangle;
import main.game.actor.Actor;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class Scenery implements Actor {
    private Vector minimumCoords, maximumCoords, minimumSpawn;
    private int preset;
    private ArrayList<GraphicalObjects> graphics;
    private Vector position;
    private float ratio, length, height;
    private Random random;

    public Scenery(Vector position, float translationRatio) {
        setViewPointPosition(position, translationRatio);
        this.graphics = new ArrayList<>();

        this.random = new Random();
        Preset thisPreset = Preset.Breezy;
        ArrayList<String> classPaths = thisPreset.getObjectNames();
        int counter = 0;
        for(Integer quantities : thisPreset.getObjectQuantities()) {
            Float[] speeds = thisPreset.getSpeedBounds().get(counter);
            Float[] sizes = thisPreset.getSizeBounds().get(counter);
            for(int i = 0; i < quantities; i++) {
                float randX = this.minimumSpawn.x + this.random.nextFloat() * (this.maximumCoords.x - this.minimumSpawn.x);
                float randY = this.minimumSpawn.y + this.random.nextFloat() * (this.maximumCoords.y - this.minimumSpawn.y);
                float randSpeedX = speeds[0] + this.random.nextFloat() * (speeds[2] - speeds[0]);
                float randSpeedY = speeds[1] + this.random.nextFloat() * (speeds[3] - speeds[1]);
                float sizeX = sizes[1] + this.random.nextFloat() * (sizes[3] - sizes[1]);
                float sizeY = sizes[0] + this.random.nextFloat() * (sizes[2] - sizes[0]);
                try {
                    this.graphics.add((GraphicalObjects) Class.forName(classPaths.get(counter))
                            .getConstructor(Vector.class, Rectangle.class, Vector.class)
                            .newInstance(new Vector(randX, randY), new Rectangle(sizeX, sizeY), new Vector(randSpeedX, randSpeedY)));
                } catch (NoSuchMethodException|ClassNotFoundException|InstantiationException|IllegalAccessException|InvocationTargetException ignored) { }
            }
            counter += 1;
        }

        this.position = position;
        this.ratio = translationRatio;

    }

    public Scenery(int preset) {

    }

    public void setViewPointPosition(Vector position, float translationRatio) {
        this.ratio = translationRatio;
        this.length = 2.5f * this.ratio;
        this.height = 1.1f * this.ratio;

        this.minimumCoords = new Vector(position.x - this.length / 2f, position.y - this.height * 0.3f);
        this.maximumCoords = this.minimumCoords.add(length, height);
        this.position = this.minimumCoords;
        this.minimumSpawn = new Vector(this.position.x + this.length * 0.8f, this.position.y + 0.8f * height);
        //System.out.println(this.position.x + "," + this.position.y + "," + minimumSpawn.x + "," + minimumSpawn.y);
        //wSystem.out.println((minimumSpawn.x > maximumCoords.x) + "," +  (minimumSpawn.y > maximumCoords.y));

    }

    @Override
    public void update(float deltaTime) {
        //Transform transformOfBox = Transform.I.translated(this.position.x - this.length / this.ratio, this.position.y - this.height / this.ratio + this.ratio / 2.7f);
        int counter2 = 0;
        for(GraphicalObjects object : this.graphics) {
            if(object.getPosition().x < this.minimumCoords.x || object.getPosition().y > this.maximumCoords.y) {
                object.setPosition(new Vector(this.minimumSpawn.x + random.nextFloat() * (this.maximumCoords.x - this.minimumSpawn.x),  this.minimumSpawn.y + this.random.nextFloat() * (this.maximumCoords.y - this.minimumSpawn.y)));
            }
            if(object instanceof BlowingLeaf && counter2 == 0) {
                System.out.println(position.x + "," + position.y + "," + object.getPosition().x + "," + object.getPosition().y + "," + maximumCoords.x + "," + maximumCoords.y + "," + minimumSpawn.x + "," + minimumSpawn.y + "," +  minimumCoords.x + "," + minimumCoords.y);
                counter2 += 1; }
            object.update(deltaTime);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void draw(Canvas canvas) {
        for(GraphicalObjects object : this.graphics) {
            object.draw(canvas);
        }

        canvas.drawShape(new Polygon(0, 0, length, 0, length, height, 0, height),
                Transform.I.translated(minimumCoords), Color.CYAN, Color.GREEN, .1f, 0.65f, 20);
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
