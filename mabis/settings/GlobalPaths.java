package settings;

import java.io.File;

import mvc.view.items.minipanels.AuthorMiniPanel;
import mvc.view.items.minipanels.BandMiniPanel;
import mvc.view.items.minipanels.IndustryIdentifiersMiniPanel;
import mvc.view.items.minipanels.TagCloudMiniPanel;
import mvc.view.items.minipanels.TrackListPanel;
import settings.io.SettingsException;
import settings.io.SettingsLoader;

/**
 * Enum, gdzie zawarte są wszystkie ścieżki z jakich aplikacji korzysta podczas
 * swojego działania. Należy tutaj zaznaczyć, że enum ten nie posiada wartości
 * stałych per se. Wewnętrzne pole {@link GlobalPaths#path} ma wartości zgodne z
 * tymi wczytanymi z pliku konfiguracyjnego.
 * 
 * @author kornicameister
 * 
 */
public enum GlobalPaths {
	/**
	 * sciezka do folderu, gdzie umieszczane sa avatary
	 */
	AVATAR_CACHE_PATH,
	
	/**
	 * sciezka do domyslnej okladki
	 */
	DEFAULT_COVER_PATH, 
	
	/**
	 * sciezka do domyslnego avatara
	 */
	DEFAULT_AVATAR_PATH, 
	
	/**
	 * sciezka do folderu, gdzie umieszczane sa avatary zespolow
	 */
	BAND_CACHE_PATH,
	
	/**
	 * sciezka do folderu, gdzie umieszczane sa avatary autorow
	 */
	AUTHOR_CACHE_PATH,
	
	/**
	 * sciezka do folderu, gdzie umieszczane sa okladki elementow kolekcji
	 */
	MEDIA_CACHE_PATH, 
	
	/**
	 * sciezka do folderu tymczasowego, miejsca gdzie trafiaja na samym
	 * początku wszystkie zdjecia lub obiekty tymczasowe
	 */
	TMP, 
	
	/**
	 * sciezka do symbolu krzyzyka
	 * 
	 * @see AuthorMiniPanel
	 * @see TagCloudMiniPanel
	 * @see BandMiniPanel
	 */
	CROSS_SIGN, 
	
	/**
	 * sciezka do symbolu ok
	 * 	 
	 * @see AuthorMiniPanel
	 * @see TagCloudMiniPanel
	 * @see BandMiniPanel
	 */
	OK_SIGN, 
	
	/**
	 * sciezka do symbolu ISBN
	 * 
	 * @see IndustryIdentifiersMiniPanel
	 */
	ISBN_SIGN,
	
	/**
	 * sciezka do symbolu muzycznego
	 * 
	 * @see TrackListPanel
	 */
	MUSIC_ICON,
	
	/**
	 * sciezdka do pliku z licencja
	 */
	LICENSE("src/resources/gpl-3.0.txt"), 
	
	MABIS_ICON("src/resources/icon-cool.png"),

	MABIS_WHY("src/resources/about/why"), 
	MABIS_API("src/resources/about/api"),
	MABIS_MVC("src/resources/about/mvc"),
	MABIS_VERSION("src/resources/about/version"),
	
	LASTFM_LOGO("src/resources/logos/last_fm.png"), 
	IMDB_LOGO("src/resources/logos/imdb-logo.png"), 
	GOOGLE_BOOKS_LOGO("src/resources/logos/google_books.png"), 
	
	MABIS_AUTHOR_AVATAR("src/resources/author/photo.png"), 
	MABIS_AUTHOR_ABOUT("src/resources/author/about"), 
	MABIS_AUTHOR_WORK("src/resources/author/work"), 
	MABIS_AUTHOR_PROJECTS("src/resources/author/projects");

	private String path = null;
	private final boolean fixed;

	/**
	 * Konstruktor obiektu enum. Odwoluje sie do ustawien aplikacji
	 * i pobiera sciezka, ktora odpowiedia wartosci wywolania funkcji <b>name()</b>.
	 * 
	 * @see SettingsLoader
	 */
	private GlobalPaths() {
		try {
			this.path = SettingsLoader.load(this.name());
			File tmp = new File(path);
			if (!tmp.exists() && this.path != "TMP") {
				tmp.mkdir();
			}
		} catch (SettingsException e) {
			e.printStackTrace();
		} finally {
			if (path == null) {
				path = "";
			}
		}
		this.fixed = false;
	}
	
	private GlobalPaths(String path){
		this.path = path;
		this.fixed = true;
	}

	@Override
	public String toString() {
		return this.path;
	}

	public void setPath(String pathContent) {
		this.path = pathContent;
	}

	public boolean isFixed() {
		return fixed;
	}
	
}
