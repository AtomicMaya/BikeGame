package main.game.actor.myEntities;

import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Entity;
import main.math.PartBuilder;
import main.math.Shape;

import java.awt.*;

public class EntityBuilder {

    /**
     * Create and add an ImageGraphics to an entity
     *
     * @param entity The entity
     * @param imagePath The path to the image to add
     * @param width The width of the image
     * @param height The height of the image
     * @return A new ImageGraphics associated to the entity
     * */
    public static ImageGraphics addGraphics(Entity entity, String imagePath, float width, float height){
        ImageGraphics graphics = new ImageGraphics(imagePath, width, height);
        graphics.setParent(entity);
        return graphics;
    }

    /**
     * Create and add a ShapeGraphics to an entity
     *
     * @param entity The entity
     * @param shape shape, may be null
     * @param fillColor fill color, may be null
     * @param outlineColor outline color, may be null
     * @param thickness outline thickness
     * @param alpha transparency, between 0 (invisible) and 1 (opaque)
     * @param depth render priority, lower-values drawn first
     * @return The ShapeGraphics created and associated to the image
     */
    public static ShapeGraphics addGraphics(Entity entity, Shape shape, Color fillColor, Color outlineColor, float thickness, float alpha, float depth){
        ShapeGraphics graphics = new ShapeGraphics(shape, fillColor, outlineColor, thickness, alpha, depth);
        graphics.setParent(entity);
        return graphics;
    }

    public static ShapeGraphics addGraphics(Entity entity, Shape shape, Color color) {
        return addGraphics(entity, shape, color, color, 0.f, 0.f, 0.f);
    }
    /**
     * Build the entity, which give a physical representation for the engine
     *
     * @param entity The entity
     * @param shape The shape to give to the entity
     */
    public static void build(Entity entity, Shape shape){
        build(entity, shape, -1, -1, false);
    }

    /**
     * Build the entity, which give a physical representation for the engine
     *
     * @param entity The entity
     * @param shape The shape to give to the entity
     * @param friction friction to give to the entity, if negative -> default value
     * @param density density of the entity, if negative -> default value
     * @param ghost whether this part is hidden and act only as a sensor
     */
    public static void build(Entity entity, Shape shape, float friction, float density, boolean ghost){
        PartBuilder partBuilder = entity.createPartBuilder();
        partBuilder.setShape(shape);
        if (friction >= 0)
            partBuilder.setFriction(friction);
        if (density >= 0)
            partBuilder.setDensity(density);
        partBuilder.setGhost(ghost);
        partBuilder.build();
    }

    private static void setParent(Entity entity, ImageGraphics graphics){
        if (graphics != null) graphics.setParent(entity);
    }

    private static void setParent(Entity entity, ShapeGraphics graphics){
        if (graphics != null) graphics.setParent(entity);
    }
}
