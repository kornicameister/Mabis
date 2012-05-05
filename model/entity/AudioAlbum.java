/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
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
public class AudioAlbum extends Movie implements Serializable {
	private static final long serialVersionUID = -6884151728501220580L;
	private ArrayList<Genre> tagCloud = null;

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
		super.initInternalFields();
		this.tagCloud = new ArrayList<Genre>();
		this.tableName = TableType.AUDIO_ALBUM.toString();
	}

	public ArrayList<Genre> getTagCloud() {
		return this.tagCloud;
	}

	public void setTagCloud(ArrayList<Genre> tagCloud) {
		this.tagCloud = tagCloud;
	}

	public void addTag(Genre genre) {
		this.tagCloud.add(genre);
	}

	public String getTrackList() {
		return this.getLocalizedTitle();
	}

	public void setTrackList(String trackList) {
		this.setLocalizedTitle(trackList);
	}

	public Band getBand() {
		return (Band) this.director;
	}

	public void setBand(Band band) {
		this.director = band;
	}

	@Override
	public String toString() {
		String str = BaseTable.class.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getOriginalTitle() + "]\n";
		str += "[BAND: " + this.getBand() + "]\n";
		str += "[TAGCLOUD: " + this.getTagCloud() + "]\n";
		str += "[DURATION: " + this.getDuration() + "]\n";
		str += "[COVER:" + this.getCover() + "]\n";
		return str;
	}

}
