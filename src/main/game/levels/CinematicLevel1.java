package main.game.levels;

import main.game.ActorGame;
import main.game.graphics.ShapeGraphics;
import main.math.Circle;
import main.math.Polygon;

import java.awt.*;

/**
 * Created on 12/10/2017 at 11:51 AM.
 */
public class CinematicLevel1 extends CinematicLevel {

    private CinematicStage cs1, cs2, cs3;

    public CinematicLevel1(ActorGame game) {
        super(game);
    }

    @Override
    public boolean isFinished() {
        return getStage() == 4;
    }


    @Override
    public void createAllActors() {
        this.cs1 = new CinematicStage();
        this.cs1.addGraphic(new ShapeGraphics(new Circle(1f), null, Color.GREEN, .2f, 1, 9));
        this.cs2 = new CinematicStage();
        this.cs2.addGraphic(new ShapeGraphics(new Polygon(0,0,1,0,1,1,0,1), null, Color.ORANGE, .2f, 1, 9));

    }

    @Override
    public void destroy() {

    }
}
