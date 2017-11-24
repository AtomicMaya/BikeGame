package main.game.actor.myEntities;

import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.math.Shape;

import java.awt.*;

public class EntityBuilder {

    public static ImageGraphics addGraphics(Entity entity, String imagePath, float width, float height){
        ImageGraphics ig = new ImageGraphics(imagePath, width, height);
        ig.setParent(entity);
        return ig;
    }


    public static ShapeGraphics addGraphics(Entity entity, Shape shape, Color fillColor, Color outlineColor, float thickness, float alpha, float depth){
        ShapeGraphics s = new ShapeGraphics(shape, fillColor, outlineColor, thickness, alpha, depth);
        s.setParent(entity);
        return s;
    }

    public static void build(Entity entity, Shape shape){
        build(entity, shape, -1, -1, false);
    }
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

    private static void setParent(Entity entity, ImageGraphics ig){
        if (ig!=null)ig.setParent(entity);
    }

    private static void setParent(Entity entity, ShapeGraphics ig){
        if (ig!=null)ig.setParent(entity);
    }
}
