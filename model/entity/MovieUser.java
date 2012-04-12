/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;

/**
 * @author kornicameister
 * 
 */
public class MovieUser extends ManyToManyTable {

	public MovieUser() {
		super();
		this.tableName = TableType.MOVIE_USER.toString();
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
	}
}
