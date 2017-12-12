package main.game.graphicalStuff;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Polygon;
import main.math.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

/** A class handling the drawing of background graphics. */
public class Scenery implements Actor {
    /** The chosen {@linkplain Preset}. */
    private Preset preset;

    /** A {@linkplain ArrayList} of {@linkplain GraphicalObjects}, for easy updating. */
    private ArrayList<GraphicalObjects> graphics;

    /** A new {@linkplain Random} generator. */
    private Random random;

    /** The master {@linkplain ActorGame}. */
    private ActorGame game;

    /**
     * Creates a new {@linkplain Scenery}.
     * @param game The master {@linkplain ActorGame}.
     * @param preset The chosen {@linkplain Preset}.
     */
    public Scenery(ActorGame game, Preset preset) {
        this.game = game;
        this.preset = preset;
        this.graphics = new ArrayList<>();
        this.random = new Random();
        this.instantiate();
    }

    /**
     * Gets a new {@linkplain Vector} from the canvas.
     * @return a new {@linkplain Vector}.
     */
    public Vector newSample() {
        float minX = this.game.getCanvas().getPosition().x - 1.2f * this.game.getViewScale(), minY = this.game.getCanvas().getPosition().y - .8f * this.game.getViewScale(),
                maxX = this.game.getCanvas().getPosition().x + 1.8f * this.game.getViewScale(), maxY = this.game.getCanvas().getPosition().y + 1.f * this.game.getViewScale();
        return new Polygon(minX, minY, maxX, minY, maxX, maxY, minX, maxY).sample(new Random());
    }

    /**
     * Creates new {@linkplain GraphicalObjects}.
     * Explanation 1 : Magic...
     * Explanation 2 : Class.forName({@linkplain String}) gets the associated class,
     *                  .getConstructor(params) gets the classes constructor,
     *                  .newInstance(params) creates a new {@linkplain GraphicalObjects} of this type.
     */
    private void instantiate() {
        ArrayList<String> classPaths = this.preset.getObjectNames();
        float ratio = game.getRelativeTransform().m00;
        this.graphics = new ArrayList<>();
        int counter = 0;
        for(Integer quantities : this.preset.getObjectQuantities()) {
            Float[] speeds = this.preset.getSpeedBounds().get(counter);
            Float[] sizes = this.preset.getSizeBounds().get(counter);
            for(int i = 0; i < quantities; i++) {
                Vector position = newSample();
                float randSpeedX = speeds[0] + this.random.nextFloat() * (speeds[2] - speeds[0]) * ratio / 30f;
                float randSpeedY = speeds[1] + this.random.nextFloat() * (speeds[3] - speeds[1]) * ratio / 30f;
                float sizeX = sizes[1] + this.random.nextFloat() * (sizes[3] - sizes[1]) * ratio / 30f;
                float sizeY = sizes[0] + this.random.nextFloat() * (sizes[2] - sizes[0]) * ratio / 30f;
                try {
                    this.graphics.add((GraphicalObjects) Class.forName(classPaths.get(counter))
                            .getConstructor(Vector.class, Rectangle.class, Vector.class)
                            .newInstance(position, new Rectangle(sizeX, sizeY), new Vector(randSpeedX, randSpeedY)));
                } catch (NoSuchMethodException|ClassNotFoundException|InstantiationException|IllegalAccessException|InvocationTargetException ignored) { }
            }
            counter += 1;
        }
    }

    @Override
    public void update(float deltaTime) {
        for(GraphicalObjects object : this.graphics) {
            object.update(deltaTime);
            if(object.getIfResets()) {
                object.setPosition(this.newSample());
            }

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
