package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Book;
import utilities.Utilities;
import controller.SQLStamentType;

public class BookSQLFactory extends MovieSQLFactory {
	private final TreeSet<Book> books = new TreeSet<Book>();

	public BookSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Book book = (Book) this.table;
		switch (this.type) {
		case INSERT:
			short parameterIndex = 1;
			// TODO add transactions
			st.setInt(parameterIndex++, this.insertDirector(book.getWriter()));
			st.setInt(parameterIndex++, this.insertGenre(book.getGenre()));
			st.setInt(parameterIndex++, this.insertCover(book.getCover()));
			st.setObject(parameterIndex++, book);
			st.execute();
			st.clearParameters();
			this.lastAffactedId = Utilities.lastInsertedId(book, st);
			break;
		case DELETE:
			st.setInt(1, book.getPrimaryKey());
			this.parseDeleteSet(st.executeUpdate());
			break;
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		default:
			break;
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Book book = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						book = (Book) objectIn.readObject();
						book.setPrimaryKey(set.getInt(("idBook")));
						this.books.add(book);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				book = null;
			}
			break;
		default:
			break;
		}
		set.close();
	}

	public TreeSet<Book> getBooks() {
		return this.books;
	}

}
