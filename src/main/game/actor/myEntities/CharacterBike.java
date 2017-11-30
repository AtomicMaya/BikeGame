package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.QuickMafs;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

import static main.game.actor.QuickMafs.toRadians;

public class CharacterBike extends GameEntity {
	private Vector headPos, armJointPos, backPos;
	private Vector lElbowPos, rElbowPos, lHandPos, rHandPos;
	private Vector lKneePos, rKneePos, lFootPos, rFootPos;
	private Polyline body;
	private Circle head;

	private float directionModifier;
	private ShapeGraphics graphics, graphicsHead;
	private PrismaticConstraint constraint;

	private float angle;
	private final float pedalingAngleIncrement = 3.f;
	private boolean isYaying;
	private final float timeTillYayEnd = 1.5f;
	private float elapsedYayTime;

	// Insurance Vectors, so as to reset the various moving limbs to default positions
	private Vector initLElbowPos = new Vector(.5f, 1.f), initLHandPos = new Vector(1.f, .8f);
	private Vector initRElbowPos = new Vector(.5f, 1.f), initRHandPos = new Vector(1.f, .8f);
	private Vector lElbowRaisedPos = new Vector(.0f, 2.2f), lHandRaisedPos = new Vector(.0f, 2.75f);
	private Vector rElbowRaisedPos = new Vector(-.2f, 2.2f), rHandRaisedPos = new Vector(-.2f, 2.75f);

	/**
	 * Initialize a Character
	 * @param game : the game in which this character exists
	 * @param position : the position the character occupies
	 */
	public CharacterBike(ActorGame game, Vector position) {
		super(game, false, position);
		this.directionModifier = 1.f;

		headPos = new Vector(.2f, 1.75f); armJointPos = new Vector(.0f, 1.5f); backPos = new Vector(-.3f, .5f);
		lElbowPos = initLElbowPos; lHandPos = initLHandPos;
		rElbowPos = initRElbowPos; rHandPos = initRHandPos;
		lKneePos = new Vector(.2f, .3f); lFootPos = new Vector(.1f, -.3f);
		rKneePos = new Vector(.0f, .2f); rFootPos = new Vector(-.1f, -.3f);

		Circle anchor = new Circle(0.1f);
		build(this.getEntity(), anchor, -1f, -1, true);

		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .15f, 1.f, 0);

