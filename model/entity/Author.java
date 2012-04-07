/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import model.enums.TableType;
import exceptions.SQLForeingKeyNotFound;

/**
 * @author kornicameister
 * 
 */
public class Author extends BaseTable {
	private String firstName = null;
	private String lastName = null;

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
		this.firstName = fName;
		this.lastName = lName;
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
		this.firstName = new String("");
		this.lastName = new String("");
		this.tableName = TableType.AUTHOR.toString();
		this.reloadMetaData();
	}

	@Override
	public void reloadMetaData() {
		this.metaData.clear();
		this.metaData.put("idAuthor", this.getPrimaryKey().toString());
		this.metaData.put("firstName", this.getFirstName());
		this.metaData.put("lastName", this.getLastName());
		try {
			this.metaData.put("picture", this.getForeingKey("picture")
					.getValue().toString());
		} catch (SQLForeingKeyNotFound e) {
			e.printStackTrace();
		} finally {
			this.metaData.clear();
		}
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
