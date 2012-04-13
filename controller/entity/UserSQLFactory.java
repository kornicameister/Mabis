package controller.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import logger.MabisLogger;
import model.entity.BaseTable;
import model.entity.User;
import controller.InvalidBaseClass;
import controller.SQLFactory;
import database.MySQLAccess;

/**
 * This is the wrapper that allows to
 * 
 * @author kornicameister
 * 
 */
public class UserSQLFactory extends SQLFactory {
	private User table;
	private HashMap<Integer, User> users;

	public UserSQLFactory() {
		super();
		users = new HashMap<Integer, User>();
	}

	@Override
	public void setTable(BaseTable table) throws InvalidBaseClass {
		if (table instanceof User) {
			this.table = (User) table;
		}
	}

	@Override
	public void executeSQL() {
		try {
			if (!MySQLAccess.getConnection().isValid(1000)) {
				MabisLogger.getLogger().log(Level.SEVERE,
						"Database connection lost");
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			String sql = this.createSQL(table);
			PreparedStatement st = MySQLAccess.getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			switch (this.type) {
			case UPDATE:
				break;
			case INSERT:
				short index = 1;
				setImageAsBlob(st,index++);
				st.setString(index++, table.getLastName());
				st.setString(index++,  table.getFirstName());
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setImageAsBlob(PreparedStatement st, short index) {
		try {
			FileInputStream fis = new FileInputStream(table.getPictureFile());
			st.setBinaryStream(index, (InputStream) fis, (int) table
					.getPictureFile().length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void parseResultSet(ResultSet set) {
		User u = null;
		try {
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
							this.createImageFromBlob(set.getBlob("avatar")),
							"mabis.user.avatar");
					u.setPrimaryKey(set.getInt("idUser"));
					this.users.put(u.getPrimaryKey(), u);
				}
				break;
			case UPDATE:
			case DELETE:
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ImageIcon createImageFromBlob(Blob blob) {
		try {
			ImageIcon i = new ImageIcon(blob.getBytes(1, (int) blob.length()));
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, User> getUsers() {
		return this.users;
	}
}
