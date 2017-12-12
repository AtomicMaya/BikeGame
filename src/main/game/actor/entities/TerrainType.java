package main.game.actor.entities;

/** Enumerates the different types of {@linkplain Terrain}. */
public enum TerrainType {
    NORMAL (1.5f, "#6D5D49", "#548542"),
    MUD (.3f, "#92817c", "#a99790"),
    ICE (0.007f, "#D4F0FF", "#A6DBF7"),
    STONE (3, "#c4c4bc", "#9c9c96");

    /** The friction given to this {@linkplain TerrainType}. */
    public float friction;
    /** The color strings given to this {@linkplain TerrainType}. */
    public String fillColor, outlineColor;

    /**
     * Creates a new {@linkplain TerrainType}.
     * @param friction A friction parameter.
     * @param fillColor The wished-for fill color.
     * @param outlineColor The wished-for outline color.
     */
    TerrainType(float friction, String fillColor, String outlineColor) {
        this.friction = friction;
        this.fillColor = fillColor;
        this.outlineColor = outlineColor;
    }
}
