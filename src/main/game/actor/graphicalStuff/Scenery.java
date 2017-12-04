package main.game.actor.graphicalStuff;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.QuickMafs;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

public class Scenery implements Actor {
    private Vector minimumCoords, maximumCoords;
    private int preset;
    private ArrayList<GraphicalObjects> graphics;

    public Scenery() {
        graphics = new ArrayList<>();
        ArrayList<Integer> presetContents = Preset.Breezy.getObjectCount();
        for(int count : presetContents) {
            float randX, randY;
        }

    }

    public Scenery(int preset) {

    }

    public void setViewPointPosition(Vector position, float translationRatio) {
        minimumCoords = position.sub(-1.25f * translationRatio, - 0.5f * translationRatio);
        maximumCoords = minimumCoords.mul(QuickMafs.xyInverted);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void reCreate(ActorGame game) {

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
