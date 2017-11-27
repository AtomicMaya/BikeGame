package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.awt.Color;


public class TestEntity extends GameEntity {
	private ShapeGraphics graphics;

	public TestEntity(ActorGame game, Vector position, Polygon shape) {
		super(game, true, position);
		EntityBuilder.build(this.getEntity(), shape);
		graphics = EntityBuilder.addGraphics(this.getEntity(), shape, null, Color.BLACK, .1f, 1, 0);
	}

	@Override
	public void draw(Canvas window) { graphics.draw(window); }
}
