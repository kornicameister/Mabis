/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * Klasa jest obiektową reprezentacją tabeli audioAlbum z bazy danych mabis.
 * Posiada następujące atrybuty: </br> | idAudio </br> | cover</br> |
 * backCover</br> | cdCover</br> | tagCloud</br> | trackList</br> | artist</br>
 * | totalTime
 * 
 * @author kornicameister
 * @version 0.2
 */
public class AudioAlbum extends BaseTable implements Serializable {
	private static final long serialVersionUID = -6884151728501220580L;
	private ArrayList<Genre> tagCloud = null;
	private Picture cover;
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

	public AudioAlbum() {
		super();
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idAudio", "object", "coverFK", "artistFK" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.tagCloud = new ArrayList<Genre>();
		this.totalTime = new Time(0);
		this.tableName = TableType.AUDIO_ALBUM.toString();
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

	public Picture getCover() {
		return this.cover;
	}

	public void setCover(Picture c) {
		this.cover = c;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getOriginalTitle() + "]\n";
		str += "[BAND: " + this.getBand() + "]\n";
		str += "[TAGCLOUD: " + this.getTagCloud() + "]\n";
		str += "[DURATION: " + this.getTotalTime() + "]\n";
		str += "[COVER:" + this.getCover() + "]\n";
		return str;
	}

}
