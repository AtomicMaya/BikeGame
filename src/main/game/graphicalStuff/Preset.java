package main.game.graphicalStuff;

import java.util.ArrayList;
import java.util.Collections;


public enum Preset {
    Breezy(new String[]{"main.game.graphicalStuff.Cloud", "main.game.graphicalStuff.BlowingLeaf"},
            new Integer[]{10, 150},
            new Float[][]{new Float[]{-.25f, 0f, -.5f, 0f}, new Float[]{-0.5f, -0.05f, -3f, -0.5f}},
            new Float[][]{new Float[]{1.5f, .5f, 3f, 1.5f}, new Float[]{.5f, .5f, .5f, .5f}});

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
