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
		this.reloadMetaData();
	}

	@Override
	protected void reloadMetaData() {
		this.metaData.clear();
		this.metaData.put("idMovieUser", this.getPrimaryKey().toString());
		this.metaData.put("idUser",
				this.getMultiForeing(this.getPrimaryKey()).f1.getValue()
						.toString());
		this.metaData.put("idMovie",
				this.getMultiForeing(this.getPrimaryKey()).f2.getValue()
						.toString());
	}
}
