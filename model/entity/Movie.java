package model.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.ImageIcon;

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
	protected Picture cover;
	protected TreeSet<Author> directors;
	protected Genre genre;
	private Time duration;
	protected Double rating;

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
		this.rating = new Double(0.0);
		this.directors = new TreeSet<Author>();
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

	public TreeSet<Author> getAuthors() {
		return directors;
	}

	public void addAuthor(Author author) {
		if (!this.directors.contains(author)) {
			this.directors.add(author);
		}
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public void setRating(Double averageRating) {
		this.rating = new Double(averageRating);
	}
	
	public Double getRating(){
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
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for(Object d : super.toColumnIdentifiers()){
			data.add(d);
		}
		data.add("Title");
		data.add("Subtitle");
		data.add("Genre");
		data.add("Lenght");
		data.add("Author");
		data.add("Cover");
		return data.toArray();
	}

	@Override
	public Object[] toRowData() {
		ArrayList<Object> data = new ArrayList<Object>();
		for(Object d : super.toRowData()){
			data.add(d);
		}
		data.add(this.getTitle());
		data.add(this.getSubtitle());
		data.add(this.getGenre().getGenre());
		data.add(this.getDuration().toString());
		data.add(this.getAuthors());
		data.add(new ImageIcon(this.getCover().getImagePath()));
		return data.toArray();
	}

}
