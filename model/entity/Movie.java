package model.entity;

import java.io.Serializable;
import java.sql.Time;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * Table structure: </br> | idMovie </br> | titleOriginal </br> | titleLocale
 * </br> | duration </br> | description </br> | cover </br> | backCover </br> |
 * director </br> | genre </br>
 * 
 * @author kornicameister
 * @version 0.3
 */
public class Movie extends BaseTable implements Serializable {
	private static final long serialVersionUID = 2787293119303350654L;
	protected Picture cover = null;
	protected Author director = null;
	protected Genre genre = null;
	private Time duration = null;

	/**
	 * Construct Movie using default constructor
	 */
	public Movie() {
		super();
	}

	/**
	 * @param pk
	 * @param keys
	 */
	public Movie(int pk, ForeignKey... keys) {
		super(pk, keys);
	}

	/**
	 * @param pk
	 */
	public Movie(int pk) {
		super(pk);
	}

	/**
	 * @param originalTitle
	 */
	public Movie(String originalTitle) {
		super(originalTitle);
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idBook", "object", "coverFK", "directorFK", "genreFK" };
		return tmp;
	}

	@Override
	protected void initInternalFields() {
		this.setDuration(new Time(0));
		this.tableName = TableType.MOVIE.toString();
	}

	public void setTitle(String title) {
		this.titles[0] = title;
	}

	public String getTitle() {
		return this.titles[0];
	}

	public Time getDuration() {
		return duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return this.titles[2];
	}

	public void setDescription(String description) {
		this.titles[2] = description;
	}

	public void setCover(Picture fc) {
		this.cover = fc;
	}

	public Picture getCover() {
		return this.cover;
	}

	public Author getAuthor() {
		return director;
	}

	public void setAuthor(Author director) {
		this.director = director;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getOriginalTitle() + "]\n";
		str += "[DIRECTOR: " + this.getAuthor() + "]\n";
		str += "[GENRE: " + this.getGenre() + "]\n";
		str += "[DURATION: " + this.getDuration() + "]\n";
		str += "[COVER :" + this.cover.toString() + "]\n";
		return str;
	}

}
