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

}
