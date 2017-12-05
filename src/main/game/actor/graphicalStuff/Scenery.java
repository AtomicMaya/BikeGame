package main.game.actor.graphicalStuff;

import main.game.actor.Actor;
import main.game.actor.QuickMafs;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.ArrayList;

public class Scenery implements Actor {
    private Vector minimumCoords, maximumCoords, minimumSpawn;
    private int preset;
    private ArrayList<GraphicalObjects> graphics;
    private Vector position;
    private float ratio, length, height, length2;

    public Scenery(Vector position, float translationRatio) {
        setViewPointPosition(position, translationRatio);
        this.graphics = new ArrayList<>();

        ArrayList<Integer> presetContents = Preset.Breezy.getObjectCount();
        for(int count : presetContents) {
            float randX, randY;
        }

        this.position = position;
        this.ratio = translationRatio;

    }

    public Scenery(int preset) {

    }

    public void setViewPointPosition(Vector position, float translationRatio) {
        this.minimumCoords = new Vector(position.x -1.1f * translationRatio, position.y -0.75f * translationRatio);
        this.maximumCoords = this.minimumCoords.mul(QuickMafs.xyInverted);
        this.position = this.minimumCoords;
        this.minimumSpawn = new Vector(this.position.x + this.minimumCoords.x + this.maximumCoords.x + this.ratio * 2.25f, this.minimumCoords.y);
        this.ratio = translationRatio;
        this.length = 2 * 1.3f * this.ratio;
        this.height = this.ratio;
        this.length2 = translationRatio * 0.5f;
    }

    @Override
    public void update(float deltaTime) {
        Transform transformOfBox = Transform.I.translated(this.position.x - this.length / this.ratio, this.position.y - this.height / this.ratio + this.ratio / 2.7f);
        //System.out.println(transformOfBox.getAffineTransform().getTranslateX() + ", " + transformOfBox.getAffineTransform().getTranslateY());
        System.out.println(this.minimumSpawn + "," + this.maximumCoords);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawShape(new Polygon(0, 0, length, 0, length, height, 0, height),
                Transform.I.translated(this.position.x - this.length / this.ratio, this.position.y - height / ratio + ratio / 2.7f), Color.CYAN, Color.GREEN, .1f, 0.65f, 20);
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
