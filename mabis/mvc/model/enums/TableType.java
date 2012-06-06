package mvc.model.enums;

/**
 * Wszystkie tabele w bazie danych, tj. ich nazwy, sa tutaj zdefiniowane w
 * nastepujacej konfiguracji:
 * 
 * <pre>
 * <b>nazwa enuma : nazwa tabeli</b>
 * </pre>
 * 
 * @author kornicameister
 */
public enum TableType {
	AUDIO_ALBUM("audioAlbum"), AUDIO_USER("audioUser"), AUTHOR("author"), BAND(
			"author"), BOOK("book"), BOOK_USER("bookUser"), PICTURE("picture"), GENRE(
			"genre"), MOVIE("movie"), MOVIE_USER("movieUser"), USER("user"), NULL(
			"all");

	private String name;
	TableType(String s) {
		this.name = s;
	}

	/**
	 * @return returns string representation of table name
	 */
	@Override
	public String toString() {
		return this.name;
	}

}
