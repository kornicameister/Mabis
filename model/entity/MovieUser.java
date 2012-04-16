/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;

/**
 * This class maps itself to mabis.movieUser many-to-many table
 * @author kornicameister
 * 
 */
public class MovieUser extends ManyToManyTable {

	public MovieUser() {
		super();
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.tableName = TableType.MOVIE_USER.toString();
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idMovieUser", "idMovie", "idUser" };
		return tmp;
	}
}
