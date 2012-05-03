package controller.entity;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Author;
import model.entity.Book;
import model.entity.Genre;
import model.entity.Movie;
import model.entity.Picture;
import model.enums.ImageType;
import model.enums.TableType;
import model.utilities.ForeignKey;
import controller.SQLFactory;
import controller.SQLStamentType;

public class BookSQLFactory extends SQLFactory {
	private final TreeSet<Book> books = new TreeSet<Book>();

	public BookSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
		this.fetchAll = "select * mabis.BookListView where !";
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		switch (this.type) {
		case INSERT:
		case DELETE:
		case SELECT:
		case FETCH_ALL:
			this.parseResultSet(st.executeQuery());
			break;
		default:
			break;
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		switch (this.type) {
		case INSERT:
		case DELETE:
		case SELECT:
		case FETCH_ALL:
			while (set.next()) {
				// creating genre
				Genre g = new Genre(set.getInt("idGenre"));
				g.setGenre(set.getString("genre"));

				// creating book cover
				Picture bookCover = new Picture(set.getInt("idPicture"),
						ImageType.FRONT_COVER);
				try {
					bookCover.setImageFile(set.getString("image"),
							set.getString("hash"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// creating book
				Book b = new Book(set.getInt("idBook"));
				b.setIsbn(set.getString("isbn"));
				b.setOriginalTitle("titleOriginal");
				b.setLocalizedTitle("titleLocale");
				b.setPages(set.getShort("pages"));
				b.setCover(bookCover);
				b.addForeingKey(new ForeignKey(TableType.PICTURE.toString(),
						"cover", bookCover.getPrimaryKey()));
			}
			break;
		default:
			break;
		}
	}

	public TreeSet<Book> getValues() {
		return this.books;
	}

}
