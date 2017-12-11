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

import java.util.ArrayList;

public class Rocket extends Weapon {

    private float targetTimer = 0;
    private float maxTimeClignote = 1f;
    private float targetSize = .75f;

    private Vector targetPos;

    private ArrayList<Missile> missils = new ArrayList<>();

    public Rocket(ActorGame game, int rocketNumber, PlayableEntity player) {
        super(game, player, rocketNumber, 1);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        targetTimer += deltaTime;
        if (targetTimer > maxTimeClignote)
            targetTimer = 0;
        if (hasShot()) {
            targetPos = getOwner().getMouse().getPosition();
        }
        for (int i = 0; i < missils.size(); i++) {
            missils.get(i).update(deltaTime);
            if (!missils.get(i).isAlive())
                missils.remove(missils.get(i));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isDeployed()) {
            Image target = canvas.getImage(
                    (targetTimer > maxTimeClignote / 2) ? "res/images/target.1.png" : "res/images/target.2.png");
            canvas.drawImage(target, Transform.I.scaled(targetSize).translated(-targetSize / 2, -targetSize / 2)
                    .translated(getOwner().getMouse().getPosition()), 1, 7331);
        }
        for (Missile m : missils)
            m.draw(canvas);
    }

    @Override
    public void shout() {
        Vector spawn = new Vector((float) (Math.random() * getOwner().getViewScale() * 2 - getOwner().getViewScale()),
                getOwner().getViewScale() + 3).add(getPlayer().getPosition());
        missils.add(new Missile(getOwner(), /*new Vector(0, 7).add(getPlayer().getPosition())*/spawn,
                getOwner().getMouse().getPosition()));
    }

}
