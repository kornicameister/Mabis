/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import database.Utilities;

/**
 * 
 * Table structure: </br> | idUser </br> | login </br> | email </br> | password
 * </br> | firstName </br> | lastName </br> | avatar </br>
 * 
 * @author kornicameister
 * @version 0.2
 */

// TODO comments

public class User extends Author {
	private String password;

	public User(String login, String email, String password) {
		super();
		this.titles[2] = login;
		this.titles[3] = email;
		this.password = password;
	}

	public User(String fName, String lName) {
		super(fName, lName);
	}

	@Override
	public String toString() {
		return null;
	}

	public String getLogin() {
		return this.titles[2];
	}

	public void setLogin(String login) {
		this.titles[2] = login;
	}

	public String getEmail() {
		return this.titles[3];
	}

	public void setEmail(String email) {
		this.titles[3] = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = Utilities.md5sum(password);
	}

}
