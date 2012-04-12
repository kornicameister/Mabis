/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.TreeMap;

import model.enums.CoverType;
import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * This class represents table of AudioAlbum from Mabis database. Table
 * structure: </br> | idAudio </br> | frontCover</br> | backCover</br> |
 * cdCover</br> | tagCloud</br> | trackList</br> | artist</br> | totalTime
 * 
 * @author kornicameister
 * @version 0.2
 */
public class AudioAlbum extends BaseTable {
	private ArrayList<Genre> tagCloud = null;
	private TreeMap<CoverType, Cover> covers = null; // map of covers
	private Time totalTime = null;
	private Band band = null;

	/**
	 * Construct audioAlbum with following title and tracklist
	 * 
	 * @param title
	 * @param trackList
	 */
	public AudioAlbum(String title, String trackList) {
		super();
		this.titles[0] = title;
		this.titles[1] = trackList;
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
		this.totalTime = new Time(0);
		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.AUTHOR);
	}

	public String getTagCloud() {
		String t = new String();
		for (short i = 0; i < this.tagCloud.size(); i++) {
			t += this.tagCloud.get(i).getGenre() + ",";
		}
		return t.substring(0, t.length() - 1);
	}

	public void setTagCloud(ArrayList<Genre> tagCloud) {
		this.tagCloud = tagCloud;
	}

	public String getTrackList() {
		return this.getLocalizedTitle();
	}

	public void setTrackList(String trackList) {
		this.setLocalizedTitle(trackList);
	}

	public Time getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Time totalTime) {
		this.totalTime = totalTime;
	}

	public String getDescription() {
		return this.titles[2];
	}

	public void setDescription(String description) {
		this.titles[2] = description;
	}

	public Band getBand() {
		return band;
	}

	public void setBand(Band band) {
		this.band = band;
	}

	public void setFrontCover(Cover fc) {
		this.covers.put(CoverType.FRONT_COVER, fc);
	}

	public void setBackCover(Cover fc) {
		this.covers.put(CoverType.BACK_COVER, fc);
	}

	public Cover getFrontCover() {
		return this.covers.get(CoverType.FRONT_COVER);
	}

	public Cover getBackCover() {
		return this.covers.get(CoverType.BACK_COVER);
	}

	public Cover getCDCover() {
		return this.covers.get(CoverType.CD_COVER);
	}

	public void setCDCover(Cover fc) {
		this.covers.put(CoverType.CD_COVER, fc);
	}

}
