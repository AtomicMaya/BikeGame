package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ShapeGraphics;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.util.ArrayList;

import static main.math.ExtendedMath.*;

/** Represents the character and its animation, which we deemed too verbose to leave in the {@linkplain Bike} class. */

public class CharacterBike extends GameEntity {
    /** {@linkplain Vector}s representing the position of the head, the arm joint, the back, both arms,
     * both hands, both legs and both feet. */
	private Vector headPos, armJointPos, backPos,
            lElbowPos, rElbowPos, lHandPos,
            rHandPos, lKneePos, rKneePos,
            lFootPos, rFootPos;

	/** A {@linkplain Float} containing the direction modifier. */
	private float directionModifier;

	/** An {@linkplain ArrayList} of {@linkplain ShapeGraphics} containing the Graphics associated to all the limbs. */
	private ArrayList<ShapeGraphics> graphics;

	/** The linked {@linkplain PrismaticConstraint}. */
	private PrismaticConstraint constraint;

	/** Whether this {@linkplain CharacterBike} is happy. */
	private boolean isHappy;

	/** Maximum animation time. Default {@value} */
	private final float timeTillHappinessEnd = 1.5f;

	/** Current animation time. */
	private float elapsedHappinessTime;

    /** Insurance {@linkplain Vector}s. */
	private Vector initLElbowPos = new Vector(.5f, 1.f), initLHandPos = new Vector(1.f, .8f),
            initRElbowPos = new Vector(.5f, 1.f), initRHandPos = new Vector(1.f, .8f),
            lElbowRaisedPos = new Vector(.4f, 2.2f), lHandRaisedPos = new Vector(.4f, 2.75f),
            rElbowRaisedPos = new Vector(.2f, 2.2f), rHandRaisedPos = new Vector(.2f, 2.75f);

	/**
	 * Initialize a {@linkplain CharacterBike}
	 * @param game The {@linkplain ActorGame} in which this {@linkplain CharacterBike} exists.
	 * @param position The position {@linkplain Vector} of this {@linkplain CharacterBike}.
	 */
	public CharacterBike(ActorGame game, Vector position) {
		super(game, false, position);
		this.directionModifier = 1.f;

        this.headPos = new Vector(.2f, 1.75f); this.armJointPos = new Vector(.0f, 1.5f); this.backPos = new Vector(-.3f, .5f);
        this.lElbowPos = this.initLElbowPos; this.lHandPos = this.initLHandPos;
        this.rElbowPos = this.initRElbowPos; this.rHandPos = this.initRHandPos;
        this.lKneePos = new Vector(.2f, .3f); this.lFootPos = new Vector(.1f, -.3f);
        this.rKneePos = new Vector(.0f, .2f); this.rFootPos = new Vector(-.1f, -.3f);

        this.updateGraphics();
		this.build(new Circle(0.1f), -1, -1, false, ObjectGroup.PLAYER.group);
	}

	private void updateGraphics() {
        Circle head = new Circle(.35f, this.headPos.add(.1f * this.directionModifier, .1f));
        Polygon body = new Polygon(this.headPos, this.armJointPos, this.backPos);
        this.graphics = new ArrayList<>();
        this.graphics.add(this.addGraphics(head, Color.decode("#f5c396"), Color.BLACK, .02f, 1.f, 10.1f));
        this.graphics.add(this.addGraphics(body, null, Color.BLACK, .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(0), null, Color.BLACK, .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(1), null, Color.BLACK, .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(2), null, Color.BLUE.brighter().brighter(), .15f, 1.f, getDirectionModifier() == 1 ? .9f : 1.1f));
        this.graphics.add(this.addGraphics(this.generateLimb(3), null, Color.BLUE.brighter().brighter(), .15f, 1.f, getDirectionModifier() == 1 ? 1.1f : .9f));
    }

	@Override
	public void update(float deltaTime) {
		if (this.isHappy) nextYay(deltaTime);
		this.updateGraphics();
	}

	@Override
	public void draw(Canvas canvas) {
	    for(ShapeGraphics graphics : this.graphics) {
	        graphics.draw(canvas);
        }
	}
	
	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	/**
	 * Generates the shape of the body given all positions.
	 * @return a {@linkplain Polyline}
	 */
	private Polyline generateLimb(int limb) {
        switch (limb) {
            case 0:     // Left arm
                return new Polyline(this.armJointPos, this.lElbowPos, this.lHandPos);
            case 1:     // Right arm
                return new Polyline(this.armJointPos, this.rElbowPos, this.rHandPos);
            case 2:     // Left leg
                return new Polyline(this.backPos, this.lKneePos, this.lFootPos, this.lKneePos, this.backPos);
            case 3:     // Right leg
                return new Polyline(this.backPos, this.rKneePos, this.rFootPos);
            default:
                return null;
        }
	}

	/** Removes the constraint. */
	public void detach() {
		if (this.constraint != null) this.constraint.destroy();
	}

	/** Calculate the next position {@linkplain Vector} of the feet and the knees. */
	public void nextPedal(float angle) {
        this.lFootPos = new Vector( (float) (Math.cos(toRadians(angle)) * 0.4 + 0.15f) * this.directionModifier,
                (float) (Math.sin(toRadians(angle)) * 0.4 - 0.25f));
        this.rFootPos = new Vector((float) (Math.cos(toRadians(angle) - Math.PI) * 0.4 + 0.15f) * this.directionModifier,
                (float) (Math.sin(toRadians(angle) - Math.PI) * 0.4 - 0.25f));
        this.lKneePos = new Vector(this.lFootPos.x , (this.lFootPos.y + 0.5f));
        this.rKneePos = new Vector(this.rFootPos.x , (this.rFootPos.y + 0.5f));
    }

