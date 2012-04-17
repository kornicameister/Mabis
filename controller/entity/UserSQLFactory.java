package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import model.entity.User;
import controller.Blobber;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * This is the wrapper that allows to perform database specific operation to
 * User table content
 * 
 * @author kornicameister
 * 
 */
public class UserSQLFactory extends SQLFactory {
	private HashMap<Integer, User> users;

	public UserSQLFactory(SQLStamentType type, User table) {
		super(type, table);
		users = new HashMap<Integer, User>();
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		User u = (User) this.table;
		switch (this.type) {
		case UPDATE:
			break;
		case INSERT:
			short index = 1;
			Blobber.putBlobImageToStatement(st, u.getPictureFile(), index++);
			st.setString(index++, u.getLastName());
			st.setString(index++, u.getFirstName());
			st.setString(index++, u.getPassword());
			st.setString(index++, u.getEmail());
			st.setString(index++, u.getLogin());
			st.execute();
			break;
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		case DELETE:
			break;
		default:
			break;
		}
	}

	/**
	 * Methods graps {@link ResultSet} and tries to parse it accordingly to
	 * {@link SQLStamentType} set in the class
	 * 
	 * @param set
	 * @throws SQLException
	 */
	private void parseResultSet(ResultSet set) throws SQLException {
		User u = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				u = new User();
				u.setFirstName(set.getString("firstName"));
				u.setLastName(set.getString("lastName"));
				u.setEmail(set.getString("email"));
				u.setLogin(set.getString("login"));
				u.setPassword(set.getString("password"));
				u.setPicture(Blobber.createImageFromBlob(set.getBlob("avatar")));
				u.setPrimaryKey(set.getInt("idUser"));
				this.users.put(u.getPrimaryKey(), u);
			}
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		default:
			break;
		}
	}

	public HashMap<Integer, User> getUsers() {
		return this.users;
	}
}
