/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;

import model.BaseTable;
import model.enums.TableType;

/**
 * Klasa {@link Author} jest obiektową wersją tabeli bazy danych o następującej
 * </br> | idBand </br> | firstName </br> | lastName </br> | picture </br>
 * 
 * @author kornicameister
 * @version 0.4
 */
public class Author extends BaseTable implements Serializable {
	private static final long serialVersionUID = 7692599340459728530L;
	private Picture pictureFile = null;

	/**
	 * Domyślny pusty konstruktor dla klasy {@link Author}
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
