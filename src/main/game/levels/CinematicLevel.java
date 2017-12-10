package main.game.levels;

import main.game.ActorGame;
import main.window.Canvas;

import java.util.ArrayList;

import static java.awt.event.KeyEvent.VK_SPACE;

/**
 * Created on 12/10/2017 at 11:05 AM.
 */
public abstract class CinematicLevel extends PlayableLevel {
    private ArrayList<CinematicStage> stages = new ArrayList<>();
    private CinematicStage currentStage;
    private int stage;

    protected transient ActorGame game;

    public CinematicLevel(ActorGame game) {
        super(game);
        this.game = game;
        this.stage = 0;
    }

    public ArrayList<CinematicStage> getStages() {
        ArrayList<CinematicStage> temporary = new ArrayList<>(this.stages);
        this.stages.clear();
        return temporary;
    }

    public void nextStage(){
        this.stage += 1;
        this.currentStage = this.stages.get(stage);
    }


    public abstract boolean isFinished();

    @Override
    public void update(float deltaTime) {
        if(this.game.getKeyboard().get(VK_SPACE).isPressed()) {
            nextStage();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.currentStage.draw(canvas);
    }

    public int getStage() {
        return this.stage;
    }
}
