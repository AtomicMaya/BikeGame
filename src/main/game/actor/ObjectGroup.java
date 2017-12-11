package main.game.actor;

public enum ObjectGroup {
    PLAYER (0),
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
