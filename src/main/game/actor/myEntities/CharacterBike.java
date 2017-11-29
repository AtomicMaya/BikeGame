package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.QuickMafs;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.window.Canvas;

import java.awt.*;
import java.lang.Math;

public class CharacterBike extends GameEntity {
	private Vector headPos, armJointPos, backPos;
	private Vector lElbowPos, rElbowPos, lHandPos, rHandPos;
	private Vector lKneePos, rKneePos, lFootPos, rFootPos;
	private Polyline body;
	private Circle head;

	private float angle, angleIncrement, directionModifier;
	private ShapeGraphics graphics, graphicsHead;
	private PrismaticConstraint constraint;
	private boolean isYaying;
	private float timeTillYayEnd = 3.f, elapsedYayTime;
	private Vector initLElbowPos = new Vector(.5f, 1.f), initLHandPos = new Vector(1.f, .8f);
	private Vector initRElbowPos = new Vector(.5f, 1.f), initRHandPos = new Vector(1.f, .8f);

	private Vector lElbowRaisedPos = new Vector(0.1f, 2.2f), lHandRaisedPos = new Vector(0.1f, 2.75f);
	private Vector  rElbowRaisedPos = new Vector(-.1f, 2.2f), rHandRaisedPos = new Vector(-.1f, 2.75f);

	public CharacterBike(ActorGame game, Vector position, float angleIncrement) {
		super(game, false, position);
		this.angleIncrement = angleIncrement;
		this.directionModifier = 1.f;
		this.isYaying = true;

		headPos = new Vector(.2f, 1.75f);
		armJointPos = new Vector(.0f, 1.5f);
		backPos = new Vector(-.3f, .5f);
		lElbowPos = initLElbowPos;
		lHandPos = initLHandPos;
		rElbowPos = initRElbowPos;
		rHandPos = initRHandPos;
		lKneePos = new Vector(.2f, .3f);
		lFootPos = new Vector(.1f, -.3f);
		rKneePos = new Vector(.0f, .2f);
		rFootPos = new Vector(-.1f, -.3f);

		Circle anchor = new Circle(0.1f);
		build(this.getEntity(), anchor, -1f, -1, true);

		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .15f, 1.f, 0);

		head = new Circle(.3f, getHeadPos().add(.1f * directionModifier, .1f));
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
	private Polyline generatePolygon() {
		return new Polyline(getHeadPos(), getArmJointPos(), getlElbowPos(), getlHandPos(), getlElbowPos(), getArmJointPos(),
				getrElbowPos(), getrHandPos(), getrElbowPos(), getArmJointPos(), getBackPos(),
				getlKneePos(), getlFootPos(), getlKneePos(), getBackPos(), getrKneePos(), getrFootPos(),
				getrKneePos(), getBackPos(), getArmJointPos());
	}

