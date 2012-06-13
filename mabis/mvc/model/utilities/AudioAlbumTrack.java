package mvc.model.utilities;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import mvc.model.entity.AudioAlbum;

/**
 * Klasa opisuje sciezke z albumu muzycznego
 * 
 * @author tomasz
 * @see AudioAlbum
 */
public class AudioAlbumTrack
		implements
			Serializable,
			Comparable<AudioAlbumTrack> {
	private static final long serialVersionUID = -7691994149397894841L;
	private Long duration;
	private Integer id;
	private String name;

	/**
	 * Tworzy nowy obiekt {@link AudioAlbumTrack}
	 * 
	 * @param id
	 *            numer porzadkowy sciezki
	 * @param name
	 *            tytul sciezki
	 * @param dur
	 *            dlugosc trwania sciezki powinna byc podana w milisekundach
	 */
	public AudioAlbumTrack(Integer id, String name, String dur) {
		this.id = id;
		this.name = name;
		this.duration = 0l;
		if (!dur.isEmpty()) {
			this.duration = Long.valueOf(dur);
		}
	}

	/**
	 * Tworzy nowy obiekt {@link AudioAlbumTrack}
	 * 
	 * @param id
	 *            numer porzadkowy sciezki
	 * @param name
	 *            tytul sciezki
	 * @param durr
	 *            dlugosc trwania sciezki powinna byc podana w milisekundach
	 */
	public AudioAlbumTrack(int id, String name, Long durr) {
		this.id = id;
		this.name = name;
		this.duration = durr;
	}

	/**
	 * Zwraca dlugosc trwania sciezki. Wazne jest to ze wewnetrznie korzysta z
	 * funkcji {@link String#format(String, Object...)}, aby poprawnie zwrocic
	 * dlugosc sciezki bazujac na ilosci milisekund zapisanych w klasie
	 * 
	 * @return dlugosc sciezki jako string, w formacie %d:%d
	 */
	public String getDuration() {
		String res = String.format(
				"%d:%d",
				TimeUnit.SECONDS.toHours(this.duration),
				TimeUnit.SECONDS.toMinutes(this.duration)
						- (TimeUnit.SECONDS.toHours(this.duration) * 60));
		return res;
	}

	/**
	 * Zwraca dlugosc sciezki jako long w milisekundach
	 * 
	 * @return dlugosc sciezki
	 */
	public Long getLongDuration() {
		return this.duration;
	}

	/**
	 * Ustawia dlugosc sciezki jako string. Dlugosc powinna byc podana w
	 * milisekundach
	 * 
	 * @param dur
	 */
	public void setDuration(String dur) {
		this.duration = Long.valueOf(dur);
	}

	/**
	 * Ustawia dlugosc trwania sciezki, ale podanej w milisekundach jako long
	 * 
	 * @param l
	 */
	public void setDuration(Long l) {
		this.duration = l;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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