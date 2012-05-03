package model.entity;

import java.io.Serializable;
import java.sql.Time;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * Table structure: </br> | idMovie </br> | titleOriginal </br> | titleLocale
 * </br> | duration </br> | description </br> | frontCover </br> | backCover
 * </br> | director </br> | genre </br>
 * 
 * @author kornicameister
 * @version 0.2
 */
public class Movie extends BaseTable implements Serializable {
	private static final long serialVersionUID = 2787293119303350654L;
	private Picture frontCover = null; // map of covers
	private Author director = null; // director is a foreing key to Author table
	private Genre genre = null;
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

	public void setFrontCover(Picture fc) {
		this.frontCover = fc;
	}

	public Picture getFrontCover() {
		return this.frontCover;
	}

	public Author getDirector() {
		return director;
	}

	public void setDirector(Author director) {
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
		str += "[DIRECTOR: " + this.getDirector() + "]\n";
		str += "[GENRE: " + this.getGenre() + "]\n";
		str += "[DURATION: " + this.getDuration() + "]\n";
		str += "[COVER :" + this.frontCover.toString() + "]\n";
		return str;
	}

}
