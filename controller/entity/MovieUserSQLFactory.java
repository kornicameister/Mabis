package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;
import model.utilities.ForeignKeyPair;
import controller.SQLFactory;
import controller.SQLStamentType;

public class MovieUserSQLFactory extends SQLFactory {
	private final TreeSet<ForeignKeyPair> values = new TreeSet<ForeignKeyPair>();

	public MovieUserSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
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
		ForeignKey key_1 = null, key_2 = null;
		switch (this.type) {
		case INSERT:
		case DELETE:
		case SELECT:
			while (set.next()) {
				key_1 = new ForeignKey(TableType.MOVIE.toString(),
						"idMovie", set.getInt("idMovie"));
				key_2 = new ForeignKey(TableType.USER.toString(), "idUser",
						set.getInt("idUser"));
				this.values.add(new ForeignKeyPair(key_1, key_2));
			}
			break;
		case FETCH_ALL:
		default:
			break;
		}
	}
	
	public TreeSet<ForeignKeyPair> getMovieUserKeys() {
		return values;
	}

}
