package main.game.actor;

/**
 * Created on 12/6/2017 at 9:10 AM.
 */
public enum ObjectGroup {
    PLAYER (0),
    WHEEL (1),
    TERRAIN (2),
    SENSOR (3),
    FINISH (4),
    CHECKPOINT (5);

    public final int group;

    ObjectGroup(int group) {
        this.group = group;
    }
}
