package settings;

/**
 * This enum contains paths to all fixed directories used by mabis
 * 
 * @author kornicameister
 * 
 */
public enum GlobalPaths {
	AVATAR_CACHE_PATH("./cache/avatars/"), 
	DEFAULT_COVER_PATH("src/resources/defaultCover.png");

	private final String path;

	private GlobalPaths(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return this.path;
	}
}
