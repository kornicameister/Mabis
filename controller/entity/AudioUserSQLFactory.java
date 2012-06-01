package controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import utilities.Utilities;

import model.BaseTable;
import model.entity.AudioUser;
import model.enums.TableType;
import model.utilities.ForeignKey;
import model.utilities.ForeignKeyPair;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * Klasa definiująca podstawową funkcjonalność bazodanową dla tabeli
 * wiele-do-wielu audioUser. Pozwala na wykonywaniu operacji:
 * <ul>
 * <li>insert</li>
 * <li>update</li>
 * <li>delete</li>
 * </ul>
 * 
 * @author kornicameister
 * @see AudioUser
 */
public class AudioUserSQLFactory extends SQLFactory {
	private final TreeSet<ForeignKeyPair> values = new TreeSet<ForeignKeyPair>();

	public AudioUserSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		AudioUser au = null;
		switch (this.type) {
		case INSERT:
			au = (AudioUser) this.table;
			st.setInt(1, au.getMultiForeing(-1).getKey("idAudio").getValue());
			st.setInt(2, au.getMultiForeing(-1).getKey("idUser").getValue());
			st.execute();
			this.lastAffactedId = Utilities.lastInsertedId(au, st);
			au.setPrimaryKey(this.lastAffactedId);
			break;
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
		ForeignKey key_1 = null, key_2 = null;
		switch (this.type) {
		case INSERT:
		case DELETE:
		case SELECT:
			while (set.next()) {
				key_1 = new ForeignKey(TableType.AUDIO_ALBUM,
						"idAudio", set.getInt("idAudio"));
				key_2 = new ForeignKey(TableType.USER, "idUser",
						set.getInt("idUser"));
				this.values.add(new ForeignKeyPair(key_1, key_2));
			}
			break;
		default:
			break;
		}
		set.close();
	}

	public TreeSet<ForeignKeyPair> getAudioUserKeys() {
		return values;
	}

	@Override
	public Boolean checkIfInserted() throws SQLException {
		return true;
	}

}
