/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.utilities.ForeignKey;
import exceptions.SQLForeingKeyNotFound;

/**
 * Class maps itself to mabis.genre table
 * 
 * @author kornicameister
 * 
 */
public class Genre extends BaseTable {

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

	/**
	 * wraps {@link BaseTable#setOriginalTitle(String)}
	 * 
	 * @param genre
	 */
	public void setGenre(String genre) {
		this.setOriginalTitle(genre);
	}

	/**
	 * wraps {@link BaseTable#getOriginalTitle()}
	 * 
	 * @return
	 */
	public String getGenre() {
		return this.getOriginalTitle();
	}

	@Override
	protected void initInternalFields() {
		return;
	}

	@Override
	public void checkConstraints(ForeignKey... keys)
			throws SQLForeingKeyNotFound {
		return;
	}

}
