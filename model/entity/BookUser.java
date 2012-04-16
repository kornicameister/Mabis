/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;

/**
 * This class maps itself to mabis.bookUser many to many table
 * @author kornicameister
 * 
 */
public class BookUser extends ManyToManyTable {

	public BookUser() {
		super();
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.tableName = TableType.BOOK_USER.toString();
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idBookUser", "idUser", "idBook" };
		return tmp;
	}
}
