/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.File;

import javax.swing.ImageIcon;

import model.enums.TableType;

/**
 * This method is to represent Author table Table structure: </br> | idBand
 * </br> | firstName </br> | lastName </br> | picture </br>
 * 
 * @author kornicameister
 * @version 0.3
 */
public class Author extends BaseTable {
	private ImageIcon picture = null;
	private File pictureFile = null;

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
		String tmp[] = { "idAuthor", "firstName", "lastName", "picture" };
		return tmp;
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
	public void setPicture(ImageIcon picture,String path) {
		this.picture = picture;
		this.pictureFile = new File(path);
	}

	public File getPictureFile() {
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
