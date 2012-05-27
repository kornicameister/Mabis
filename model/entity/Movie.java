package model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * Table structure: </br> | idMovie </br> | titleOriginal </br> | titleLocale
 * </br> | duration </br> | description </br> | cover </br> | backCover </br> |
 * director </br> | genres </br>
 * 
 * @author kornicameister
 * @version 0.3
 */
public class Movie extends BaseTable implements Serializable {
	private static final long serialVersionUID = 2787293119303350654L;
	protected Picture cover;
	protected TreeSet<Author> directors;
	protected TreeSet<Genre> genres;
	protected Long duration;
	protected Double rating;
	protected String yearOfRelease;

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
		this.duration = new Long(0l);
		this.tableType = TableType.MOVIE;
		this.rating = new Double(0.0);
		this.directors = new TreeSet<>();
		this.genres = new TreeSet<>();
		this.yearOfRelease = new String();
	}
	
	public String getYearOfRelease() {
		return yearOfRelease;
	}

	public void setYearOfRelease(String yearOfRelease) {
		this.yearOfRelease = yearOfRelease;
	}
	
	public Long getLongDuration() {
		return duration;
	}

	public String getDuration() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return sdf.format(new Date(this.duration));
	}

	public void setDuration(Long duration) {
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

	public TreeSet<Author> getAuthors() {
		return directors;
	}

	public void addAuthor(Author author) {
		this.directors.add(author);
	}

	public Genre getGenre() {
		return genres.first();
	}

	public void addGenre(Genre genre) {
		this.genres.add(genre);
	}

	public TreeSet<Genre> getGenres() {
		return this.genres;
	}

	public void setGenres(Collection<Genre> g) {
		this.genres.addAll(g);
	}

	public void setRating(Double averageRating) {
		this.rating = new Double(averageRating);
	}

	public Double getRating() {
		return this.rating;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[TITLE: " + this.getTitle() + "]\n";
		str += "[DIRECTORS]\n";
		for (Author a : this.directors) {
			str += "\t" + a.toString() + "\n";
		}
		str += "[GENRE: " + this.getGenre() + "]\n";
		str += "[RATING: " + this.getRating() + "]\n";
		str += "[DURATION: " + this.getDuration() + "]\n";
		str += "[COVER :" + this.cover.toString() + "]\n";
		return str;
	}

	@Override
	public int compareTo(BaseTable o) {
		int result = super.compareTo(o);
		Movie tmp = (Movie) o;
		if (result == 0) {
			result = (this.directors.equals(tmp.directors) ? 0 : -1);
		}
		if (result == 0) {
			result = (this.genres.equals(tmp.genres) ? 0 : -1);
		}
		return result;
	}

}
