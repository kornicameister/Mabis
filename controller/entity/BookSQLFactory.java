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
import model.entity.Picture;
import model.enums.ImageType;
import controller.SQLFactory;
import controller.SQLStamentType;

public class BookSQLFactory extends SQLFactory {
	private final TreeSet<Book> books = new TreeSet<Book>();

	public BookSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		switch (this.type) {
		case INSERT:
		case DELETE:
		case SELECT:
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
			while (set.next()) {
				// creating genre
				Genre genre = new Genre(set.getInt("idGenre"));
				genre.setGenre(set.getString("genre"));

				// creating book cover
				Picture bookCover = new Picture(set.getInt("idPicture"),
						ImageType.FRONT_COVER);
				// creating author image
				Picture authorPicture = new Picture(
						set.getInt("authorImageId"), ImageType.AUTHOR);
				try {
					bookCover.setImageFile(set.getString("image"),
							set.getString("hash"));
					authorPicture.setImageFile(
							set.getString("authorImageFile"),
							set.getString("authorImageHash"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// creating author
				Author author = new Author(set.getInt("idAuthor"));
				author.setFirstName(set.getString("firstName"));
				author.setLastName(set.getString("lastName"));
				author.setPicture(authorPicture);

				// creating book
				Book b = new Book(set.getInt("idBook"));
				b.setIsbn(set.getString("isbn"));
				b.setOriginalTitle("titleOriginal");
				b.setLocalizedTitle("titleLocale");
				b.setPages(set.getShort("pages"));
				b.setCover(bookCover);
				b.setWriter(author);
				b.setGenre(genre);
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
