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
public class BookUser extends ManyToManyTable {

	public BookUser() {
		super();
		this.tableName = TableType.BOOK_USER.toString();
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.reloadMetaData();
	}

	@Override
	public void reloadMetaData() {
		this.metaData.clear();
		this.metaData.put("idBookUser", this.getPrimaryKey().toString());
		this.metaData.put("idUser",
				this.getMultiForeing(this.getPrimaryKey()).f1.getValue()
						.toString());
		this.metaData.put("idBook",
				this.getMultiForeing(this.getPrimaryKey()).f2.getValue()
						.toString());
	}
}
