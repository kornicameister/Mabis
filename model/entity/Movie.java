package model.entity;

import java.sql.Time;
import java.util.TreeMap;

import model.enums.CoverType;
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
public class Movie extends BaseTable {
	private TreeMap<CoverType, Cover> covers = null; // map of covers
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
	protected void initInternalFields() {
		this.setDuration(new Time(0));

		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.GENRE);
		this.constraints.add(TableType.AUTHOR);
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

}
