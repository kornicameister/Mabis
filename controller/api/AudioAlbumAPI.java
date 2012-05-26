/**
 * 
 */
package controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import logger.MabisLogger;
import model.entity.AudioAlbum;
import model.entity.Band;
import model.entity.Genre;
import model.entity.Picture;
import model.enums.GenreType;
import model.enums.ImageType;
import model.utilities.AudioAlbumTrack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kornicameister
 * 
 */
public class AudioAlbumAPI extends ApiAccess {
	private final static String API_KEY = "&api_key=9dcb20fc9c4a1bcfa67e6e85f7c59f1e";
	private final static String ALBUM_SEARCH = "http://ws.audioscrobbler.com/2.0/?method=album.search&album=";
	private final static String ARTIST_SEARCH = "http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=";
	private final static String MBID_ALBUM_INFO = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&autocorrect=1&lang=pol&mbid=";
	private final static String MBID_TOP_TAGS = "http://ws.audioscrobbler.com/2.0/?method=album.gettoptags&autocorrect=1&mbid=";
	private final static String JSON_ENABLED = "&format=json";

	@Override
	public void query(TreeMap<String, String> params) throws IOException {
		try {
			parseResponse(accessAPI(params));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param params
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private StringBuilder accessAPI(TreeMap<String, String> params)
			throws IOException {
		String line = ((String) params.values().toArray()[0]).replaceAll(" ",
				"+").toLowerCase();
		String type = ((String) params.keySet().toArray()[0]).replaceAll(" ",
				"+").toLowerCase();

		URL downloadURL = null;
		if (type.contains("album")) {
			downloadURL = new URL(ALBUM_SEARCH + line + API_KEY + JSON_ENABLED);
		} else if (type.contains("artist")) {
			downloadURL = new URL(ARTIST_SEARCH + line + API_KEY + JSON_ENABLED);
		} else if (type.contains("tracks")) {
			downloadURL = new URL(MBID_ALBUM_INFO + line + API_KEY + JSON_ENABLED);
		} else if (type.contains("tags")) {
			downloadURL = new URL(MBID_TOP_TAGS + line + API_KEY + JSON_ENABLED);
		}

		URLConnection connection = downloadURL.openConnection();
		connection.addRequestProperty("Referer", "www.example.com");
		StringBuilder builder = new StringBuilder();
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder;
	}

	/**
	 * @param builder
	 * @throws JSONException
	 */
	private void parseResponse(StringBuilder builder) throws JSONException {
		JSONObject startObject = new JSONObject(builder.toString());
		JSONArray albumMatches = startObject.getJSONObject("results")
				.getJSONObject("albummatches").getJSONArray("album");

		// fire task lenght
		this.pcs.firePropertyChange("taskStarted", 0, albumMatches.length());

		AudioAlbum aa = null;
		JSONObject albumObject;
		for (int i = 0; i < albumMatches.length(); i++) {
			albumObject = albumMatches.getJSONObject(i);
			try {
				aa = new AudioAlbum(albumObject.getString("name"),
						this.parseTrackList(albumObject.getString("mbid")));
				aa.setGenres(this.parseTags(albumObject.getString("mbid")));
				aa.setBand(this.parseBand(albumObject.getString("artist")));
				aa.setCover(this.parseImage(albumObject.getJSONArray("image")));
				this.result.add(aa);
				this.pcs.firePropertyChange("taskStep",i,i+1);
			} catch (JSONException pp) {
				Object params[] = { aa.getTitle(), pp.getMessage() };
				MabisLogger.getLogger().log(Level.WARNING,
						"During processing of {0}, exception was caught - {1}",
						params);
			}
		}
	}

	private ArrayList<Genre> parseTags(String mbid) throws JSONException {
		TreeMap<String, String> params = new TreeMap<>();
		params.put("tags", mbid);
		ArrayList<Genre> tags = new ArrayList<>();
		try {
			JSONObject startObject = new JSONObject(accessAPI(params)
					.toString());
			if (startObject.has("error") || !startObject.has("toptags")) {
				return tags;
			}
			if (!startObject.getJSONObject("toptags").has("tag")) {
				return tags;
			}
			JSONArray tracks = startObject.getJSONObject("toptags")
					.getJSONArray("tag");
			for (int i = 0; i < 5; i++) {
				tags.add(new Genre(tracks.getJSONObject(i).getString("name"),GenreType.AUDIO));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tags;
	}

	private TreeSet<AudioAlbumTrack> parseTrackList(String mbid)
			throws JSONException {
		TreeMap<String, String> params = new TreeMap<>();
		params.put("tracks", mbid);
		TreeSet<AudioAlbumTrack> trackList = new TreeSet<>();
		try {
			JSONObject startObject = new JSONObject(accessAPI(params)
					.toString());
			if (startObject.has("error") || !startObject.has("album")) {
				return trackList;
			}
			if (!startObject.getJSONObject("album").has("tracks")
					|| startObject.getJSONObject("album")
							.getJSONObject("tracks").has("album")) {
				return trackList;
			}
			JSONArray tracks = startObject.getJSONObject("album")
					.getJSONObject("tracks").getJSONArray("track");
			JSONObject track = null;
			for (int i = 0; i < tracks.length(); i++) {
				track = tracks.getJSONObject(i);
				trackList.add(new AudioAlbumTrack((short) track.getJSONObject(
						"@attr").getInt("rank"), track.getString("name"), track
						.getString("duration")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trackList;
	}

	private Band parseBand(String band) throws JSONException {
		Band b = new Band(band);
		TreeMap<String, String> params = new TreeMap<>();
		params.put("artist", band);
		try {
			JSONObject startObject = new JSONObject(accessAPI(params)
					.toString());
			JSONArray artistMatches = startObject.getJSONObject("results")
					.getJSONObject("artistmatches").getJSONArray("artist");
			JSONObject artistObject = null;
			for (int i = 0; i < artistMatches.length(); i++) {
				artistObject = artistMatches.getJSONObject(i);
				if (artistObject.getString("name").equals(band)) {
					b.setPicture(this.parseImage(artistObject
							.getJSONArray("image")));
					return b;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	private Picture parseImage(JSONArray images) throws JSONException {
		for (int i = images.length() - 1; i > 0; i--) {
			JSONObject picture = images.getJSONObject(i);
			if (picture.getString("#text") != null) {
				try {
					return new Picture(new URL(picture.getString("#text")),
							ImageType.FRONT_COVER);
				} catch (MalformedURLException e) {
					return null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
