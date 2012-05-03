/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;

import model.ManyToManyTable;
import model.enums.TableType;

/**
 * This class maps itself to mabis.bookUser many to many table
 * 
 * @author kornicameister
 * 
 */
public class BookUser extends ManyToManyTable implements Serializable {
	private static final long serialVersionUID = -4928344550589522311L;

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
