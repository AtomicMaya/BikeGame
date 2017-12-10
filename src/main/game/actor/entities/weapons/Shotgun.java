package main.game.actor.entities.weapons;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.PlayableEntity;
import main.math.Impact;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/** {@linkplain Weapon} of type shotgun */
public class Shotgun implements Weapon {

	private ActorGame game;

	// Shooting related parameters
	private boolean deployed = false;
	private int ammoCount;
	private boolean shooting = false;
	private float elapsedTime = 0, shotgunOutTime = .5f;
	private Vector position;

	// Direction of shot
	private Vector direction;

	// Graphical parameters
	private float laserDistance = 10;
	private String imagePath = "./res/images/shotgun.png";
	private boolean lookRight;
	private float angle = 0;

	// The Owner
	private PlayableEntity player;

	// GUI elements
	private Comment display;
	private String ammoText = " Shots";
	private Vector displayPosition = new Vector(17, 9);

	/** Creates a new shotgun */
	public Shotgun(ActorGame game, int initialAmmoCount, PlayableEntity player) {
		// super(game, false, position);
		this.lookRight = player.isLookingRight();
		this.position = player.getPosition().add(this.lookRight ? 1 : -1, 2);
		this.game = game;
		this.player = player;
		this.ammoCount = initialAmmoCount;

		this.direction = direction(this.position, game.getMouse().getPosition());
		this.display = new Comment(game, this.ammoCount + this.ammoText);
		this.display.setAnchor(this.displayPosition);
	}

	/** Compute the direction vector */
	private Vector direction(Vector un, Vector deux) {
		return new Vector(un.x - deux.x, un.y - deux.y).normalized();
	}

	@Override
	public void update(float deltaTime) {
		if (this.deployed && this.game.getMouse().getLeftButton().isPressed()) {
			fireWeapon();
		}
		if (this.game.getKeyboard().get(KeyEvent.VK_F).isPressed())
			this.deployed = !this.deployed;

		if (this.deployed) {
			Vector playerPos = this.player.getPosition();
			this.position = playerPos.add(this.lookRight ? 1 : -1, 2);

			this.direction = direction(playerPos, this.game.getMouse().getPosition());
			this.lookRight = this.direction.x <= 0;

			angle = direction.rotated(Math.PI).getAngle();
			// System.out.println(angle);
			if (angle > Math.PI / 4 && angle <= Math.PI / 2)
				angle = (float) (Math.PI / 4);
			else if (angle > Math.PI / 2 && angle < Math.PI * 3 / 4f)
				angle = (float) Math.PI * 3 / 4f;
			else if (angle < -Math.PI / 4 && angle >= -Math.PI / 2)
				angle = (float) (-Math.PI / 4);
			else if (angle < -Math.PI / 2 && angle > -Math.PI * 3 / 4f)
				angle = (float) -Math.PI * 3 / 4f;

			this.direction = new Vector((float) (Math.sin(-this.angle + Math.PI / 2)),
					(float) (Math.cos(-this.angle + Math.PI / 2))).mul(-1);

			if (this.shooting)
				this.elapsedTime += deltaTime;
			if (this.elapsedTime > this.shotgunOutTime) {
				this.elapsedTime = 0;
				this.shooting = false;
			}

			this.display.update(deltaTime, this.game.getViewScale() / 20f);
			if (shooting) {
				List<Impact> impacts = this.game.getImpacts(this.position,
						this.position.add(this.direction.mul(-this.laserDistance)));
				if (!impacts.isEmpty() && impacts.get(0).getPart().getCollisionGroup() == ObjectGroup.ENEMY.group)
					impacts.get(0).getPart().getEntity().destroy();
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (this.deployed) {
			canvas.drawImage(canvas.getImage(imagePath),
					Transform.I.scaled(2, .5f * ((lookRight) ? 1 : -1)).translated(position).rotated(angle, position),
					1, 5);

			Vector correction = new Vector(
					(float) (Math.sin(-angle + Math.PI / 2 + Math.PI / 14 * (lookRight ? -1 : 1))),
					(float) (Math.cos(-angle + Math.PI / 2 + Math.PI / 14 * (lookRight ? -1 : 1)))).mul(-1);
			canvas.drawShape(
					new Polyline(position.add(correction.mul(-2.1f)), position.add(direction.mul(-laserDistance))),
					Transform.I, Color.GREEN, Color.GREEN, .05f, .3f, -12);

			this.display.draw(canvas);
		}
	}

	@Override
	public void fireWeapon() {
		if (this.ammoCount > 0) {
			this.ammoCount -= 1;
			this.shooting = true;
			this.display.setText(this.ammoCount + this.ammoText);
		}

	}

	@Override
	public void addAmmo(int quantity) {
		this.ammoCount += quantity;
		this.display.setText(this.ammoCount + this.ammoText);
	}

	@Override
	public Vector getPosition() {
		return this.position;
	}

	@Override
	public void destroy() {
		this.game.destroyActor(this);
	}

	@Override
	public Transform getTransform() {
		return player.getTransform();
	}

	@Override
	public Vector getVelocity() {
		return player.getVelocity();
	}
}