	/**
	 * Calculates the coordinates of the next {@linkplain Vector} relative to time.
	 * @param anchor The position {@linkplain Vector} to which this point is 'attached', can move.
	 * @param initial The absolute initial position that this {@linkplain Vector} occupied.
	 * @param goal The destination {@linkplain Vector}.
	 * @return the new {@linkplain Vector}.
	 */
	private Vector getNewPosition(Vector anchor, Vector initial, Vector goal) {
		float radius = ExtendedMath.getDistance(anchor, initial);
		float angle = ExtendedMath.getAngle(anchor, initial, goal);
		angle += angle * this.directionModifier * this.elapsedHappinessTime / (this.timeTillHappinessEnd / 2.f);
		return new Vector((float) (anchor.x - radius * Math.cos(angle)), (float) (anchor.y - radius * Math.sin(angle)));
	}

	/**
	 * Calculates all the next positions and assigns them to the elbows and hands.
	 * @param deltaTime The current deltaTime, so that we can use time as a parameter of the animation
	 */
	private void nextYay(float deltaTime) {
		this.elapsedHappinessTime += deltaTime;
		if (this.elapsedHappinessTime < this.timeTillHappinessEnd / 2.f) {
			this.lElbowPos = getNewPosition(this.armJointPos, this.initLElbowPos, this.lElbowRaisedPos);
			this.lHandPos = getNewPosition(this.armJointPos, this.initLHandPos, this.lHandRaisedPos);
			this.rElbowPos = getNewPosition(this.armJointPos, this.initRElbowPos, this.rElbowRaisedPos);
			this.rHandPos = getNewPosition(this.armJointPos, this.initRHandPos, this.rHandRaisedPos);
		} else if (this.elapsedHappinessTime < this.timeTillHappinessEnd) {
            this.lElbowPos = getNewPosition(this.armJointPos, this.lElbowRaisedPos, this.initLElbowPos);
            this.lHandPos = getNewPosition(this.armJointPos, this.lHandRaisedPos, this.initLHandPos);
            this.rElbowPos = getNewPosition(this.armJointPos, this.rElbowRaisedPos, this.initRElbowPos);
            this.rHandPos = getNewPosition(this.armJointPos, this.rHandRaisedPos, this.initRHandPos);
		}

		if (this.elapsedHappinessTime > this.timeTillHappinessEnd) {
			this.isHappy = false;
			this.elapsedHappinessTime = 0.f;

			// Insurance for bad math.
			this.lElbowPos = this.initLElbowPos; this.lHandPos = this.initLHandPos;
			this.rElbowPos = this.initRElbowPos; this.rHandPos = this.initRHandPos;
		}
	}

	/** Trigger the Victory animation */
	public void triggerHappiness() {
		this.isHappy = true;
	}

	/** @return whether the Victory animation is active. */
	public boolean getHappiness() {
		return this.isHappy;
	}

	/** Inverts all the xCoordinates on the x axis. */
	public void invertX() {
        this.directionModifier = this.directionModifier * -1;
        this.headPos = this.headPos.mul(xInverted); this.armJointPos = this.armJointPos.mul(xInverted);
        this.backPos = this.backPos.mul(xInverted);
        this.lElbowPos = this.lElbowPos.mul(xInverted); this.lHandPos = this.lHandPos.mul(xInverted);
        this.rElbowPos = this.rElbowPos.mul(xInverted); this.rHandPos = this.rHandPos.mul(xInverted);

		// Insurance against bad math...
        this.lKneePos = new Vector(.2f, .3f).mul(this.directionModifier == 1 ? xyNormal : xInverted);
        this.lFootPos = new Vector(.1f, -.3f).mul(this.directionModifier == 1 ? xyNormal : xInverted);
        this.rKneePos = new Vector(.0f, .2f).mul(this.directionModifier == 1 ? xyNormal : xInverted);
        this.rFootPos = new Vector(-.1f, -.3f).mul(this.directionModifier == 1 ? xyNormal : xInverted);

        this.lElbowRaisedPos = this.lElbowRaisedPos.mul(xInverted); this.lHandRaisedPos = this.lHandRaisedPos.mul(xInverted);
        this.rElbowRaisedPos = this.rElbowRaisedPos.mul(xInverted); this.rHandRaisedPos = this.rHandRaisedPos.mul(xInverted);
        this.initLElbowPos = this.initLElbowPos.mul(xInverted); this.initLHandPos = this.initLHandPos.mul(xInverted);
        this.initRElbowPos = this.initRElbowPos.mul(xInverted); this.initRHandPos = this.initRHandPos.mul(xInverted);
	}

	/** @return the current direction modifier. */
    private float getDirectionModifier() {
        return this.directionModifier;
    }

    /**
     * Sets a new {@linkplain PrismaticConstraint} to this {@linkplain CharacterBike}.
     * @param constraint The linked {@linkplain PrismaticConstraint}.
     */
    public void setConstraint(PrismaticConstraint constraint) {
        this.constraint = constraint;
    }
}