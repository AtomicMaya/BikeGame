package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.PlayableEntity;
import main.math.Impact;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.List;

/** {@linkplain PortableWeapon} of type shotgun */
public class Shotgun extends PortableWeapon {

	// graphical parameters
	private float laserDistance = 16;
	private String imagePath = "./res/images/shotgun.png";

	/** Create a new shotgun */
	public Shotgun(ActorGame game, int initialAmmoCount, PlayableEntity player) {
		super(game, player, initialAmmoCount, 2f);

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (isDeployed()) {
			canvas.drawImage(canvas.getImage(imagePath),
					Transform.I.scaled(2, .5f * ((isShoutingOnTheRight()) ? 1 : -1)).translated(getPosition())
							.rotated(getAngle(), getPosition()),
					1, 5);

			Vector corection = new Vector(
					(float) (Math.sin(-getAngle() + Math.PI / 2 + Math.PI / 14 * (isShoutingOnTheRight() ? -1 : 1))),
					(float) (Math.cos(-getAngle() + Math.PI / 2 + Math.PI / 14 * (isShoutingOnTheRight() ? -1 : 1))))
							.mul(-1);
			canvas.drawShape(
					new Polyline(getPosition().add(corection.mul(-2.1f)),
							getPosition().add(getDirection().mul(-laserDistance))),
					Transform.I, Color.GREEN, Color.GREEN, .05f, 1, -12);
		}
	}

	@Override
	public void shout() {
		List<Impact> impacts = getOwner().getImpacts(this.getPosition(),
				this.getPosition().add(this.getDirection().mul(-this.laserDistance)));

		for (Impact i : impacts) {
			int group = i.getPart().getCollisionGroup();

			if (group == ObjectGroup.CHECKPOINT.group || group == ObjectGroup.FINISH.group
					|| group == ObjectGroup.SENSOR.group) {

			} else if (group == ObjectGroup.OBSTACLE.group || group == ObjectGroup.TERRAIN.group) {
				break;
			} else if (group == ObjectGroup.ENEMY.group) {
				i.getPart().getEntity().destroy();
				break;
			}
		}
	}

}
