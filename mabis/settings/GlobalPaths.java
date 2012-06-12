package settings;

import java.io.File;

import mvc.view.AboutMabis;
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
	 * sciezka do pliku html dla okienka {@link AboutMabis}
	 */
	ABOUT_MABIS_HTML,
	
	/**
	 * sciezka do pliku html dla okienka {@link AboutMabis}
	 */
	ABOUT_AUTHOR_HTML,
	
	/**
	 * sciezdka do pliku z licencja
	 */
	LICENSE;

	private String path = null;

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
	}

	@Override
	public String toString() {
		return this.path;
	}

	public void setPath(String pathContent) {
		this.path = pathContent;
	}
}
