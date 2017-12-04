package main.game.actor.graphicalStuff;

import java.util.ArrayList;


public enum Preset {
    //Clouds, Leaves, Trees
    Breezy(4, 3, 2);

    private ArrayList<Integer> objectCount;
    Preset(int... objectCounter) {
        for (int i : objectCounter) this.objectCount.add(i);
    }

    public ArrayList<Integer> getObjectCount() {
        return this.objectCount;
    }
}
