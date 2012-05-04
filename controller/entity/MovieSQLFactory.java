package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Author;
import model.entity.Genre;
import model.entity.Movie;
import model.entity.Picture;
import utilities.Utilities;
import controller.SQLFactory;
import controller.SQLStamentType;

public class MovieSQLFactory extends SQLFactory {
	private final TreeSet<Movie> movies = new TreeSet<Movie>();

	public MovieSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Movie movie = (Movie) this.table;
		switch (this.type) {
		case INSERT:
			short parameterIndex = 1;
			// TODO add transactions
			st.setInt(parameterIndex++, this.insertGenre(movie.getGenre()));
			st.setInt(parameterIndex++, this.insertDirector(movie.getDirector()));
			st.setInt(parameterIndex++, this.insertCover(movie.getCover()));
			st.setObject(parameterIndex++, movie);
			st.execute();
			st.clearParameters();
			this.lastAffactedId = Utilities.lastInsertedId(movie, st);
			break;
		case DELETE:
			st.setInt(1, movie.getPrimaryKey());
			this.parseDeleteSet(st.executeUpdate());
			break;
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		default:
			break;
		}
	}

	/**
	 * Metoda umieszcza w bazie danych informacje o okładce dla danego filmu.
	 * Nie sprawdzane jest, czy okładka już istnieje. Wewnętrznie sprawdza to
	 * klasa {@link PictureSQLFactory}, która prócz operowania na bazie danych
	 * ma możliwość operacji na cache grafik. Niemniej może to być proces
	 * długotrwały, jako że {@link PictureSQLFactory} może być zmuszony do
	 * przejrzenia kilkudziesięciu grafik.
	 * 
	 * @param cover
	 *            okładka filmu
	 * @return numer identyfikacyjny okładki
	 * @throws SQLException
	 */
	protected Integer insertCover(Picture cover) throws SQLException {
		PictureSQLFactory psf = new PictureSQLFactory(SQLStamentType.INSERT,
				cover);
		this.lastAffactedId = psf.executeSQL(localDatabase);
		cover.setPrimaryKey(this.lastAffactedId);
		return lastAffactedId;
	}

	/**
	 * Metoda umieszcza w bazie danych informacje o reżyserze danego filmu.
	 * Jeśli dany reżyser jest już w bazie danych, wtedy pobierany jest jedynie
	 * jego numer identyfikacyjny.
	 * 
	 * @param director
	 *            reżyser filmu
	 * @return numer identyfikacyjny reżysera filmu
	 * @throws SQLException
	 */
	protected Integer insertDirector(Author director) throws SQLException {
		AuthorSQLFactory asf = new AuthorSQLFactory(SQLStamentType.SELECT,
				director);
		asf.addWhereClause("idAuthor", director.getPrimaryKey().toString());
		asf.executeSQL(true);
		if (asf.getAuthors().isEmpty()) {
			asf.setStatementType(SQLStamentType.INSERT);
			this.lastAffactedId = asf.executeSQL(true);
			director.setPrimaryKey(this.lastAffactedId);
			return this.lastAffactedId;
		} else {
			for (Author a : asf.getAuthors()) {
				this.lastAffactedId = a.getPrimaryKey();
				return this.lastAffactedId;
			}
		}
		return 0;
	}

	/**
	 * Metoda umieszcza w bazie danych informacje o gatunku danego filmu. Jeśli
	 * dany gatunek jest już w bazie danych, wtedy pobierany jest jego numer
	 * identyfikacyjny
	 * 
	 * @param genre
	 *            gatunek filmu
	 * @return numer identyfikacynyjny gatunku
	 * @throws SQLException
	 */
	protected Integer insertGenre(Genre genre) throws SQLException {
		this.lastAffactedId = -1;
		GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT, genre);
		gsf.addWhereClause("idGenre", genre.getPrimaryKey().toString());
		gsf.executeSQL(true);
		if (gsf.getGenres().isEmpty()) {
			// no such genre, inserting
			gsf.setStatementType(SQLStamentType.INSERT);
			this.lastAffactedId = gsf.executeSQL(true);
			genre.setPrimaryKey(this.lastAffactedId);
		} else {
			for (Genre g : gsf.getGenres()) {
				this.lastAffactedId = g.getPrimaryKey();
				break;
			}
		}
		return this.lastAffactedId;
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Movie movie = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						movie = (Movie) objectIn.readObject();
						movie.setPrimaryKey(set.getInt(("idMovie")));
						this.movies.add(movie);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				movie = null;
			}
			break;
		default:
			break;
		}
		set.close();
	}

	public TreeSet<Movie> getMovies() {
		return movies;
	}

}
