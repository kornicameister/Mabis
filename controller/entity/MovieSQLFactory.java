package controller.entity;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Author;
import model.entity.Genre;
import model.entity.Movie;
import model.entity.Picture;
import model.enums.ImageType;
import controller.SQLFactory;
import controller.SQLStamentType;

public class MovieSQLFactory extends SQLFactory {
	private final TreeSet<Movie> values = new TreeSet<Movie>();

	public MovieSQLFactory(SQLStamentType type, BaseTable table) {
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
				Genre g = new Genre(set.getInt("idGenre"));
				g.setGenre(set.getString("genre"));

				// creating cover
				Picture p = new Picture(set.getInt("coverId"),
						ImageType.FRONT_COVER);
				try {
					p.setImageFile(set.getString("coverImage"),
							set.getString("coverHash"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// creating authorAvatar
				Picture pp = new Picture(set.getInt("authorImageId"),
						ImageType.AVATAR);
				try {
					pp.setImageFile(set.getString("authorImageFile"),
							set.getString("authorImageHash"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// creating director
				Author a = new Author(set.getInt("idAuthor"));
				a.setFirstName(set.getString("authorFirstName"));
				a.setLastName(set.getString("authorLastName"));
				a.setPicture(pp);

				// creating movie
				Movie m = new Movie(set.getInt("idMovie"));
				m.setOriginalTitle(set.getString("titleOriginal"));
				m.setLocalizedTitle(set.getString("titleLocale"));
				m.setDuration(set.getTime("duration"));
				m.setGenre(g);
				m.setFrontCover(p);
				m.setDirector(a);
				this.values.add(m);
			}
			break;
		default:
			break;
		}
	}

	public TreeSet<Movie> getValues() {
		return values;
	}

}
