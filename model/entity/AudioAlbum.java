/**
 * package model in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeSet;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.AudioAlbumTrack;
import model.utilities.ForeignKey;
import settings.GlobalPaths;

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
		String tmp[] = { "idAudio", "object", "coverFK", "artistFK" };
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

	@Override
	public URL toDescription() {
		String fullContent = new String();
		fullContent += "<html><body>";
		fullContent += this.generateBody();
		fullContent += "</body></html>";

		DataOutputStream dos = null;
		String path = GlobalPaths.TMP
				+ String.valueOf(Math.random() * Double.MAX_EXPONENT);
		try {
			dos = new DataOutputStream(new FileOutputStream(new File(path)));
			dos.writeBytes(fullContent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			URL ulr = new URL("file:///" + path);
			return ulr;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String generateBody() {
		String bodyPart = new String();
		bodyPart += "<h1>" + this.getBand().getName() + "</h1>";
		
		bodyPart += "<table border='0'>";
			bodyPart += "<tr>";
				bodyPart += "<td>";
				if(this.getCover() != null && this.getCover().getImageFile() != null){
					bodyPart += "<img src='" + this.getCover().getImageFile().getAbsolutePath() + "'/>";
				}else{
					bodyPart += "<img src='" + GlobalPaths.DEFAULT_COVER_PATH.toString() + "'/>";
				}
				bodyPart += "</td>";
				bodyPart += "<td>";		
					bodyPart += "<p><b>Rating:</b>" + this.getRating() + "</p>";
					bodyPart += "<p><b>ID:</b>" + this.getPrimaryKey() + "</p>";
					bodyPart += "<p><b>Title:</b>" + this.getTitle() + "</p>";
					if (this.getSubtitle() != null && !this.getSubtitle().isEmpty()) {
						bodyPart += "<b><i>Subtitle:</i></b>" + this.getSubtitle() + "</p>";
					}
					bodyPart += "<p><b>Lenght:</b>" + this.getDuration() + "</p>";
				bodyPart += "</td>";
			
				if (this.getTrackList() != null && !this.getTrackList().isEmpty()) {
					bodyPart += "<td>";
						bodyPart += "<b>Tracklist:</b>";
						bodyPart += "<ul>";
						for (AudioAlbumTrack t : this.getTrackList()) {
							bodyPart += "<li>" + t.toString() + "</li>";
						}
						bodyPart += "</ul>";
					bodyPart += "</td>";
				}
		
				if (this.getGenres() != null && !this.getGenres().isEmpty()) {
					bodyPart += "</tr>";
					bodyPart += "<tr>";
						bodyPart += "<td>";
							bodyPart += "<b>Genres:</b>";
							bodyPart += "<ul>";
							for (Genre g : this.getGenres()) {
								bodyPart += "<li>" + g.getGenre() + "</li>";
							}
							bodyPart += "</ul>";
						bodyPart += "</td>";
					bodyPart += "</tr>";
				}else{
					bodyPart += "</tr>";
				}
		bodyPart += "</table>";
		return bodyPart;
	}
}
