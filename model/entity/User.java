/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import exceptions.SQLForeingKeyNotFound;

/**
 * @author kornicameister
 * 
 */

// TODO comments

public class User extends Author {
	private String login, email, password;

	/**
	 * @param login
	 * @param email
	 * @param password
	 */
	public User(String login, String email, String password) {
		super();
		this.login = login;
		this.email = email;
		this.password = password;
	}

	/**
	 * @param fName
	 * @param lName
	 */
	public User(String fName, String lName) {
		super(fName, lName);
	}

	@Override
	public String toString() {
		return null;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.metaData.clear();
		this.reloadMetaData();
	}

	@Override
	protected void reloadMetaData() {
		this.metaData.clear();
		this.metaData.put("idUser", this.getPrimaryKey().toString());
		this.metaData.put("login", this.getLogin());
		this.metaData.put("email", this.getEmail());
		this.metaData.put("password", this.getPassword());
		this.metaData.put("firstName", this.getFirstName());
		this.metaData.put("lastName", this.getLastName());
		try {
			this.metaData.put("avatar", this.getForeingKey("avatar").getValue()
					.toString());
		} catch (SQLForeingKeyNotFound e) {
			e.printStackTrace();
		} finally {
			this.metaData.clear();
		}
	}

}
