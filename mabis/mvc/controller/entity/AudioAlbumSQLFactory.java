/**
 * 
 */
package mvc.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import mvc.controller.SQLStamentType;
import mvc.controller.exceptions.SQLEntityExistsException;
import mvc.model.BaseTable;
import mvc.model.entity.AudioAlbum;
import utilities.Utilities;

/**
 * Klasa opisuje operacje bazodanowe, ktore wykonywane sa na tabeli movie
 * zgodnie z danymi i parametrami przekazanymi przez {@link AudioAlbum} lub
 * metody dostępowe tejze klasy
 * 
 * @author tomasz
 * 
 */
public class AudioAlbumSQLFactory extends MovieSQLFactory {
	TreeSet<AudioAlbum> audioAlbums = new TreeSet<AudioAlbum>();

	public AudioAlbumSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		AudioAlbum am = (AudioAlbum) this.table;
		switch (this.type) {
			case INSERT :
				try {
					AudioAlbum tmp = (AudioAlbum) this.checkIfInserted();
					if (tmp != null) {
						this.lastAffactedId = tmp.getPrimaryKey();
						this.entityAlreadyInserted = true;
						return;
					}
				} catch (SQLEntityExistsException e) {
					e.printStackTrace();
				}
			case UPDATE :
				short parameterIndex = 1;
				st.setInt(parameterIndex++, this.insertGenres(am.getGenres()));
				st.setInt(parameterIndex++, this.insertCover(am.getCover()));
				st.setInt(parameterIndex++,
						this.insertDirectors(am.getAuthors()));
				st.setObject(parameterIndex++, am);
				st.setString(parameterIndex++, am.getTitle());
				if (this.type == SQLStamentType.INSERT) {
					st.execute();
					this.lastAffactedId = Utilities.lastInsertedId(am, st);
					am.setPrimaryKey(this.lastAffactedId);
				} else {
					this.lastAffactedId = st.executeUpdate();
				}
				break;
			case DELETE :
				this.deletePicture(am.getCover());
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
	protected void parseResultSet(ResultSet set) throws SQLException {
		AudioAlbum audioAlbum = null;
		switch (this.type) {
			case SELECT :
				while (set.next()) {
					byte[] buf = set.getBytes("object");
					if (buf != null) {
						try {
							ObjectInputStream objectIn = new ObjectInputStream(
									new ByteArrayInputStream(buf));
							audioAlbum = (AudioAlbum) objectIn.readObject();
							audioAlbum
									.setPrimaryKey(set.getInt("idAudioAlbum"));
							this.audioAlbums.add(audioAlbum);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					audioAlbum = null;
				}
				break;
			default :
				break;
		}
		set.close();
	}

	public TreeSet<AudioAlbum> getValues() {
		return this.audioAlbums;
	}

	@Override
	public BaseTable checkIfInserted() throws SQLException,
			SQLEntityExistsException {
		AudioAlbumSQLFactory aasf = new AudioAlbumSQLFactory(
				SQLStamentType.SELECT, this.table);
		aasf.addWhereClause("title", this.table.getTitle());
		aasf.executeSQL(true);
		for (AudioAlbum mm : aasf.getValues()) {
			return mm;
		}
		return null;
	}

}
