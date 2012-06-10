package mvc.controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import mvc.controller.SQLStamentType;
import mvc.controller.dispatcher.SQLFactory;
import mvc.model.BaseTable;
import mvc.model.entity.MovieUser;
import mvc.model.enums.TableType;
import mvc.model.utilities.ForeignKey;
import mvc.model.utilities.ForeignKeyPair;
import utilities.Utilities;

/**
 * Klasa opisuje operacje bazodanowe na tabeli <strong>movie-user</strong>
 * 
 * @author tomasz
 * 
 */
public class MovieUserSQLFactory extends SQLFactory {
	private final TreeSet<ForeignKeyPair> values = new TreeSet<ForeignKeyPair>();

	public MovieUserSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		switch (this.type) {
			case INSERT :
				MovieUser au = (MovieUser) this.table;
				st.setInt(2, au.getMultiForeing(-1).getKey("idMovie")
						.getValue());
				st.setInt(1, au.getMultiForeing(-1).getKey("idUser").getValue());
				st.execute();
				this.lastAffactedId = Utilities.lastInsertedId(au, st);
				au.setPrimaryKey(this.lastAffactedId);
				break;
			case DELETE :
			case SELECT :
				this.parseResultSet(st.executeQuery());
				break;
			default :
				break;
		}
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		ForeignKey key_1 = null, key_2 = null;
		switch (this.type) {
			case INSERT :
			case DELETE :
			case SELECT :
				while (set.next()) {
					key_1 = new ForeignKey(TableType.MOVIE, "idMovie",
							set.getInt("idMovie"));
					key_2 = new ForeignKey(TableType.USER, "idUser",
							set.getInt("idUser"));
					this.values.add(new ForeignKeyPair(key_1, key_2));
				}
				break;
			default :
				break;
		}
		set.close();
	}

	public TreeSet<ForeignKeyPair> getMovieUserKeys() {
		return values;
	}

	@Override
	public BaseTable checkIfInserted() throws SQLException {
		return this.table;
	}

}
