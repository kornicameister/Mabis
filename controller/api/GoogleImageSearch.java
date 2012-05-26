package controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleImageSearch {
	private final static String MAGIC_URL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	private final static String MAGIC_OPTIONS = "&imgtype=face&safe=off&imgsz=large";

	public static URL queryForImage(String query) {
		query = query.replaceAll(" ", "+");
		URL url = null;
		URLConnection connection = null;
		try {
			url = new URL(MAGIC_URL + query + MAGIC_OPTIONS);
			connection = url.openConnection();
			connection.addRequestProperty("Referer", "www.example.com");
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return new URL(parseJson(builder));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param builder
	 * @return
	 * @throws JSONException
	 */
	private static String parseJson(StringBuilder builder) throws JSONException {
		JSONObject json = new JSONObject(builder.toString());
		JSONObject responseData = json.getJSONObject("responseData");
		JSONArray results = responseData.getJSONArray("results");
		if (results.length() == 0) {
			return null;
		}
		responseData = results.getJSONObject(0);
		String url = responseData.getString("url");
		return url;
	}
}
