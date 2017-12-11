package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.StartCheckpoint;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CheckpointBuilder extends ActorBuilder {

	private Checkpoint checkpoint;
	private Vector position;
	private ActorGame game;

	// number field stuff
	protected NumberField height, width;
	private Vector heightNumberFieldPos = new Vector(26, 6), widthNumberFieldPos = new Vector(26, 8);
	private Comment heightComment, widthComments;

	private boolean isDone = false;
	private boolean hover = false;
	private boolean placed = false;

	public CheckpointBuilder(ActorGame game) {
		super(game);
		this.game = game;
		this.checkpoint = new Checkpoint(game, getFlooredMousePosition());

		height = new NumberField(game, heightNumberFieldPos, 3, 1, 10);

		heightComment = new Comment(game, "Area of trigger height");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		width = new NumberField(game, widthNumberFieldPos, 3, 1, 1);

		widthComments = new Comment(game, "Area of trigger width");
		widthComments.setParent(width);
		widthComments.setAnchor(new Vector(-6, 0));

		position = getFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			checkpoint.setPosition(position);

		} else if (hover && isRightPressed())
			placed = false;
		if (!isDone) {

			height.update(deltaTime, zoom);
			width.update(deltaTime, zoom);

			heightComment.update(deltaTime, zoom);
			widthComments.update(deltaTime, zoom);

			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				checkpoint.setSize(width.getNumber(), height.getNumber());
				isDone = true;
			}
		}
		hover = ExtendedMath.isInRectangle(position, position.add(1, 1), game.getMouse().getPosition());

		if (hover && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		checkpoint.draw(canvas);

		if (!isDone()) {
			height.draw(canvas);
			if (height.isHovered())
				heightComment.draw(canvas);
			width.draw(canvas);
			if (width.isHovered())
				widthComments.draw(canvas);
			canvas.drawShape(ExtendedMath.createRectangle(width.getNumber(), height.getNumber()),
					Transform.I.translated(position), new Color(0, 255, 255), Color.BLACK, .02f, .6f, 1337);
		}
	}

	@Override
	public Actor getActor() {
		return checkpoint;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		checkpoint.destroy();
		checkpoint = new StartCheckpoint(game, position, null);
		checkpoint.setSize(width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		this.checkpoint.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}
}
