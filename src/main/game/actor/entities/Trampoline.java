package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Linker;
import main.io.Saveable;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class Trampoline implements Actor, Saveable {
	private GenericPlatform genericPlatform;
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

		this.genericPlatform = new GenericPlatform(game, position, width, height);

        this.genericPlatform.setConstraint(Linker.attachWeldilly(game, this.anchor.getEntity(), this.genericPlatform.getEntity(), new Vector(-1, 0),
                0, 2.5f, 0));

    }
	private void create() {

	}

    @Override
    public void update(float deltaTime) {
        this.anchor.update(deltaTime);
        this.genericPlatform.update(deltaTime);
    }
	@Override
	public void reCreate(ActorGame game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		this.anchor.destroy();
		this.genericPlatform.destroy();
		this.game.destroyActor(this);
	}

	@Override
	public Transform getTransform() {
		return Transform.I.translated(this.position);
	}

    @Override
    public void draw(Canvas canvas) {
        this.anchor.draw(canvas);
        this.genericPlatform.draw(canvas);
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
