package main.game.actor;

import main.game.ActorGame;

import java.io.Serializable;

public interface Saveable extends Serializable, Actor {

    /**
     * Method used recreate an actor when loaded from a file, ActorGame can't be
     * save and would anyway not be the same in a new game so this method create the
     * Actor using its parameters and an ActorGame given in parameters.
     */
    void reCreate(ActorGame game);
}