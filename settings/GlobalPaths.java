package settings;

/**
 * This enum contains paths to all fixed directories used by mabis
 * 
 * @author kornicameister
 * 
 */
public enum GlobalPaths {
	AVATAR_CACHE_PATH("./cache/avatars/"), DEFAULT_COVER_PATH(
			"src/resources/defaultCover.png"), DEFAULT_AVATAR_PATH(
			"src/resources/defaultAvatar.png"), BAND_CACHE_PATH(
			"./cache/bands/"), AUTHOR_CACHE_PATH("./cache/authors/"), MEDIA_CACHE_PATH(
			"./cache/media/"), TMP(System.getProperty("java.io.tmpdir")
			+ "/mabis/"), CROSS_SIGN("src/resources/cross_sign.png"), OK_SIGN(
			("src/resources/ok_sign.png"));

	private final String path;

	private GlobalPaths(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return this.path;
	}
}
