package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Linker;
import main.io.Saveable;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class Trampoline implements Actor, Saveable {

	private static final long serialVersionUID = 1927654389208717735L;

	private transient TrampolinePlatform trampolinePlatform;
	private transient AnchorPoint anchor;
	private transient ActorGame game;
	private Vector position;
	private float width, height;

	public Trampoline(ActorGame game, Vector position, float width, float height) {
		this.game = game;
		this.position = position;
		this.width = width;
		this.height = height;

		create();
	}

	private void create() {
		this.width = this.width < 0 ? 5 : this.width;
		this.height = this.height < 0 ? 1 : this.height;
		this.anchor = new AnchorPoint(game, position);

		this.trampolinePlatform = new TrampolinePlatform(game, position.add(1, 0), width, height);

		this.trampolinePlatform.setLeftConstraint(Linker.attachWeldilly(game, this.anchor.getEntity(),
				this.trampolinePlatform.getEntity(), new Vector(-1, 0), 0, 2.5f, 0));
	}

	@Override
	public void reCreate(ActorGame game) {
		this.game = game;
		create();
	}

	@Override
	public void draw(Canvas canvas) {
		this.anchor.draw(canvas);
		this.trampolinePlatform.draw(canvas);
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
		this.anchor.setPosition(newPosition);
		this.trampolinePlatform.setPosition(newPosition.add(1,0));
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;

		this.trampolinePlatform.setSize(width, height);
	}

}
