package main.game.graphicalActors;

import java.util.ArrayList;
import java.util.Collections;

/** Allows the on-the-fly parametrization of {@linkplain GraphicalObjects}. */
public enum Preset {
    Breezy(new String[]{"main.game.graphicalActors.Cloud", "main.game.graphicalActors.BlowingLeaf"},
            new Integer[]{10, 150},
            new Float[][]{new Float[]{-.25f, 0f, -.5f, 0f}, new Float[]{-0.5f, -0.05f, -3f, -0.5f}},
            new Float[][]{new Float[]{1.5f, .5f, 3f, 1.5f}, new Float[]{.5f, .5f, .5f, .5f}}),
    Snowy(new String[]{"main.game.graphicalActors.Snowflake"},
            new Integer[]{200},
            new Float[][]{new Float[]{-0.25f, -0.05f, -2f, -0.5f}},
            new Float[][]{new Float[]{.4f, .4f, .4f, .4f}});

    /** The {@linkplain GraphicalObjects} class names. */
    private ArrayList<String> objectNames;

    /** In what quantity the {@linkplain GraphicalObjects} should spawn. */
    private ArrayList<Integer> objectQuantities;

    /** How fast these {@linkplain GraphicalObjects} can move. */
    private ArrayList<Float[]> speedBounds;

    /** What size these {@linkplain GraphicalObjects} can have. */
    private ArrayList<Float[]> sizeBounds;

    /***
     * Generates an easily callable {@linkplain Preset} allowing for quick generation on {@linkplain GraphicalObjects}.
     * @param classPaths The class names of the {@linkplain GraphicalObjects}.
     * @param number The quantity of {@linkplain GraphicalObjects}.
     * @param speedBounds The speed bounds of the {@linkplain GraphicalObjects}.
     * @param sizeBounds The size bounds of the {@linkplain GraphicalObjects}.
     */
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

    /** @return the class names of the chosen presets {@linkplain GraphicalObjects}. */
    public ArrayList<String> getObjectNames() {
        return this.objectNames;
    }

    /** @return the amount of {@linkplain GraphicalObjects} to generate. */
    public ArrayList<Integer> getObjectQuantities() {
        return this.objectQuantities;
    }

    /** @return the {@linkplain GraphicalObjects} speed limits. */
    public ArrayList<Float[]> getSpeedBounds() {
        return this.speedBounds;
    }

    /** @return the {@linkplain GraphicalObjects} size bounds. */
    public ArrayList<Float[]> getSizeBounds() {
        return this.sizeBounds;
    }
}
