package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

public class CharacterBike extends GameEntity {

	private Vector headPosition, armJointPosition, backPosition;
	private Vector leftElbowPosition, rightElbowPosition, leftHandPosition, rightHandPosition;
	private Vector leftKneePosition, rightKneePosition, leftFootPosition, rightFootPosition;
	private Polyline body;
	private Circle anchor;
	private Circle head;
	private float directionModifier;

	private float angle, angleIncrement;
	private ShapeGraphics graphics, graphicsHead;
	private Constraint constraint;

	public CharacterBike(ActorGame game, Vector position, float angleIncrement) {
		super(game, false, position);
		this.angleIncrement = angleIncrement;
		this.directionModifier = 1.f;

		setHeadPosition(new Vector(.2f, 1.75f));
		setArmJointPosition(new Vector(.0f, 1.5f));
		setBackPosition(new Vector(-.3f, .5f));
		setLeftElbowPosition(new Vector(.5f, 1.f));
		setLeftHandPosition(new Vector(1.f, .8f));
		setRightElbowPosition(new Vector(.5f, 1.f));
		setRightHandPosition(new Vector(1.f, .8f));
		setLeftKneePosition(new Vector(.2f, .3f));
		setLeftFootPosition(new Vector(.1f, -.3f));
		setRightKneePosition(new Vector(.0f, .2f));
		setRightFootPosition(new Vector(-.1f, -.3f));

		anchor = new Circle(0.1f);
		build(this.getEntity(), anchor, -1f, -1, true);

		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .15f, 1.f, 0);

		head = new Circle(.3f, getHeadPosition().add(.1f * directionModifier, .1f));
		graphicsHead = addGraphics(this.getEntity(), head, Color.WHITE, Color.BLACK, .15f, 1.f, 0);
	}

	private Polyline generatePolygon() {
		return new Polyline(getHeadPosition(), getArmJointPosition(), getLeftElbowPosition(), getLeftHandPosition(), getLeftElbowPosition(), getArmJointPosition(),
				getRightElbowPosition(), getRightHandPosition(), getRightElbowPosition(), getArmJointPosition(), getBackPosition(),
				getLeftKneePosition(), getLeftFootPosition(), getLeftKneePosition(), getBackPosition(), getRightKneePosition(), getRightFootPosition(),
				getRightKneePosition(), getBackPosition(), getArmJointPosition());
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

	public void nextPedal(int modifier) {
		angle += modifier * angleIncrement;
		angle %= 360;
		float newLFootX = (float) (getLeftFootPosition().x + Math.cos(toRadians(angle)) * 0.01 * -modifier);
		float newLFootY = (float) (getLeftFootPosition().y + Math.sin(toRadians(angle))* 0.01 * -modifier);
		float newRFootX = (float) (getRightFootPosition().x + Math.cos(toRadians((float) (angle - Math.PI))) * 0.01 * -modifier);
		float newRFootY = (float) (getRightFootPosition().y + Math.sin(toRadians((float) (angle - Math.PI))) * 0.01 * -modifier);
		setLeftFootPosition(new Vector(newLFootX, newLFootY));
		setRightFootPosition(new Vector(newRFootX, newRFootY));
		setLeftKneePosition(new Vector((float) (getLeftKneePosition().x + Math.cos(toRadians(angle)) * 0.005 * -modifier),
				(float) (getLeftKneePosition().y + Math.sin(toRadians(angle))* 0.005 * -modifier)));
		setRightKneePosition(new Vector((float) (getRightKneePosition().x + Math.cos(toRadians((float) (angle - Math.PI))) * 0.005 * -modifier),
				(float) (getRightKneePosition().y + Math.sin(toRadians((float) (angle - Math.PI))) * 0.005 * -modifier)));
	}

	private float toRadians(float angle) {
		return (float) (angle * Math.PI / 180.f);
	}

	@Override
	public void update(float deltaTime) {
		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .15f, 1.f, 0);
		head = new Circle(.3f, getHeadPosition().add(.1f * directionModifier, .1f));
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


	public void invertX() {
		directionModifier = directionModifier * -1;
		setHeadPosition(getHeadPosition().mul(new Vector(-1.f, 1.f)));
		setArmJointPosition(getArmJointPosition().mul(new Vector(-1.f, 1.f)));
		setBackPosition(getBackPosition().mul(new Vector(-1.f, 1.f)));
		setLeftElbowPosition(getLeftElbowPosition().mul(new Vector(-1.f, 1.f)));
		setLeftHandPosition(getLeftHandPosition().mul(new Vector(-1.f, 1.f)));
		setRightElbowPosition(getRightElbowPosition().mul(new Vector(-1.f, 1.f)));
		setRightHandPosition(getRightHandPosition().mul(new Vector(-1.f, 1.f)));
		setLeftKneePosition(getLeftKneePosition().mul(new Vector(-1.f, 1.f)));
		setLeftFootPosition(getLeftFootPosition().mul(new Vector(-1.f, 1.f)));
		setRightKneePosition(getRightKneePosition().mul(new Vector(-1.f, 1.f)));
		setRightFootPosition(getRightFootPosition().mul(new Vector(-1.f, 1.f)));
	}

	private Vector getHeadPosition() {
		return headPosition;
	}

	private void setHeadPosition(Vector headPosition) {
		this.headPosition = headPosition;
	}

	private Vector getArmJointPosition() {
		return armJointPosition;
	}

	private void setArmJointPosition(Vector armJointPosition) {
		this.armJointPosition = armJointPosition;
	}

	private Vector getBackPosition() {
		return backPosition;
	}

	private void setBackPosition(Vector backPosition) {
		this.backPosition = backPosition;
	}

	private Vector getLeftElbowPosition() {
		return leftElbowPosition;
	}

	private void setLeftElbowPosition(Vector leftElbowPosition) {
		this.leftElbowPosition = leftElbowPosition;
	}

	private Vector getRightElbowPosition() {
		return rightElbowPosition;
	}

	private void setRightElbowPosition(Vector rightElbowPosition) {
		this.rightElbowPosition = rightElbowPosition;
	}

	private Vector getLeftHandPosition() {
		return leftHandPosition;
	}

	private void setLeftHandPosition(Vector leftHandPosition) {
		this.leftHandPosition = leftHandPosition;
	}

	private Vector getRightHandPosition() {
		return rightHandPosition;
	}

	private void setRightHandPosition(Vector rightHandPosition) {
		this.rightHandPosition = rightHandPosition;
	}

	private Vector getLeftKneePosition() {
		return leftKneePosition;
	}

	private void setLeftKneePosition(Vector leftKneePosition) {
		this.leftKneePosition = leftKneePosition;
	}

	private Vector getRightKneePosition() {
		return rightKneePosition;
	}

	private void setRightKneePosition(Vector rightKneePosition) {
		this.rightKneePosition = rightKneePosition;
	}

	private Vector getLeftFootPosition() {
		return leftFootPosition;
	}

	public void setLeftFootPosition(Vector leftFootPosition) {
		this.leftFootPosition = leftFootPosition;
	}

	private Vector getRightFootPosition() {
		return rightFootPosition;
	}

	public void setRightFootPosition(Vector rightFootPosition) {
		this.rightFootPosition = rightFootPosition;
	}
}
