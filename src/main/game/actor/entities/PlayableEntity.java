package main.game.actor.entities;

/**
 * Created on 12/7/2017 at 10:47 PM.
 */
public interface PlayableEntity  {
    void triggerDeath(boolean wasGravity);
    void triggerVictory();
    void triggerCheckpoint();

    boolean getDeathStatus();
    boolean getVictoryStatus();
    boolean getIfWasKilledByGravity();
}
