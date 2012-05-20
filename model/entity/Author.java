/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

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
	private Picture pictureFile;

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
		this.setTitle(fName);
		this.setSubTitle(lName);
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
		String tmp[] = { "idAuthor", "object", "avatarFK" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.tableType = TableType.AUTHOR;
	}

	public String getFirstName() {
		return this.getTitle();
	}

	public void setFirstName(String firstName) {
		this.setTitle(firstName);
	}

	public String getLastName() {
		return this.getSubtitle();
	}

	public void setLastName(String lastName) {
		this.setSubTitle(lastName);
	}

	public void setPicture(Picture f) {
		this.pictureFile = f;
	}

	public Picture getPictureFile() {
		return pictureFile;
	}

	@Override
	public String toString() {
		return this.getPrimaryKey() + ": " + this.getFirstName() + " "
				+ this.getLastName();
	}

	@Override
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
			data.add(d);
		}
		data.add("First name");
		data.add("Last name");
		data.add("Avatar");
		return data.toArray();
	}

	@Override
	public Object[] toRowData() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
			data.add(d);
		}
		data.add(this.getFirstName());
		data.add(this.getLastName());
		data.add(new ImageIcon(this.getPictureFile().getImagePath()));
		return data.toArray();
	}
}
