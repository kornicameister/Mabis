package model.entity;

import java.sql.Time;

import model.enums.TableType;
import model.utilities.ForeignKey;
import exceptions.SQLForeingKeyNotFound;

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
		this.reloadMetaData();
	}

	@Override
	protected void reloadMetaData() {
		this.metaData.clear();
		this.metaData.put("idMovie", this.getPrimaryKey().toString());
		this.metaData.put("titleOriginal", this.getOriginalTitle());
		this.metaData.put("titleLocale", this.getLocalizedTitle());
		this.metaData.put("duration", this.getDuration().toString());
		this.metaData.put("description", this.getDescription());
		try {
			this.metaData.put("frontCover", this.getForeingKey("frontCover")
					.getValue().toString());
			this.metaData.put("backCover", this.getForeingKey("backCover")
					.getValue().toString());
			this.metaData.put("director", this.getForeingKey("director")
					.getValue().toString());
			this.metaData.put("genre", this.getForeingKey("genre").getValue()
					.toString());
		} catch (SQLForeingKeyNotFound e) {
			e.printStackTrace();
		} finally {
			this.metaData.clear();
		}
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
