package mvc.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TreeMap;

import mvc.model.entity.Author;
import mvc.model.entity.Genre;
import mvc.model.entity.Movie;
import mvc.model.entity.Picture;
import mvc.model.enums.GenreType;
import mvc.model.enums.PictureType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Klasa pozwala na dostep do bazy filmow <a href="themoviedb.org">TMDb</a> oraz
 * <a href="http://imdbapi.com/">IMDB</a> poprzez publiczne API
 * 
 * @author kornicameister
 * 
 */
public class MovieAPI extends ApiAccess {
	private static final String IMDB_SEARCH = "http://www.imdbapi.com/?t=";
	private MovieApiTarget target;

	/**
	 * Publiczny enum, wskazuje na API do którego program się łączy celem
	 * pobrania danych o filmie.
	 * 
	 * @author kornicameister
	 * 
	 */
	public enum MovieApiTarget {
		IMDB, THE_MOVIE_DB;
	}

	public MovieAPI(MovieApiTarget t) {
		super();
		this.target = t;
	}

	@Override
	public void query(TreeMap<String, String> params) throws IOException {
		String title = (String) params.values().toArray()[0];
		title = title.replaceAll(" ", "+");
		try {
			switch (this.target) {
				case IMDB :
					parseIMDBResponse(this.accessAPI(title));
					break;
				case THE_MOVIE_DB :
					break;
				default :
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected StringBuilder accessAPI(String title) throws IOException {
		URL url = new URL(IMDB_SEARCH + title);
		URLConnection connection = url.openConnection();
		connection.addRequestProperty("Referer", "www.example.com");
		StringBuilder builder = new StringBuilder();
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		while ((title = reader.readLine()) != null) {
			builder.append(title);
		}
		return builder;
	}

	private void parseIMDBResponse(StringBuilder builder) throws JSONException {
		this.pcs.firePropertyChange("taskStarted", 0, 1);
		JSONObject startObject = new JSONObject(builder.toString());

		Movie m = new Movie(startObject.getString("Title"));
		m.setYearOfRelease(startObject.getString("Year"));
		m.setDescription(startObject.getString("Plot"));

		String arr[] = startObject.getString("Genre").split(", ");
		for (String g : arr) {
			m.addGenre(new Genre(g, GenreType.MOVIE));
		}

		arr = startObject.getString("Director").split(", ");
		for (String a : arr) {
			Author tmp = new Author(a.split(" ")[0], a.split(" ")[1]);
			try {
				tmp.setPicture(new Picture(GoogleImageSearch.queryForImage(a),
						PictureType.AUTHOR));
				m.addAuthor(tmp);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (tmp.getAvatar() == null) {
					tmp.setPicture(null);
				}
			}
		}

		try {
			m.setCover(new Picture(new URL(startObject.getString("Poster")),
					PictureType.FRONT_COVER));
		} catch (IOException e) {
			e.printStackTrace();
		}

		arr = startObject.getString("Runtime").split(" ");
		Long res = 0l;
		for (int i = arr.length - 1; i > 0; i -= 2) {
			if (arr[i].equals("sec")) {
				res += Long.valueOf(arr[i - 1]);
			} else if (arr[i].equals("min")) {
				res += Long.valueOf(arr[i - 1]) * 60;
			} else if (arr[i].equals("h")) {
				res += Long.valueOf(arr[i - 1]) * 3600;
			}
		}
		m.setDuration(res * 60);
		this.result.add(m);
		this.pcs.firePropertyChange("taskStep", 0, 1);
	}

	public MovieApiTarget getTarget() {
		return target;
	}

	public void setTarget(MovieApiTarget target) {
		this.target = target;
	}

	@Override
	protected StringBuilder accessAPI(TreeMap<String, String> params)
			throws IOException {
		return null;
	}
}
