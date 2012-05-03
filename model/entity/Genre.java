/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;

import model.BaseTable;
import model.enums.TableType;

/**
 * Class maps itself to mabis.genre table Table structure: </br> | idGenre </br>
 * | genre </br>
 * 
 * @author kornicameister
 * 
 */
public class Genre extends BaseTable implements Serializable {
	private static final long serialVersionUID = 3862214016426775417L;

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
	 * Construct new Genre with genre value
	 * 
	 * @param genre
	 */
	public Genre(String genre) {
		super(genre);
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idGenre", "object" };
		return tmp;
	}

	/**
	 * wraps {@link BaseTable#setOriginalTitle(String)}
	 * 
	 * @param genre
	 */
	public void setGenre(String genre) {
		this.titles[0] = genre;
	}

	/**
	 * wraps {@link BaseTable#getOriginalTitle()}
	 * 
	 * @return
	 */
	public String getGenre() {
		return this.titles[0];
	}

	@Override
	protected void initInternalFields() {
		this.tableName = TableType.GENRE.toString();
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[GENRE: " + this.getGenre() + "]\n";
		return str;
	}

}
