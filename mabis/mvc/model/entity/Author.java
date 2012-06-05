/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.Serializable;

import mvc.model.BaseTable;
import mvc.model.enums.AuthorType;
import mvc.model.enums.TableType;

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
	protected AuthorType type;

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
		this.setSubTitle(fName);
		this.setTitle(lName);
	}

	/**
	 * @see BaseTable#BaseTable(int)
	 * @param pk
	 */
	public Author(int pk) {
		super(pk);
	}

	public Author(String fName, String lName, AuthorType type) {
		super();
		this.setSubTitle(fName);
		this.setTitle(lName);
		this.type = type;
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idAuthor", "object", "avatarFK", "type", "hash"};
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.tableType = TableType.AUTHOR;
	}

	public String getFirstName() {
		return this.getSubtitle();
	}

	public void setFirstName(String firstName) {
		this.setSubTitle(firstName);
	}

	public String getLastName() {
		return this.getTitle();
	}

	public void setLastName(String lastName) {
		this.setTitle(lastName);
	}

	public void setPicture(Picture f) {
		this.pictureFile = f;
	}

	public Picture getAvatar() {
		return pictureFile;
	}

	public AuthorType getType() {
		return this.type;
	}
	
	public void setType(AuthorType t){
		this.type = t;
	}

	@Override
	public String toString() {
		return this.getPrimaryKey() + ": " + this.getFirstName() + " "
				+ this.getLastName() + ": " + this.getType() + "\n";
	}

	@Override
	public int compareTo(BaseTable o) {
		if(o == null){
			return 0;
		}
		Author tmp = (Author) o;
		int result = this.getLastName().compareTo(tmp.getLastName());
		if (result == 0) {
			result = this.getFirstName().compareTo(tmp.getFirstName());
		}
		if(result == 0){
			result = super.compareTo(o);
		}
		return result;
	}

}
