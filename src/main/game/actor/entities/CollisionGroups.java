package main.game.actor.entities;

/**
 * Created on 12/6/2017 at 9:10 AM.
 */
public enum CollisionGroups {
    PLAYER (0),
    WHEEL (1),
    TERRAIN (2);

    public final int group;

    CollisionGroups(int group) {
        this.group = group;
    }
}
