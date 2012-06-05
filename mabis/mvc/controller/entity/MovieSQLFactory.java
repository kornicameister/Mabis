package mvc.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import mvc.controller.SQLFactory;
import mvc.controller.SQLStamentType;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.Author;
import mvc.model.entity.Genre;
import mvc.model.entity.Movie;
import mvc.model.entity.Picture;
import utilities.Utilities;

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
			case INSERT :
				try {
					Movie tmp = (Movie) this.checkIfInserted();
					if (tmp != null) {
						this.lastAffactedId = tmp.getPrimaryKey();
						this.entityAlreadyInserted = true;
						return;
					}
				} catch (SQLEntityExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				short parameterIndex = 1;
				st.setInt(parameterIndex++, this.insertGenres(movie.getGenres()));
				st.setInt(parameterIndex++, this.insertCover(movie.getCover()));
				st.setInt(parameterIndex++, this.insertDirectors(movie.getAuthors()));
				st.setObject(parameterIndex++, movie);
				st.setString(parameterIndex++, movie.getTitle());
				st.execute();
				this.lastAffactedId = Utilities.lastInsertedId(movie, st);
				movie.setPrimaryKey(this.lastAffactedId);
				break;
			case UPDATE :
				parameterIndex = 1;
				st.setInt(parameterIndex++,
						this.insertGenres(movie.getGenres()));
				st.setInt(parameterIndex++, this.insertCover(movie.getCover()));
				st.setInt(parameterIndex++,
						this.insertDirectors(movie.getAuthors()));
				st.setObject(parameterIndex++, movie);
				st.setString(parameterIndex++, movie.getTitle());
				this.lastAffactedId = st.executeUpdate();
				break;
			case DELETE :
				this.deletePicture(movie.getCover());
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
	public BaseTable checkIfInserted() throws SQLException,
			SQLEntityExistsException {
		MovieSQLFactory msf = new MovieSQLFactory(SQLStamentType.SELECT,
				this.table);
		msf.addWhereClause("title", this.table.getTitle());
		msf.executeSQL(true);
		for (Movie mm : msf.getMovies()) {
			return mm;
		}
		return null;
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
		try {
			this.lastAffactedId = psf.executeSQL(false);
		} catch (SQLEntityExistsException e) {
			e.printStackTrace();
		}
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
	protected Integer insertDirectors(TreeSet<Author> directors)
			throws SQLException {
		this.lastAffactedId = -1;
		for (Author director : directors) {
			if (director.getPrimaryKey() < 0) {
				try {
					AuthorSQLFactory asf = new AuthorSQLFactory(
							SQLStamentType.INSERT, director);
					if (this.lastAffactedId < 0) {
						this.lastAffactedId = asf.executeSQL(false);
					} else {
						asf.executeSQL(false);
					}
				} catch (SQLEntityExistsException e) {
					e.printStackTrace();
				}
			}
		}
		if (lastAffactedId < 0) {
			this.lastAffactedId = ((Author) directors.toArray()[0])
					.getPrimaryKey();
		}
		return this.lastAffactedId;
	}

	/**
	 * Metoda umieszcza w bazie danych informacje o gatunku danego filmu. Jeśli
	 * dany gatunek jest już w bazie danych, wtedy pobierany jest jego numer
	 * identyfikacyjny
	 * 
	 * @param genres
	 * 
	 * @param genres
	 *            gatunek filmu
	 * @return numer identyfikacynyjny gatunku
	 * @throws SQLException
	 */
	protected Integer insertGenres(TreeSet<Genre> genres) throws SQLException {
		this.lastAffactedId = -1;
		for (Genre genre : genres) {
			if (genre.getPrimaryKey() < 0) {
				try {
					GenreSQLFactory gsf = new GenreSQLFactory(
							SQLStamentType.INSERT, genre);
					if (this.lastAffactedId < 0) {
						this.lastAffactedId = gsf.executeSQL(false);
					} else {
						gsf.executeSQL(false);
					}
				} catch (SQLEntityExistsException e) {
					e.printStackTrace();
				}
			}
		}
		if (lastAffactedId < 0) {
			this.lastAffactedId = ((Genre) genres.toArray()[0]).getPrimaryKey();
		}
		return this.lastAffactedId;
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Movie movie = null;
		switch (this.type) {
			case SELECT :
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
			default :
				break;
		}
		set.close();
	}

	public TreeSet<Movie> getMovies() {
		return movies;
	}

}
