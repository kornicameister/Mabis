package model.utilities;

import java.io.Serializable;
import java.net.URL;

public class AudioAlbumTrack implements Serializable,
		Comparable<AudioAlbumTrack> {
	private static final long serialVersionUID = -7691994149397894841L;
	private Long duration;
	private Short id;
	private String name;
	private URL lastFMUrl;

	public AudioAlbumTrack(Short id, String name, String dur) {
		this.id = id;
		this.name = name;
		if(dur.isEmpty()){
			this.duration = 0l;
		}else{
			this.duration = Long.valueOf(dur);
		}
		this.lastFMUrl = null;
	}

	public String getDuration() {
		int minutes = (int) (this.duration / 60);
		int leftSeconds = (int) (this.duration - (minutes * 60));
		return minutes + ":" + leftSeconds;
	}

	public Long getLongDuration() {
		return this.duration;
	}

	public void setDuration(String dur) {
		this.duration = Long.valueOf(dur);
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URL getLastFMUrl() {
		return lastFMUrl;
	}

	public void setLastFMUrl(URL lastFMUrl) {
		this.lastFMUrl = lastFMUrl;
	}

	@Override
	public int compareTo(AudioAlbumTrack o) {
		int result = this.id.compareTo(o.id);
		if (result == 0) {
			result = this.name.compareTo(o.name);
		}
		return result;
	}

	@Override
	public String toString() {
		return this.id.toString() + ". " + this.name + "\t("
				+ this.getDuration() + ")";
	}
}