package main.game.actor;

public enum DepthValue {
	BACKGROUND_DEAP(-20),
	BACKGROUND_MEDIUM(-19),
	BACKGROUND_LOW(-18),
	
	TERRAIN_DEAP(-17),
	TERRAIN_MEDIUM(-16),
	TERRAIN_LOW(-15),
	
	BACK_OBSTACLE_DEAP(-10),
	BACK_OBSTACLE_MEDIUM(-9),
	BACK_OBSTACLE_LOW(-8),
	
	FRONT_OBSTACLE_DEAP(10),
	FRONT_OBSTACLE_MEDIUM(11),
	FRONT_OBSTACLE_LOW(12),
	

	
	PLAYER_DEAP(41),
	PLAYER_MEDIUM(42),
	PLAYER_LOW(43),
	
	BUTTON_BG(42000),
	BUTTON_TEXT(42042)
	;
	public final float value;
	
	DepthValue(float value){
		this.value = value;
	}
}