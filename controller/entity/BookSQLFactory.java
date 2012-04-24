package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.AudioAlbum;
import model.entity.Book;

import controller.SQLFactory;
import controller.SQLStamentType;

public class BookSQLFactory extends SQLFactory {

	public BookSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		// TODO Auto-generated method stub

	}

	public TreeSet<Book> getValues() {
		return null;
	}

}
