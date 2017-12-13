/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Image;

/** A Rocket to be fired by the {@linkplain PlayableEntity}. */
public class Rocket extends Weapon {
    
	/** The reference to how long ago the weapon was fired. */
    private float elapsedTime = 0;

    /** How long the missile will blink. */
    private float maxTimeBlink = 1f;

    /** The size of the target. */
    private float targetSize = .75f;

    /**
     * Creates a new {@linkplain Rocket}.
     * @param game The master {@linkplain ActorGame}.
     * @param rocketAmmo The number of shots one can fire.
     * @param player The {@linkplain PlayableEntity}.
     */
    public Rocket(ActorGame game, int rocketAmmo, PlayableEntity player) {
        super(game, player, rocketAmmo, 1);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.elapsedTime += deltaTime;
        if (this.elapsedTime > this.maxTimeBlink)
            this.elapsedTime = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isDeployed()) {		   
        	// drawing of the target red cross
            Image target = canvas.getImage(
                    (this.elapsedTime > this.maxTimeBlink / 2) ? "res/images/target.1.png" : "res/images/target.2.png");
            canvas.drawImage(target, Transform.I.scaled(this.targetSize).translated(-this.targetSize / 2, -this.targetSize / 2)
                    .translated(getOwner().getMouse().getPosition()), 1, 7331);
        }
    }

    @Override
    public void shoot() {
        Vector spawn = new Vector((float) (Math.random() * this.getOwner().getViewScale() * 2 - this.getOwner().getViewScale()),
        		this.getOwner().getViewScale() + 3).add(this.getPlayer().getPosition());
        this.getOwner().addActor(new Missile(this.getOwner(), spawn, this.getOwner().getMouse().getPosition()));
    }

}
