/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.sql.Time;
import java.util.ArrayList;

import model.enums.TableType;
import model.utilities.ForeignKey;
import exceptions.SQLForeingKeyNotFound;

/**
 * @author kornicameister
 * 
 */
public class AudioAlbum extends BaseTable {
	private ArrayList<Genre> tagCloud = null;
	private String trackList = null;
	private Time totalTime = null;
	private String description = null;

	/**
	 * Construct audioAlbum with following title and tracklist
	 * 
	 * @param title
	 * @param trackList
	 */
	public AudioAlbum(String title, String trackList) {
		super();
		this.setOriginalTitle(title);
		this.trackList = trackList;
	}

	/**
	 * @see BaseTable#BaseTable(int, ForeignKey...)
	 * @param pk
	 * @param keys
	 */
	public AudioAlbum(int pk, ForeignKey... keys) {
		super(pk, keys);
	}

	/**
	 * @see BaseTable#BaseTable(int)
	 * @param pk
	 */
	public AudioAlbum(int pk) {
		super(pk);
	}

	@Override
	protected void initInternalFields() {
		this.tableName = TableType.AUDIO_ALBUM.toString();
		this.tagCloud = new ArrayList<Genre>();
		this.trackList = new String();
		this.totalTime = new Time(0);
		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.AUTHOR);
		this.reloadMetaData();
	}

	@Override
	public void reloadMetaData() {
		this.metaData.clear();
		try {
			this.metaData.put("idAudio", this.getPrimaryKey().toString());
			this.metaData.put("frontCover", this.getForeingKey("frontCover")
					.getValue().toString());
			this.metaData.put("backCover", this.getForeingKey("backCover")
					.getValue().toString());
			this.metaData.put("cdCover", this.getForeingKey("cdCover")
					.getValue().toString());
			this.metaData.put("tagCloud", this.getTagCloud());
			this.metaData.put("trackList", this.getTrackList());
			this.metaData.put("artist", this.getForeingKey("arist").getValue()
					.toString());
			this.metaData.put("totalTime", this.totalTime.toString());
		} catch (SQLForeingKeyNotFound e) {
			e.printStackTrace();
		} finally {
			this.metaData.clear();
		}
	}

	/**
	 * @return the tagCloud
	 */
	public String getTagCloud() {
		String t = new String();
		for (short i = 0; i < this.tagCloud.size(); i++) {
			t += this.tagCloud.get(i).getGenre() + ",";
		}
		return t.substring(0, t.length() - 1);
	}

	/**
	 * @param tagCloud
	 *            {
	 *            "idAudio,frontCover,backCover,cdCover,tagCloud,trackList,artist,totalTime"
	 *            }; the tagCloud to set
	 */
	public void setTagCloud(ArrayList<Genre> tagCloud) {
		this.tagCloud = tagCloud;
	}

	/**
	 * @return the trackList
	 */
	public String getTrackList() {
		return trackList;
	}

	/**
	 * @param trackList
	 *            the trackList to set
	 */
	public void setTrackList(String trackList) {
		this.trackList = trackList;
	}

	/**
	 * @return the totalTime
	 */
	public Time getTotalTime() {
		return totalTime;
	}

	/**
	 * @param totalTime
	 *            the totalTime to set
	 */
	public void setTotalTime(Time totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
