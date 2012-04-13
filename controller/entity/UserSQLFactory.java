package controller.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.ImageIcon;

import model.entity.BaseTable;
import model.entity.User;
import controller.InvalidBaseClass;
import controller.SQLFactory;
import controller.SQLStamentType;
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
	public boolean executeSQL() {
		String sql = this.createSQL(table);

		try {
			if (!MySQLAccess.getConnection().isValid(1000)) {
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			PreparedStatement st = MySQLAccess.getConnection()
					.prepareStatement(sql);
			switch (this.type) {
			case UPDATE:
			case INSERT:
				short index = 1;
				setImageAsBlob(st, index++);
				st.setString(index++, table.getLastName());
				st.setString(index++, table.getFirstName());
				st.setString(index++, table.getPassword());
				st.setString(index++, table.getEmail());
				st.setString(index++, table.getLogin());
				// TODO polish diacritis are problem
				// TODO no valid return statement confirming that sql was
				// executed successfully
				return this.parseResultSet(st.executeQuery());
			case SELECT:
				return this.parseResultSet(st.executeQuery());
			}
			if (this.type == SQLStamentType.UPDATE
					|| this.type == SQLStamentType.INSERT) {

			} else {
				return MySQLAccess.getConnection().prepareStatement(sql)
						.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setImageAsBlob(PreparedStatement st, int index) {
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

	private boolean parseResultSet(ResultSet set) {
		User u = null;
		try {
			if (this.type == SQLStamentType.SELECT) {
				set.first();
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
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
