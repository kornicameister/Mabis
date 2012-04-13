package controller.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

	public UserSQLFactory() {
		super();
	}

	@Override
	public String createSQL() {
		String rawQueryCopy = this.rawQuery.replaceFirst("!",
				table.getTableName());
		String fieldList = this.buildFieldList(table);

		// setting field list
		switch (this.type) {
		case INSERT:
			rawQueryCopy = rawQueryCopy.replaceFirst("!", fieldList);
			rawQueryCopy = rawQueryCopy.replaceFirst("!",
					this.questionMarkFieldList);
			break;
		case UPDATE:
			rawQueryCopy = rawQueryCopy.replaceFirst("!", fieldList);
			rawQueryCopy = rawQueryCopy.replaceAll(",", " = ?, ");
			break;
		}
		switch (this.type) {
		case UPDATE:
		case DELETE:
			rawQueryCopy = rawQueryCopy.replaceFirst("!",
					this.buildWhereChunk());
			break;
		}

		return rawQueryCopy;
	}

	@Override
	public void setTable(BaseTable table) throws InvalidBaseClass {
		if (table instanceof User) {
			this.table = (User) table;
		}
	}

	@Override
	public boolean executeSQL() {
		String sql = this.createSQL();

		try {
			if (!MySQLAccess.getConnection().isValid(1000)) {
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		boolean status = false;

		try {
			if (this.type == SQLStamentType.UPDATE
					|| this.type == SQLStamentType.INSERT) {
				short index = 1;
				PreparedStatement st = MySQLAccess.getConnection()
						.prepareStatement(sql);
				setImageAsBlob(st, index++);
				st.setString(index++, table.getLastName());
				st.setString(index++, table.getFirstName());
				st.setString(index++, table.getPassword());
				st.setString(index++, table.getEmail());
				st.setString(index++, table.getLogin());
				// TODO polish diacritis are problem
				// TODO no valid return statement confirming that sql was
				// executed successfully
				status = st.execute();
			} else {
				MySQLAccess.getConnection().prepareStatement(sql)
						.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return status;
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
}
