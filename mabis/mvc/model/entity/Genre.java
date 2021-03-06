/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.Serializable;

import mvc.model.BaseTable;
import mvc.model.enums.GenreType;
import mvc.model.enums.TableType;

/**
 * Class maps itself to mabis.genre table Table structure: </br> | idGenre </br>
 * | genres </br>
 * 
 * @author kornicameister
 * 
 */
public class Genre extends BaseTable implements Serializable {
	private static final long serialVersionUID = 3862214016426775417L;
	private GenreType gType;

	/**
	 * Construct new Genre with default empty constructor
	 */
	public Genre() {
		super();
	}

	/**
	 * Constructs new Genre with primary key set up
	 * 
	 * @param pk
	 */
	public Genre(int pk) {
		super(pk);
	}

	/**
	 * Tworzy nowy gatunek z nazwą genre.
	 * 
	 * @param genre
	 */
	public Genre(String genre) {
		super(genre);
	}

	public Genre(String genre, GenreType t) {
		super(genre);
		this.gType = t;
	}

	@Override
	public String[] metaData() {
		String tmp[] = {"idGenre", "object", "type"};
		return tmp;
	}

	/**
	 * Metoda ustawia nazwę gatunku
	 * 
	 * @param genre
	 */
	public void setGenre(String genre) {
		this.titles[0] = genre;
	}

	/**
	 * wraps {@link BaseTable#getTitle()}
	 * 
	 * @return nazwę gatunku
	 */
	public String getGenre() {
		return this.titles[0];
	}

	@Override
	protected void initInternalFields() {
		this.tableType = TableType.GENRE;
	}

	@Override
	public String toString() {
		return this.getPrimaryKey() + ": " + this.getGenre() + ": -> "
				+ this.getType() + "\n";
	}

	@Override
	public int compareTo(BaseTable o) {
		if (o == null) {
			return 0;
		}
		int result = super.compareTo(o);
		if (result == 0) {
			result = this.getGenre().compareTo(((Genre) o).getGenre());
		}
		return result;
	}

	public GenreType getType() {
		return gType;
	}

	public void setType(GenreType gType) {
		this.gType = gType;
	}
}
