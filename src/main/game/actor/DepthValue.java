package main.game.actor;

public enum DepthValue {
	BACKGROUND_DEEP(-20),
	BACKGROUND_MEDIUM(-19),
	BACKGROUND_LOW(-18),
	
	BACK_OBSTACLE_DEEP(-17),
	BACK_OBSTACLE_MEDIUM(-16),
	BACK_OBSTACLE_LOW(-15),	
	
	TERRAIN_DEEP(-14),
	TERRAIN_MEDIUM(-13),
	TERRAIN_LOW(-12),
	
	

	FRONT_OBSTACLE_DEEP(10),
	FRONT_OBSTACLE_MEDIUM(11),
	FRONT_OBSTACLE_LOW(12),
	

	
	PLAYER_DEEP(41),
	PLAYER_MEDIUM(42),
	PLAYER_LOW(43),
	
	BUTTON_BG(42000),
	BUTTON_TEXT(42042) ;

	public final float value;
	
	DepthValue(float value){
		this.value = value;
	}
}
