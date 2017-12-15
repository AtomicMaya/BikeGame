package main.game;

import java.util.Arrays;
import java.util.List;

import main.game.levels.*;

/** Create an instance of {@linkplain ComplexBikeGame}. */
public class BikeGame extends ComplexBikeGame {
	@Override
	protected List<Level> createLevelList() {
		return Arrays.asList(new Level0(this), new Level1(this), new Level2(this));
	}
}
