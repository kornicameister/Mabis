/**
 * 
 */
package view.enums;

import model.enums.TableType;

/**
 * Enum opisuje jaki rodzaj kolekcji jest aktualnie wyświetlany w widoku
 * kolekcji.
 * 
 * @author kornicameister
 * @version 0.1
 * @see TableType
 */
public enum CollectionView {
	VIEW_BOOKS(TableType.BOOK), VIEW_MOVIES(TableType.MOVIE), VIEW_AUDIOS(
			TableType.AUDIO_ALBUM), VIEW_ALL(null);

	private TableType tt = null;

	/**
	 * Konstruktor tworzy enum bazując na {@link TableType} przekazanym jako
	 * parametr
	 * 
	 * @param t
	 */
	private CollectionView(TableType t) {
		this.tt = t;
	}

	@Override
	public String toString() {
		if (tt != null) {
			switch (this.tt) {
			case BOOK:
			case MOVIE:
			case AUDIO_ALBUM:
				return tt.toString();
			}
		}
		return "all";
	}

	public static String[] toArray() {
		String tmp[] = new String[4];
		tmp[0] = "all";
		tmp[1] = TableType.BOOK.toString();
		tmp[2] = TableType.MOVIE.toString();
		tmp[3] = TableType.AUDIO_ALBUM.toString();
		return tmp;
	}
}
