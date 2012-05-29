/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.Serializable;
import java.util.TreeSet;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.AudioAlbumTrack;
import model.utilities.ForeignKey;

/**
 * Klasa jest obiektową reprezentacją tabeli audioAlbum z bazy danych mabis.
 * Posiada następujące atrybuty: </br> | idAudio </br> | title </br> |
 * cover</br> | tagCloud</br> | trackList</br> | artist</br> | totalTime
 * 
 * @author kornicameister
 * @version 0.2
 */
public class AudioAlbum extends Movie implements Serializable {
	private static final long serialVersionUID = -6884151728501220580L;
	private TreeSet<AudioAlbumTrack> trackList;

	/**
	 * Construct audioAlbum with following title and tracklist
	 * 
	 * @param title
	 * @param treeSet
	 */
	public AudioAlbum(String title, TreeSet<AudioAlbumTrack> set) {
		super();
		this.titles[0] = title;
		this.trackList = set;
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
		String tmp[] = { "idAudio",
						"object", 
						"title", 
						"bandFK", 
						"coverFK", 
						"genreFK" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		super.initInternalFields();
		this.tableType = TableType.AUDIO_ALBUM;
		this.trackList = new TreeSet<>();
	}

	public TreeSet<AudioAlbumTrack> getTrackList() {
		return this.trackList;
	}

	public void setTrackList(TreeSet<AudioAlbumTrack> trackList) {
		this.trackList = trackList;
		this.duration = 0l;
		Long ms = new Long(0l);
		for (AudioAlbumTrack t : this.trackList) {
			ms += t.getLongDuration();
		}
		this.duration = ms;
	}

	public void addTrack(AudioAlbumTrack t) {
		this.trackList.add(t);
		Long ms = this.duration;
		ms += t.getLongDuration();
		this.duration = ms;
	}

	public Band getBand() {
		if (this.directors.isEmpty()) {
			return null;
		}
		return (Band) this.directors.first();
	}

	public void setBand(Band band) {
		this.directors.clear();
		this.directors.add(band);
	}
	
}
