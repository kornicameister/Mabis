/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.BaseTable;
import model.enums.TableType;

/**
 * This method is to represent Author table Table structure: </br> | idBand
 * </br> | firstName </br> | lastName </br> | picture </br>
 * 
 * @author kornicameister
 * @version 0.4
 */
public class Author extends BaseTable {
	private Picture pictureFile = null;

	/**
	 * @see BaseTable#BaseTable()
	 */
	public Author() {
		super();
	}

	/**
	 * Constructs Author object with firstName and lastName. <b>Notice</b> that
	 * in this case {@link BaseTable} default constructor is called
	 * 
	 * @param fName
	 * @param lName
	 */
	public Author(String fName, String lName) {
		super();
		this.setOriginalTitle(fName);
		this.setLocalizedTitle(lName);
	}

	/**
	 * @see BaseTable#BaseTable(int)
	 * @param pk
	 */
	public Author(int pk) {
		super(pk);
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idAuthor", "firstName", "lastName", "avatar" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.tableName = TableType.AUTHOR.toString();
		this.constraints.add(TableType.PICTURE);
	}

	public String getFirstName() {
		return this.getOriginalTitle();
	}

	public void setFirstName(String firstName) {
		this.setOriginalTitle(firstName);
	}


	public String getLastName() {
		return this.getLocalizedTitle();
	}

	public void setLastName(String lastName) {
		this.setLocalizedTitle(lastName);
	}

	public void setPicture(Picture f) {
		this.pictureFile = f;
	}

	public Picture getPictureFile() {
		return pictureFile;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[FNAME: " + this.getFirstName() + "]\n";
		str += "[LNAME: " + this.getLastName() + "]\n";
		return str;
	}
}
