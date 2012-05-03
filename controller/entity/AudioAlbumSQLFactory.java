/**
 * 
 */
package controller.entity;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import model.BaseTable;
import model.entity.AudioAlbum;
import model.entity.Band;
import model.entity.Picture;
import model.enums.ImageType;
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
		this.fetchAll = "SELECT * FROM mabis.AudioAlbumListView where !";
	}

	@Override
	protected void executeByTableAndType(PreparedStatement st)
			throws SQLException {
		short index = 1;
		switch (this.type) {
		case UPDATE:
			break;
		case INSERT:
			break;
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
		AudioAlbum a = null;
		switch (this.type) {
		case INSERT:
		case DELETE:
		case SELECT:
		case FETCH_ALL:
			while (set.next()) {
				a = new AudioAlbum(set.getInt("idAudio"));
				a.setOriginalTitle(set.getString("title"));
				a.setTrackList(set.getString("trackList"));
				a.setTotalTime(set.getTime("totalTime"));

				// creating front cover
				try {
					Picture frontCover = new Picture(
							set.getInt("frontCoverId"), ImageType.FRONT_COVER);
					frontCover.setImageFile(set.getString("frontCoverPath"),
							set.getString("frontCoverHash"));
					a.setFrontCover(frontCover);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// creating back cover
				try {
					Picture backCover = new Picture(set.getInt("backCoverId"),
							ImageType.BACK_COVER);
					backCover.setImageFile(set.getString("backCoverPath"),
							set.getString("backCoverHash"));
					a.setBackCover(backCover);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// creating cd cover
				try {
					Picture cdCover = new Picture(set.getInt("cdCoverId"),
							ImageType.CD_COVER);
					cdCover.setImageFile(set.getString("cdCoverPath"),
							set.getString("cdCoverHash"));
					a.setCDCover(cdCover);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// creating band
				Band band = new Band(set.getString("bandName"));
				band.setPrimaryKey(set.getInt("idBand"));

				// creating image for band
				try {
					Picture bandCover = new Picture(set.getInt("bandImageId"),
							ImageType.BAND);
					bandCover.setImageFile(set.getString("bandImagePath"),
							set.getString("bandImageHash"));
					band.setPicture(bandCover);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.audioAlbums.add(a);
			}
			break;
		default:
			break;
		}
	}

	public TreeSet<AudioAlbum> getValues() {
		return this.audioAlbums;
	}

}
