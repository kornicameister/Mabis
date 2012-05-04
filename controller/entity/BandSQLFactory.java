package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.Band;
import model.entity.Genre;
import controller.SQLStamentType;

public class BandSQLFactory extends AuthorSQLFactory {
	private final TreeSet<Band> bands = new TreeSet<Band>();

	public BandSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		Band band = (Band) this.table;
		switch (this.type) {
		case INSERT:
			this.insertGenres(band.getTagCloud());// inserting genres
			this.insertEntity(band, st);// inserting band itself
			break;
		case DELETE:
			st.setInt(1, band.getPrimaryKey());
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

	/**
	 * Metoda stara się umieścić w bazie danych nowe gatunki. Niemniej, jeśli
	 * dany gatunek jest już w bazie danych, to nie jest on ponownie
	 * umieszczany.
	 * 
	 * @param genres
	 * @throws SQLException
	 */
	private void insertGenres(ArrayList<Genre> genres) throws SQLException {
		GenreSQLFactory gsf = new GenreSQLFactory(SQLStamentType.SELECT,
				genres.get(0));
		gsf.reset();
		for (Genre genre : genres) {
			gsf.setTable(genre);
			gsf.addWhereClause("idGenre", genre.getPrimaryKey().toString());
			gsf.executeSQL(true);
			if (gsf.getGenres().isEmpty()) {
				gsf.setStatementType(SQLStamentType.INSERT);
				this.lastAffactedId = gsf.executeSQL(true);
				genre.setPrimaryKey(genre.getPrimaryKey());
			}
			gsf.reset();
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		Band band = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						band = (Band) objectIn.readObject();
						band.setPrimaryKey(set.getInt("idBand"));
						this.bands.add(band);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				band = null;
			}
			break;
		default:
			break;
		}
		set.close();
	}

	/**
	 * Zwraca pobrane zespoły
	 * 
	 * @return the bands
	 */
	public TreeSet<Band> getBands() {
		return bands;
	}

}
