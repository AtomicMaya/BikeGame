/**
 * Author: Clément Jeannet Date: 4 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.actorBuilder.*;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/** A {@linkplain Menu} to add a {@linkplain Actor} to the game */
public class ActorMenu extends Menu {

	/** Contains all the buttons of this {@linkplain ActorMenu}. */
	private ArrayList<GraphicalButton> boutons = new ArrayList<>();

	/** Size of a button. */
	private float sizeX = 3f, sizeY = 3f;

	/** Space between the buttons. */
	private float betweenButton = .2f;

	/** Contains all the description of the {@linkplain GraphicalButton}. */
	private ArrayList<Comment> description = new ArrayList<>();

	/** Max and minimum position of this {@linkplain ActorMenu}. */
	private Vector maxPosition = Vector.ZERO, minPosition = Vector.ZERO;

	/** Position of this {@linkplain ActorMenu}. */
	private Vector position = new Vector(20, 0);

	/** Size of this {@linkplain ActorMenu}. */
	float width, height;

	/** Text of this {@linkplain ActorMenu}. */
	private final String text = "Actors menu";

	/** Font site of this {@linkplain ActorMenu}. */
	private final float fontSize = 1;

	/** Number of buttons/line */
	private int nbButonLine = 3;

	/**
	 * Create a new {@linkplain ActorMenu}
	 * @param game {@linkplain ActorGame} where this {@linkplain ActorMenu} belong
	 * @param levelEditor {@linkplain LevelEditor} where this {@linkplain ActorMenu} will create new {@linkplain ActorBuilder}
	 * @param backgroundColor COlor of the back ground
	 */
	public ActorMenu(ActorGame game, LevelEditor levelEditor, Color backgroundColor) {
		super(game, Vector.ZERO, false);

		this.setAnchor(position);

		// crate pos 0,0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).setNewGraphics("res/images/box.4.png", "res/images/box.4.png");
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new CrateBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a crate"));

		// ground pos 1, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addGround();
			changeStatus();
		});
		description.add(new Comment(game, "Add or edit the Ground"));

		// spawn
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addSpawn(new SpawnBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add or edit the spawnpoint"));

		// checkpoint
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new CheckpointBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a checkpoint"));

		// bike
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addFinish(new FinishBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add or edit the finish point"));

		// coin
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new CoinBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a coin"));

		// mouving platform
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new PlatformBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a platform"));

		// trampoline builder
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new TrampolineBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a trampoline"));

		// fluid
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new LiquidBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a fluid"));

		// laser
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new LaserBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Create a laser"));

		// mine
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new MineBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Create a mine"));

		// boum barrel
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new BoomBarrelBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Create a barrel"));

		// pandule
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new PenduleBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Create a pendulum"));

		// adjust size
		for (GraphicalButton gb : boutons) {
			gb.forceShape(width - betweenButton, height - betweenButton);
			gb.setDepth(1337);
		}
		// adjust position
		for (int i = 0; i < boutons.size(); i++) {
			boutons.get(i).setAnchor(new Vector((i % nbButonLine) * (sizeX + betweenButton),
					-sizeY - i / nbButonLine * (sizeY + betweenButton)).add(this.getAnchor()));
		}
		for (int i = 0; i < description.size(); i++) {
			description.get(i).setParent(boutons.get(i));
			description.get(i).setAnchor(new Vector(-10, 0));

		}
		// compute size of this menu
		minPosition = new Vector(-betweenButton, betweenButton);
		maxPosition = new Vector(nbButonLine * (sizeX + betweenButton),
				-(sizeY + betweenButton) * ((float) Math.ceil(boutons.size() / nbButonLine) + 1)).add(betweenButton,
						-betweenButton);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (getOwner().getKeyboard().get(KeyEvent.VK_M).isPressed())
			changeStatus();
		else if (isRightPressed() && (!isHovered() || !isOpen()))
			this.setStatus(true);

		else if ((isLeftPressed() && !isHovered()) || getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed())
			this.setStatus(false);

		if (isOpen()) {
			for (GraphicalButton gb : boutons) {
				gb.update(deltaTime, zoom);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (isOpen()) {
			canvas.drawShape(ExtendedMath.createRectangle(minPosition.sub(0, -3), minPosition.add(maxPosition)),
					getTransform(), Color.LIGHT_GRAY, Color.LIGHT_GRAY, .1f * getZoom(), 1, 1335);

			for (int i = 0; i < boutons.size(); i++) {
				boutons.get(i).draw(canvas);
				if (boutons.get(i).isHovered())
					description.get(i).draw(canvas);
			}

			canvas.drawText(text, fontSize, getTransform().translated(new Vector(maxPosition.x / 2, 2).mul(getZoom())),
					Color.BLACK, Color.BLACK, .02f, false, false, new Vector(.5f, 0), 1, 1336);
		}
	}

	@Override
	public void destroy() {
		for (GraphicalButton gb : this.boutons)
			gb.destroy();
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(getPosition().add(minPosition), getPosition().add(maxPosition),
				getMousePosition());
	}
}
