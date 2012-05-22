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
import java.util.TreeMap;

import model.entity.AudioAlbum;
import model.entity.Band;
import model.entity.Picture;
import model.enums.ImageType;

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
	private final static String OPTIONS = "&format=json";

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
			downloadURL = new URL(ALBUM_SEARCH + line + API_KEY + OPTIONS);
		} else if (type.contains("artist")) {
			downloadURL = new URL(ARTIST_SEARCH + line + API_KEY + OPTIONS);
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

		AudioAlbum aa;
		JSONObject albumObject;
		for (int i = 0; i < albumMatches.length(); i++) {
			aa = new AudioAlbum();
			albumObject = albumMatches.getJSONObject(i);
			aa.setTitle(albumObject.getString("name"));
			aa.setBand(this.parseBand(albumObject.getString("artist")));
			aa.setCover(this.parseImage(albumObject.getJSONArray("image")));
			this.result.add(aa);
		}
	}

	private Band parseBand(String band) throws JSONException {
		Band b = new Band(band);
		TreeMap<String, String> params = new TreeMap<>();
		params.put("artist", band);
		try {
			JSONObject startObject = new JSONObject(accessAPI(params).toString());
			JSONArray artistMatches = startObject.getJSONObject("results")
					.getJSONObject("artistmatches").getJSONArray("artist");
			JSONObject artistObject = null;
			for (int i = 0; i < artistMatches.length(); i++) {
				artistObject = artistMatches.getJSONObject(i);
				if(artistObject.getString("name").equals(band)){
					b.setPicture(this.parseImage(artistObject.getJSONArray("image")));
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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
