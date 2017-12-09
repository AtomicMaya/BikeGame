package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Linker;
import main.io.Saveable;
import main.math.Transform;
import main.math.Vector;

public class Trampoline implements Actor, Saveable {
	private TrampolinePlatform trampolinePlatform;
	private AnchorPoint anchor;
	private ActorGame game;
	private Vector position;

	public Trampoline(ActorGame game, Vector position, float width, float height) {
		this.game = game;
		this.position = position;

		if (width == -1)
			width = 5;
		if (height == -1)
			height = 1;

		this.anchor = new AnchorPoint(game, position);

		this.trampolinePlatform = new TrampolinePlatform(game, position, width, height);

		this.trampolinePlatform.setLeftConstraint(Linker.attachWeldilly(game, this.anchor.getEntity(),
				this.trampolinePlatform.getEntity(), new Vector(-1, 0), 0, 2.5f, 0));

//		game.addActor(this.anchor);
//		game.addActor(this.trampolinePlatform);
	}

	private void create() {

	}

	@Override
	public void reCreate(ActorGame game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float deltaTime) {
		this.anchor.update(deltaTime);
		this.trampolinePlatform.update(deltaTime);
	}

	@Override
	public void destroy() {
		this.anchor.destroy();
		this.trampolinePlatform.destroy();
		this.game.destroyActor(this);
	}

	@Override
	public Transform getTransform() {
		return Transform.I.translated(this.position);
	}

	@Override
	public Vector getPosition() {
		return this.position;
	}

	@Override
	public Vector getVelocity() {
		return Vector.ZERO;
	}

	public void setPosition(Vector newPosition) {
		this.position = newPosition;
	}

}