		head = new Circle(.3f, headPos.add(.1f * directionModifier, .1f));
		graphicsHead = addGraphics(this.getEntity(), head, Color.WHITE, Color.BLACK, .15f, 1.f, 0);
	}

	@Override
	public void update(float deltaTime) {
		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .15f, 1.f, 0);
		head = new Circle(.3f, headPos.add(.1f * directionModifier, .1f));
		graphicsHead = addGraphics(this.getEntity(), head, Color.WHITE, Color.BLACK, .15f, 1.f, 0);
	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
		graphicsHead.draw(canvas);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	/**
	 * Generates the shape of the body given all positions.
	 * @return a Polyline
	 */
	private Polyline generatePolygon() {
		return new Polyline(headPos, armJointPos, lElbowPos, lHandPos, lElbowPos, armJointPos, rElbowPos, rHandPos,
				rElbowPos, armJointPos, backPos, lKneePos, lFootPos, lKneePos, backPos, rKneePos, rFootPos, rKneePos,
				backPos, armJointPos);
	}

	/**
	 * Attaches this to the entity, at the anchor point
	 * @param vehicle : the entity on which this will be attached
	 * @param anchor : the point were this should be centered
	 */
	protected void attach(Entity vehicle, Vector anchor) {
		PrismaticConstraintBuilder builder = super.getOwner().createPrismaticConstraintBuilder();
		builder.setFirstEntity(vehicle);
		builder.setSecondEntity(this.getEntity());
		builder.setFirstAnchor(anchor);
		builder.setSecondAnchor(Vector.ZERO);
		builder.setLimitEnabled(true);
		builder.setMotorEnabled(true);
		builder.setMotorSpeed(0);
		builder.setMotorMaxTorque(10f);
		builder.setLowerTranslationLimit(0);
		builder.setUpperTranslationLimit(0);
		builder.setAxis(Vector.Y);
		builder.setInternalCollision(false);
		constraint = builder.build();
	}

	/**
	 * Removes the constraint
	 */
	public void detach() {
		if (constraint != null) constraint.destroy();
	}

	/**
	 *
	 */
	public void nextPedal() {
		angle += -directionModifier * pedalingAngleIncrement;
		angle %= 360;
		lFootPos = new Vector( (float) (lFootPos.x + Math.cos(toRadians(angle)) * 0.01 * -directionModifier),
				(float) (lFootPos.y + Math.sin(toRadians(angle))* 0.01 * -directionModifier));
		rFootPos = new Vector((float) (rFootPos.x + Math.cos(toRadians(angle) - Math.PI) * 0.01 * -directionModifier),
				(float) (rFootPos.y + Math.sin(toRadians(angle) - Math.PI) * 0.01 * -directionModifier));
		lKneePos = new Vector((float) (lKneePos.x + Math.cos(toRadians(angle)) * 0.005 * -directionModifier),
				(float) (lKneePos.y + Math.sin(toRadians(angle))* 0.005 * -directionModifier));
		rKneePos = new Vector((float) (rKneePos.x + Math.cos(toRadians(angle) - Math.PI) * 0.005 * -directionModifier),
				(float) (rKneePos.y + Math.sin(toRadians(angle) - Math.PI) * 0.005 * -directionModifier));
	}

	private Vector getNewPosition(Vector anchor, Vector initial, Vector goal) {
		float radius = QuickMafs.getDistance(anchor, initial);
		float angle = (float) Math.atan2((initial.y - anchor.y) - (goal.y - anchor.y), (initial.x - anchor.x) - (goal.x - anchor.x));
		angle += angle * directionModifier * elapsedYayTime / (timeTillYayEnd / 2.f);
		return new Vector((float) (anchor.x + radius * Math.cos(angle)), (float) (anchor.y - radius * Math.sin(angle)));
	}

	public void nextYay(float deltaTime) {
		elapsedYayTime += deltaTime;
		if (elapsedYayTime < timeTillYayEnd / 2.f) {
			lElbowPos = getNewPosition(armJointPos, initLElbowPos, lElbowRaisedPos);
			lHandPos = getNewPosition(armJointPos, initLHandPos, lHandRaisedPos);
			rElbowPos = getNewPosition(armJointPos, initRElbowPos, rElbowRaisedPos);
			rHandPos = getNewPosition(armJointPos, initRHandPos, rHandRaisedPos);

		} else if (elapsedYayTime < timeTillYayEnd) {
			lHandPos = getNewPosition(armJointPos, lHandRaisedPos, initLHandPos);
			lElbowPos = getNewPosition(armJointPos, lElbowRaisedPos, initLElbowPos);
			rHandPos = getNewPosition(armJointPos, rHandRaisedPos, initRHandPos);
			rElbowPos = getNewPosition(armJointPos, rElbowRaisedPos, initRElbowPos);
		}

		if (elapsedYayTime > timeTillYayEnd) {  // Insurance for bad math
			this.isYaying = false;
			this.elapsedYayTime = 0.f;

			// Once again : Insurance
			lElbowPos = initLElbowPos; lHandPos = initLHandPos;
			rElbowPos = initRElbowPos; rHandPos = initRHandPos;
		}
	}

	public void triggerYayAnimation() {
		this.isYaying = true;
	}

	public boolean getIsYaying() {
		return this.isYaying;
	}

	public void invertX() {
		directionModifier = directionModifier * -1;
		headPos = headPos.mul(new Vector(-1.f, 1.f));
		armJointPos = armJointPos.mul(new Vector(-1.f, 1.f));
		backPos = backPos.mul(new Vector(-1.f, 1.f));
		lElbowPos = lElbowPos.mul(new Vector(-1.f, 1.f));
		lHandPos = lHandPos.mul(new Vector(-1.f, 1.f));
		rElbowPos = rElbowPos.mul(new Vector(-1.f, 1.f));
		rHandPos = rHandPos.mul(new Vector(-1.f, 1.f));
		lKneePos = lKneePos.mul(new Vector(-1.f, 1.f));
		lFootPos = lFootPos.mul(new Vector(-1.f, 1.f));
		rKneePos = rKneePos.mul(new Vector(-1.f, 1.f));
		rFootPos = rFootPos.mul(new Vector(-1.f, 1.f));

		lElbowRaisedPos = lElbowRaisedPos.mul(new Vector(-1.f, 1.f)); lHandRaisedPos = lHandRaisedPos.mul(new Vector(-1.f, 1.f));
		rElbowRaisedPos = rElbowRaisedPos.mul(new Vector(-1.f, 1.f)); rHandRaisedPos = rHandRaisedPos.mul(new Vector(-1.f, 1.f));
		initLElbowPos = initLElbowPos.mul(new Vector(-1.f, 1.f)); initLHandPos = initLHandPos.mul(new Vector(-1.f, 1.f));
		initRElbowPos = initRElbowPos.mul(new Vector(-1.f, 1.f)); initRHandPos = initRHandPos.mul(new Vector(-1.f, 1.f));
	}

	public void setlFootPos(Vector newPosition) {
		lFootPos = newPosition;
	}

	public void setrFootPos(Vector newPosition) {
		rFootPos = newPosition;
	}
}