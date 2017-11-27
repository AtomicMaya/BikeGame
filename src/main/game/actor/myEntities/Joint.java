package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polygon;
import main.math.PrismaticConstraintBuilder;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

import static main.game.actor.myEntities.EntityBuilder.addGraphics;
import static main.game.actor.myEntities.EntityBuilder.build;


public class Joint extends GameEntity {
	private Vector anchor;
	private ShapeGraphics graphics;
	private boolean relative;

	public Joint(ActorGame game, boolean relative, Vector position, float length) {
		super(game, false, position);
		if(!relative) anchor = position;
		Polygon member = new Polygon(.0f, .0f, length, .0f, length, length, .0f, length);
		build(this.getEntity(), member);
		graphics = addGraphics(this.getEntity(), member, null, Color.BLACK, .1f, 1.f, 0f);
	}

	public void attach(Joint anotherJoint, Vector anchor, Vector axis) {
		this.anchor = anchor;
		PrismaticConstraintBuilder constraintBuilder = super.getOwner().createPrismaticConstraintBuilder();
		constraintBuilder.setFirstEntity(anotherJoint.getEntity());
		constraintBuilder.setFirstAnchor(anchor);
		constraintBuilder.setSecondEntity(this.getEntity());
		constraintBuilder.setFirstAnchor(Vector.ZERO);
		constraintBuilder.setAxis(axis);
		constraintBuilder.setLimitEnabled(true);
	}

	public Vector getAnchor() {
		return this.anchor;
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

}