	public void attach(Entity vehicle, Vector anchor) {
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

	public void detach() {
		if (constraint != null)
			constraint.destroy();
	}

	private float toRadians(float angle) {
		return (float) (angle * Math.PI / 180.f);
	}

	public void nextPedal(int modifier) {
		angle += modifier * angleIncrement;
		angle %= 360;
		float newLFootX = (float) (getlFootPos().x + Math.cos(toRadians(angle)) * 0.01 * -modifier);
		float newLFootY = (float) (getlFootPos().y + Math.sin(toRadians(angle))* 0.01 * -modifier);
		float newRFootX = (float) (getrFootPos().x + Math.cos(toRadians((float) (angle - Math.PI))) * 0.01 * -modifier);
		float newRFootY = (float) (getrFootPos().y + Math.sin(toRadians((float) (angle - Math.PI))) * 0.01 * -modifier);
		setlFootPos(new Vector(newLFootX, newLFootY));
		setrFootPos(new Vector(newRFootX, newRFootY));
		setlKneePos(new Vector((float) (getlKneePos().x + Math.cos(toRadians(angle)) * 0.005 * -modifier),
				(float) (getlKneePos().y + Math.sin(toRadians(angle))* 0.005 * -modifier)));
		setrKneePos(new Vector((float) (getrKneePos().x + Math.cos(toRadians((float) (angle - Math.PI))) * 0.005 * -modifier),
				(float) (getrKneePos().y + Math.sin(toRadians((float) (angle - Math.PI))) * 0.005 * -modifier)));
	}

	private Vector getNewPosition(Vector anchor, Vector initial, Vector now, Vector goal) {
		float angle = now.sub(anchor).dot(goal.sub(anchor));
		System.out.print(angle + ",");
		angle += angle * elapsedYayTime / (timeTillYayEnd / 2.f);
		float radius = QuickMafs.getDistance(anchor, initial);
		return new Vector((float) (anchor.x + radius * Math.cos(angle)), (float) (anchor.y - radius * Math.sin(angle)));
	}

	public void nextYay(int modifier, float deltaTime) {
		elapsedYayTime += deltaTime;
		if (elapsedYayTime < timeTillYayEnd / 2.f) {
			lElbowPos = getNewPosition(armJointPos, initLElbowPos, lElbowPos, lElbowRaisedPos);
			lHandPos = getNewPosition(lElbowPos, initLHandPos, lHandPos, lHandRaisedPos);
			rElbowPos = getNewPosition(armJointPos, initRElbowPos, rElbowPos, rElbowRaisedPos);
			rHandPos = getNewPosition(rElbowPos, initRHandPos, rHandPos, rHandRaisedPos);
			System.out.print("\r\n");

			//System.out.println(lElbowPos.x + "," + lElbowPos.y + "," + lHandPos.x + "," + lHandPos.y + "," + rElbowPos.x + "," + rElbowPos.y + "," + rHandPos.x + "," + rHandPos.y);
			//System.out.println(newVector.x + "," + newVector.y);
		} else if (elapsedYayTime < timeTillYayEnd) {

		}

		if (elapsedYayTime > timeTillYayEnd) {  // Insurance for bad math
			this.isYaying = false;
			setlElbowPos(new Vector(.5f, 1.f));
			setlHandPos(new Vector(1.f, .8f));
			setrElbowPos(new Vector(.5f, 1.f));
			setrHandPos(new Vector(1.f, .8f));
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
		setHeadPos(getHeadPos().mul(new Vector(-1.f, 1.f)));
		setArmJointPos(getArmJointPos().mul(new Vector(-1.f, 1.f)));
		setBackPos(getBackPos().mul(new Vector(-1.f, 1.f)));
		setlElbowPos(getlElbowPos().mul(new Vector(-1.f, 1.f)));
		setlHandPos(getlHandPos().mul(new Vector(-1.f, 1.f)));
		setrElbowPos(getrElbowPos().mul(new Vector(-1.f, 1.f)));
		setrHandPos(getrHandPos().mul(new Vector(-1.f, 1.f)));
		setlKneePos(getlKneePos().mul(new Vector(-1.f, 1.f)));
		setlFootPos(getlFootPos().mul(new Vector(-1.f, 1.f)));
		setrKneePos(getrKneePos().mul(new Vector(-1.f, 1.f)));
		setrFootPos(getrFootPos().mul(new Vector(-1.f, 1.f)));
	}

	private Vector getHeadPos() {
		return headPos;
	}

	private void setHeadPos(Vector headPos) {
		this.headPos = headPos;
	}

	private Vector getArmJointPos() {
		return armJointPos;
	}

	private void setArmJointPos(Vector armJointPos) {
		this.armJointPos = armJointPos;
	}

	private Vector getBackPos() {
		return backPos;
	}

	private void setBackPos(Vector backPos) {
		this.backPos = backPos;
	}

	private Vector getlElbowPos() {
		return lElbowPos;
	}

	private void setlElbowPos(Vector lElbowPos) {
		this.lElbowPos = lElbowPos;
	}

	private Vector getrElbowPos() {
		return rElbowPos;
	}

	private void setrElbowPos(Vector rElbowPos) {
		this.rElbowPos = rElbowPos;
	}

	private Vector getlHandPos() {
		return lHandPos;
	}

	private void setlHandPos(Vector lHandPos) {
		this.lHandPos = lHandPos;
	}

	private Vector getrHandPos() {
		return rHandPos;
	}

	private void setrHandPos(Vector rHandPos) {
		this.rHandPos = rHandPos;
	}

	private Vector getlKneePos() {
		return lKneePos;
	}

	private void setlKneePos(Vector lKneePos) {
		this.lKneePos = lKneePos;
	}

	private Vector getrKneePos() {
		return rKneePos;
	}

	private void setrKneePos(Vector rKneePos) {
		this.rKneePos = rKneePos;
	}

	private Vector getlFootPos() {
		return lFootPos;
	}

	public void setlFootPos(Vector lFootPos) {
		this.lFootPos = lFootPos;
	}

	private Vector getrFootPos() {
		return rFootPos;
	}

	public void setrFootPos(Vector rFootPos) {
		this.rFootPos = rFootPos;
	}
}