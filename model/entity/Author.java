/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import javax.swing.ImageIcon;

import model.enums.TableType;

/**
 * This method is to represent Author table
 * Table structure: </br>
 * | idBand </br>
 * | firstName </br>
 * | lastName </br>
 * | picture </br>
 * @author kornicameister
 * @version 0.2
 */
public class Author extends BaseTable {
	private ImageIcon picture = null;

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
	protected void initInternalFields() {
		this.tableName = TableType.AUTHOR.toString();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.getOriginalTitle();
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.setOriginalTitle(firstName);
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.getLocalizedTitle();
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.setLocalizedTitle(lastName);
	}

	/**
	 * @return the picture
	 */
	public ImageIcon getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(ImageIcon picture) {
		this.picture = picture;
	}
}
