package main.game.actor;

/**
 * Created on 11/24/2017 at 4:36 PM.
 */
public enum CharacterSprite {
	LETTER ("./res/font/letter"),
	NUMBER ("./res/font/num"),
	APOSTROPHE ("./res/font/symbApos"),
	COLON ("./res/font/symbColon"),
	COMMA ("./res/font/symbComma"),
	DOT ("./res/font/symbDot"),
	PLUS ("./res/font/symbPlus"),
	DASH ("./res/font/symbDash"),
	QUESTIONMARK ("./res/font/symbQMark"),
	EXCLAMATIONMARK ("./res/font/symbEMark"),
	LPARENTHESE ("./res/font/symbLParen"),
	RPARENTHESE ("./res/font/symbRParen"),
	SLASH ("./res/font/symbSlash"),
	BACKSLASH ("./res/font/symbBackSlash"),
	QUOTATIONMARK ("./res/font/symbQuot");

	private String url;

	CharacterSprite(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}
}
