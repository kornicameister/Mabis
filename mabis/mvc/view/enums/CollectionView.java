/**
 * 
 */
package mvc.view.enums;

import mvc.model.enums.TableType;

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
			TableType.AUDIO_ALBUM), VIEW_ALL(TableType.NULL);

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
				case BOOK :
				case MOVIE :
				case AUDIO_ALBUM :
					return tt.toString();
				default :
					break;
			}
		}
		return "all";
	}

	/**
	 * Metoda zwraca tablice zawierajacych wszystkie mozliwe typy
	 * {@link CollectionView}
	 * 
	 * @return tablice enumow {@link CollectionView}
	 */
	public static CollectionView[] toArray() {
		CollectionView tmp[] = new CollectionView[4];
		tmp[0] = VIEW_ALL;
		tmp[1] = VIEW_AUDIOS;
		tmp[2] = VIEW_BOOKS;
		tmp[3] = VIEW_MOVIES;
		return tmp;
	}
}
