package settings;

import settings.io.SettingsException;
import settings.io.SettingsLoader;

/**
 * This enum contains paths to all fixed directories used by mabis
 * 
 * @author kornicameister
 * 
 */
public enum GlobalPaths {
	AVATAR_CACHE_PATH, 
	DEFAULT_COVER_PATH, 
	DEFAULT_AVATAR_PATH, 
	BAND_CACHE_PATH, 
	AUTHOR_CACHE_PATH,
	MEDIA_CACHE_PATH, 
	TMP,
	CROSS_SIGN,
	OK_SIGN,
	ISBN_SIGN,
	MUSIC_ICON;

	private String path = null;

	private GlobalPaths() {
		try {
			this.path = SettingsLoader.loadPath(this.name());
		} catch (SettingsException e) {
			e.printStackTrace();
		} finally {
			if(path == null){
				path = "";
			}
		}
	}

	@Override
	public String toString() {
		return this.path;
	}
}
