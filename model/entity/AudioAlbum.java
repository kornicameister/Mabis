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
import java.util.ArrayList;

import model.BaseTable;
import model.enums.TableType;
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
		this.tableType = TableType.AUDIO_ALBUM;
	}

	public String getTrackList() {
		return this.getSubtitle();
	}

	public void setTrackList(String trackList) {
		this.setSubTitle(trackList);
	}

	public Band getBand() {
		if(this.directors.isEmpty()){
			return null;
		}
		return (Band) this.directors.first();
	}

	public void setBand(Band band) {
		this.directors.clear();
		this.directors.add(band);
	}

	@Override
	public String toString() {
		String str = BaseTable.class.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getTitle() + "]\n";
		str += "[BAND: " + this.getBand() + "]\n";
		str += "[TAGCLOUD: " + this.getGenres() + "]\n";
		str += "[DURATION: " + this.getDuration() + "]\n";
		str += "[COVER:" + this.getCover() + "]\n";
		return str;
	}

	@Override
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
			data.add(d);
		}
		data.set(6, "Band");
		return data.toArray();
	}

	@Override
	public URL toDescription() {
		String str = new String();
		str += "<html>";
		str += "<p style='color: red'><b>Rating:</b>" + this.getRating()
				+ "</p>";
		str += "<p><b>Band:</b>" + this.getBand().getName() + "</p>";
		str += "<p><b>ID:</b>" + this.getPrimaryKey() + "</p>";
		str += "<p><b>Title:</b>" + this.getTitle() + "</p>";
		if (this.getSubtitle() != null && !this.getSubtitle().isEmpty()) {
			str += "<b><i>Subtitle:</i></b>" + this.getSubtitle() + "</p>";
		}
		str += "<p><b>Lenght:</b>" + this.getDuration() + "</p>";
		str += "<p><b>Tracklist:</b>" + this.getTrackList() + "</p>";
		if (this.getGenres() != null && !this.getGenres().isEmpty()) {
			str += "<b>Genres:</b>";
			str += "<ul>";
			for (Genre g : this.getGenres()) {
				str += "<li>" + g.getGenre() + "</li>";
			}
			str += "</ul>";
		}
		str += "<p><b>Description:</b></p>";
		str += "<span style='margin-left:20px'>" + this.getDescription()
				+ "</span>";
		str += "</html>";

		DataOutputStream dos = null;
		String path = GlobalPaths.TMP
				+ String.valueOf(Math.random() * Double.MAX_EXPONENT);
		try {
			dos = new DataOutputStream(new FileOutputStream(new File(path)));
			dos.writeBytes(str);
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

}
