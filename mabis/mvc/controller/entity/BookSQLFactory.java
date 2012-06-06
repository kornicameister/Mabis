package mvc.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import mvc.controller.SQLStamentType;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.Book;
import utilities.Utilities;

/**
 * Klasa opisuje operacje bazodanowe, ktore wykonywane sa na tabeli movie
 * zgodnie z danymi i parametrami przekazanymi przez {@link Book} lub metody
 * dostępowe tejze klasy
 * 
 * @author tomasz
 * 
 */
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
			case INSERT :
				try {
					Book bb = (Book) this.checkIfInserted();
					if (bb != null) {
						this.lastAffactedId = bb.getPrimaryKey();
						this.entityAlreadyInserted = true;
						return;
					}
				} catch (SQLEntityExistsException e) {
					e.printStackTrace();
				}
			case UPDATE :
				short parameterIndex = 1;
				st.setInt(parameterIndex++, this.insertGenres(book.getGenres()));
				st.setInt(parameterIndex++, this.insertCover(book.getCover()));
				st.setInt(parameterIndex++, this.insertDirectors(book.getAuthors()));
				st.setObject(parameterIndex++, book);
				st.setString(parameterIndex++, book.getTitle());
				if (this.type == SQLStamentType.INSERT) {
					st.execute();
					this.lastAffactedId = Utilities.lastInsertedId(book, st);
					book.setPrimaryKey(this.lastAffactedId);
				} else {
					this.lastAffactedId = st.executeUpdate();
				}
				break;
			case DELETE :
				this.deletePicture(book.getCover());
				this.parseDeleteSet(st.executeUpdate());
				break;
			case SELECT :
				this.parseResultSet(st.executeQuery());
				break;
			default :
				break;
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Book book = null;
		switch (this.type) {
			case SELECT :
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
			default :
				break;
		}
		set.close();
	}

	public TreeSet<Book> getBooks() {
		return this.books;
	}

}
