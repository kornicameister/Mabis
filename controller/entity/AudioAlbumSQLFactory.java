/**
 * 
 */
package controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.AudioAlbum;
import model.entity.Band;
import model.entity.Picture;
import utilities.Utilities;
import controller.SQLFactory;
import controller.SQLStamentType;

/**
 * @author kornicameister
 * 
 */
public class AudioAlbumSQLFactory extends SQLFactory {
	TreeSet<AudioAlbum> audioAlbums = new TreeSet<AudioAlbum>();

	public AudioAlbumSQLFactory(SQLStamentType type, BaseTable table) {
		super(type, table);
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		AudioAlbum am = (AudioAlbum) this.table;
		switch (this.type) {
		case UPDATE:
			break;
		case INSERT:
			short parameterIndex = 1;
			// inserting covers
			int frontCover = this.insertCover(am.getFrontCover());
			int backCover = this.insertCover(am.getBackCover());
			int cdCover = this.insertCover(am.getCDCover());
			// setting band
			int band = this.setBand(am.getBand());
			st.setObject(parameterIndex++, am);
			st.setInt(parameterIndex++, frontCover);
			st.setInt(parameterIndex++, backCover);
			st.setInt(parameterIndex++, cdCover);
			st.setInt(parameterIndex++, band);
			st.execute();
			st.clearParameters();
			this.lastAffactedId = Utilities.lastInsertedId(am, st);
			break;
		case DELETE:
			st.setInt(1, am.getPrimaryKey());
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
	 * Metoda działa w dwojaki sposób. Jeśli w bazie danych istnieje już zespół,
	 * identyfikowany przez parametr <b>band</b>, to pobierany jest jego numer
	 * identyfikacyjny. W drugim wypadku, zespół jest umieszczany w bazie
	 * danych, a następnie pobierany jest jego numer identyfikacyjny.
	 * 
	 * @param band
	 *            zespół który jest autorem tego {@link AudioAlbum}
	 * @return klucz główny zespołu dla danego albumu
	 * @throws SQLException
	 */
	private Integer setBand(Band band) throws SQLException {
		BandSQLFactory bsf = new BandSQLFactory(SQLStamentType.SELECT, band);
		bsf.addWhereClause("idBand", band.getPrimaryKey().toString());
		bsf.executeSQL(true);
		if (bsf.getBands().isEmpty()) {
			bsf.setStatementType(SQLStamentType.INSERT);
			this.lastAffactedId = bsf.executeSQL(true);
			band.setPrimaryKey(this.lastAffactedId);
		}
		return this.lastAffactedId;
	}

	/**
	 * Metoda odpowiedzialna jest za umieszczenie w bazie danych informacji o
	 * okładce danego albumu
	 * 
	 * @param cover
	 * @return numer identyfikacyjny ostatnio umieszczonej okładki w bazie danych
	 * @throws SQLException
	 */
	private Integer insertCover(Picture cover) throws SQLException {
		PictureSQLFactory psf = new PictureSQLFactory(SQLStamentType.INSERT,
				cover);
		this.lastAffactedId = psf.executeSQL(localDatabase);
		cover.setPrimaryKey(this.lastAffactedId);
		return lastAffactedId;
	}

	@Override
	protected void parseResultSet(ResultSet set) throws SQLException {
		AudioAlbum audioAlbum = null;
		switch (this.type) {
		case SELECT:
			while (set.next()) {
				byte[] buf = set.getBytes("object");
				if (buf != null) {
					try {
						ObjectInputStream objectIn = new ObjectInputStream(
								new ByteArrayInputStream(buf));
						audioAlbum = (AudioAlbum) objectIn.readObject();
						audioAlbum.setPrimaryKey(set.getInt("idAudio"));
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
		default:
			break;
		}
		set.close();
	}

	public TreeSet<AudioAlbum> getValues() {
		return this.audioAlbums;
	}

}
