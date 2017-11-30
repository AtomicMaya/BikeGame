/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.actor;

import java.util.List;

import main.game.GameWithLevels;
import main.game.levels.Level;
import main.math.Transform;
import main.math.Vector;

public class ActorLevel implements Actor {

	List<Level> levels;
	private GameWithLevels game;
	private int currentLevel = 0;

	public ActorLevel(GameWithLevels game, List<Level> levels) {
		if (levels == null || levels.size() == 0)
			throw new NullPointerException("No levels to initialize");
		this.levels = levels;
		this.game = game;

		beginLevel(0);
	}

	@Override
	public Transform getTransform() {
		return null;
	}

	@Override
	public Vector getVelocity() {
		return null;
	}

	@Override
	public void update(float deltaTime) {

		if (levels.get(currentLevel).getFinishActor() != null
				&& levels.get(currentLevel).getFinishActor().isFinished()) {
			nextLevel();

		}
	}

	public void beginLevel(int i) {
		currentLevel = i;
		levels.get(i).createAllActors();
		game.addActor(levels.get(i).getActors());
		game.setViewCandidate(levels.get(i).getViewCandidate());

	}

	@Override
	public void destroy() {
		game.destroyActor(this);
	}

	public void nextLevel() {
		// Destroy the old actors and entities
//		for (Actor a : levels.get(currentLevel).getActors()) {
//			a.destroy();
//		}
		game.destroyAllActorsExept(this);
		currentLevel++;
		if (currentLevel >= levels.size()) {
			currentLevel = 0;
		}
		beginLevel(currentLevel);
	}
	
	public void restartLevel() {
		
	}

}
