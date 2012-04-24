package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.utilities.ForeignKeyPair;

import controller.SQLFactory;
import controller.SQLStamentType;

public class BookUserSQLFactory extends SQLFactory{

	public BookUserSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {		
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		
	}

	public TreeSet<ForeignKeyPair> getBookUserKeys() {
		return null;
	}

}
