package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.User;
import controller.Blobber;
import controller.SQLFactory;
import controller.SQLStamentType;
import database.MySQLAccess;

/**
 * This is the wrapper that allows to perform database specific operation to
 * User table content
 * 
 * @author kornicameister
 * 
 */
public class UserSQLFactory extends SQLFactory {
	private User table;
	private HashMap<Integer, User> users;

	public UserSQLFactory(User table) {
		super();
		users = new HashMap<Integer, User>();
		this.table = table;
	}

	@Override
	public void executeSQL() throws SQLException {
		try {
			if (!MySQLAccess.getConnection().isValid(1000)) {
				MabisLogger.getLogger().log(Level.SEVERE,
						"Database connection lost");
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = this.createSQL(table);
		PreparedStatement st = MySQLAccess.getConnection().prepareStatement(
				sql, Statement.RETURN_GENERATED_KEYS);
		switch (this.type) {
		case UPDATE:
			break;
		case INSERT:
			short index = 1;
			Blobber.putBlobImageToStatement(st, this.table.getPictureFile(),
					index++);
			st.setString(index++, table.getLastName());
			st.setString(index++, table.getFirstName());
			st.setString(index++, table.getPassword());
			st.setString(index++, table.getEmail());
			st.setString(index++, table.getLogin());
			st.execute();
			break;
		case SELECT:
			this.parseResultSet(st.executeQuery(sql));
			break;
		case DELETE:
		}
		st.close();
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
				u.setPicture(
						Blobber.createImageFromBlob(set.getBlob("avatar")),
						"mabis.user.avatar");
				u.setPrimaryKey(set.getInt("idUser"));
				this.users.put(u.getPrimaryKey(), u);
			}
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		}
	}

	public HashMap<Integer, User> getUsers() {
		return this.users;
	}
}
