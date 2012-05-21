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
import java.util.Date;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import model.BaseTable;
import model.enums.TableType;
import model.utilities.ForeignKey;
import settings.GlobalPaths;

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
	private Date duration;
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
		this.duration = new Date(0);
		this.tableType = TableType.MOVIE;
		this.rating = new Double(0.0);
		this.directors = new TreeSet<>();
		this.genres = new TreeSet<>();
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
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
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
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
		for (Object d : super.toRowData()) {
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

	/**
	 * Tworzy opis obiektu w formacie html, bazując na składowych polach klas
	 * 
	 * @return url do pliku html, zachowanego w ścieżce {@link GlobalPaths#TMP}
	 *         .Plik html zawiera prosty opis obiektu kolekcji
	 */
	public URL toDescription() {
		String str = new String();
		str += "<html>";
		str += "<p style='color: red'><b>Rating:</b>" + this.getRating()
				+ "</p>";
		str += "<p><b>ID:</b>" + this.getPrimaryKey() + "</p>";
		str += "<p><b>Title:</b>" + this.getTitle() + "</p>";
		if (this.getSubtitle() != null && !this.getSubtitle().isEmpty()) {
			str += "<b><i>Subtitle:</i></b>" + this.getSubtitle() + "</p>";
		}
		str += "<p><b>Lenght:</b>" + this.getDuration() + "</p>";
		if (this.getAuthors() != null && !this.getAuthors().isEmpty()) {
			str += "<b>Directors:</b>";
			str += "<ul>";
			for (Author author : this.getAuthors()) {
				str += "<li>" + author.getFirstName() + " "
						+ author.getLastName() + "</li>";
			}
			str += "</ul>";
		}
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
