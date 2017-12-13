package main.game.actor;

/** Allows for verbose referencing of ok {@linkplain main.math.Entity}'s collision groups. */
public enum ObjectGroup {
    PLAYER (42),
    WHEEL (1),
    TERRAIN (2),
    OBSTACLE (3),
    SENSOR (4),
    FINISH (5),
    CHECKPOINT (6),
    ENEMY (7),
    PROJECTILE(8);

    public final int group;

    ObjectGroup(int group) {
        this.group = group;
    }
}
