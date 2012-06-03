/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.Serializable;

import mvc.model.ManyToManyTable;
import mvc.model.enums.TableType;

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
		this.tableType = TableType.BOOK_USER;
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idBookUser", "idUser", "idBook" };
		return tmp;
	}
}
