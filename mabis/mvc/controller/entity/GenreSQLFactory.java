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
import mvc.model.BaseTable;
import mvc.model.entity.Genre;
import mvc.model.enums.GenreType;
import utilities.Utilities;

public class GenreSQLFactory extends SQLFactory {
	private final TreeSet<Genre> genres = new TreeSet<Genre>();

	public GenreSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Genre genre = (Genre) this.table;
		switch (this.type) {
		case INSERT:
			st.setString(1, genre.getType().toString());
			st.setObject(2, genre);
			st.execute();
			this.lastAffactedId = Utilities.lastInsertedId(genre, st);
			genre.setPrimaryKey(this.lastAffactedId);
			break;
		case DELETE:
			st.setInt(1, genre.getPrimaryKey());
			this.parseDeleteSet(st.executeUpdate());
			break;
		case UPDATE:
		case SELECT:
			this.parseResultSet(st.executeQuery());
			break;
		default:
			break;
		}

	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Genre genre = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						genre = (Genre) objectIn.readObject();
						genre.setPrimaryKey(set.getInt("idGenre"));
						genre.setType(GenreType.valueOf(set.getString("type")));
						this.genres.add(genre);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				genre = null;
			}
			break;
		default:
			break;
		}
		set.close();
	}

	@Override
	public void reset() {
		super.reset();
		this.genres.clear();
	}

	/**
	 * 
	 * @return pobrane gatunki z bazy danych
	 */
	public TreeSet<Genre> getGenres() {
		return this.genres;
	}

	@Override
	public BaseTable checkIfInserted() throws SQLException {
		return this.table;
	}

}
