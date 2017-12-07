package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.math.Circle;
import main.math.Vector;
import main.window.Canvas;

public class Trampoline extends GameEntity {
    private TrampolinePlatform trampolinePlatform;
    private ImageGraphics graphics;

    public Trampoline(ActorGame game, Vector position, float width, float height) {
        super(game, true, position);
        if (width == -1) width = 5;
        if (height == -1) height = 1;

        this.graphics = this.addGraphics("./res/images/stone.hollow.11.png", 1, 1, new Vector(1, 0));

        this.trampolinePlatform = new TrampolinePlatform(game, position, width, height);
        this.build(new Circle(1), -1f, -1, false);

        this.trampolinePlatform.setConstraint(Linker.attachWeldilly(game, this.getEntity(), this.trampolinePlatform.getEntity(), Vector.ZERO,
                (float) 0, 3, .5f));

        game.addActor(this.trampolinePlatform);
    }

    @Override
    public void update(float deltaTime) {
        this.trampolinePlatform.update(deltaTime);
    }

    @Override
    public void destroy() {
        this.trampolinePlatform.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.draw(canvas);
        this.trampolinePlatform.draw(canvas);
    }
}
