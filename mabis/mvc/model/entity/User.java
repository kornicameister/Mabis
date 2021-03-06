/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.Serializable;

import mvc.model.enums.TableType;
import utilities.Hasher;

/**
 * Klasa {@link User} jest obiektową wersją tabeli bazy danych o następującej
 * </br> | idUser </br> | login </br> | email </br> | password </br> | firstName
 * </br> | lastName </br> | avatar </br>
 * 
 * @author kornicameister
 * @version 0.3
 * @see Author
 */

public class User extends Author implements Serializable {
	private static final long serialVersionUID = 7165099983429863760L;
	private String password;

	/**
	 * Tworzy nowego uzytkownika, ale jest to jedynie pusty konstruktor, wiec
	 * nie ustawia wartosci zadnych pol
	 */
	public User() {
		super();
	}

	/**
	 * Tworzy nowego uzytkownika
	 * 
	 * @param login
	 *            login uzytkownika
	 * @param email
	 *            email uzytkownika
	 * @param password
	 *            haslo uzytkownika
	 */
	public User(String login, String email, String password) {
		super();
		this.titles[2] = login;
		this.titles[3] = email;
		this.password = Hasher.hashString(password);
	}

	/**
	 * Tworzy nowego uzytkownika bazujac na jego imieniu i nazwisku
	 * 
	 * @param fName
	 *            imie
	 * @param lName
	 *            nazwisko
	 */
	public User(String fName, String lName) {
		super(fName, lName);
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[LOGIN: " + this.getLogin() + "]\n";
		str += "[MAIL: " + this.getEmail() + "]\n";
		return str;
	}

	@Override
	public String[] metaData() {
		String tmp[] = {"idUser", "object", "avatarFK"};
		return tmp;
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
		this.password = Hasher.hashString(password);
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.tableType = TableType.USER;
	}
}
