package model.entity;

import java.sql.Time;

import model.enums.TableType;
import model.utilities.ForeignKey;

public class Movie extends BaseTable {

	private Time duration = null;
	private String description = null;

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
		this.setDescription(new String());
		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.GENRE);
		this.constraints.add(TableType.AUTHOR);
	}

	/**
	 * 
	 * @return the duration
	 */
	public Time getDuration() {
		return duration;
	}

	/**
	 * 
	 * @param duration
	 *            of the movie
	 */
	public void setDuration(Time duration) {
		this.duration = duration;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            of the movie
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
