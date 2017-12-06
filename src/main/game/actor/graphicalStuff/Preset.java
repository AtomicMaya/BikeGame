package main.game.actor.graphicalStuff;

import java.util.ArrayList;
import java.util.Collections;


public enum Preset {
    //Clouds, Leaves, Trees
    Breezy(new String[]{"main.game.actor.graphicalStuff.Cloud", "main.game.actor.graphicalStuff.BlowingLeaf"},
            new Integer[]{3, 6},
            new Float[][]{new Float[]{-1f, 0f, -2f, 0f}, new Float[]{-0.5f, -0.05f, -3f, -0.5f}},
            new Float[][]{new Float[]{2f, .05f, 6f, 1.5f}, new Float[]{.5f, .5f, .5f, .5f}});

    private ArrayList<String> objectNames;
    private ArrayList<Integer> objectQuantities;
    private ArrayList<Float[]> speedBounds;
    private ArrayList<Float[]> sizeBounds;

    Preset(String[] classPaths, Integer[] number, Float[][] speedBounds, Float[][] sizeBounds) {
        this.objectNames = new ArrayList<>();
        this.objectQuantities = new ArrayList<>();
        this.speedBounds = new ArrayList<>();
        this.sizeBounds = new ArrayList<>();
        Collections.addAll(this.objectNames, classPaths);
        Collections.addAll(this.objectQuantities, number);
        Collections.addAll(this.speedBounds, speedBounds);
        Collections.addAll(this.sizeBounds, sizeBounds);
    }

    public ArrayList<String> getObjectNames() {
        return this.objectNames;
    }

    public ArrayList<Integer> getObjectQuantities() {
        return this.objectQuantities;
    }

    public ArrayList<Float[]> getSpeedBounds() {
        return this.speedBounds;
    }

    public ArrayList<Float[]> getSizeBounds() {
        return this.sizeBounds;
    }
}
