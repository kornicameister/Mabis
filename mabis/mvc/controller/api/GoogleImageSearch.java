package mvc.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import logger.MabisLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstrakcyjna klasa. Poprzez dostep do jej statycznych metod uzytkownik moze
 * wyszukac adresy zdjec dla podanego kryterium podobnie jak robi sie to przez
 * strone Google Image Search
 * 
 * @author tomasz
 * 
 */
public abstract class GoogleImageSearch {
	private final static String MAGIC_URL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	private final static String MAGIC_OPTIONS = "&imgtype=face&safe=off&imgsz=large";

	/**
	 * Zwraca URL do zdjecia.</br>Metoda laczy sie z baza danych Google poprzez
	 * adres, ktory budowany jest z nastepujacych elementow:
	 * <ul>
	 * <li> {@link GoogleImageSearch#MAGIC_URL}</li>
	 * <li> {@link GoogleImageSearch#MAGIC_OPTIONS}</li>
	 * <li>parametr wywolania funkcji <b>query</b></li>
	 * </ul>
	 * 
	 * @param query
	 * @return url do zdjecia
	 */
	public static URL queryForImage(String query) {
		query = query.replaceAll(" ", "+");
		URL url = null;
		URLConnection connection = null;
		try {
			url = new URL(MAGIC_URL + query + MAGIC_OPTIONS);
			connection = url.openConnection();
			connection.addRequestProperty("Referer", "www.example.com");
			connection
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0");
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
	 * Parsuje odpowiedz zwrocona z serwerow Google w formacie JSON. Po
	 * zlokalizowaniu adresu URL z tablicy JSON, metoda sprawdza dostepnosc
	 * zdjecia podejmujac probe otwarcia strumienia InputStream na URL, jeśli
	 * sie to nie uda, przechodzi do kolejnego adresu URL i podejmuje probe
	 * ponownie.
	 * 
	 * @param builder
	 *            obiekt klasy StringBuilder zawierajacy odpowiedz JSON
	 * @return reprezentacje string URL do zdjecia
	 * @throws JSONException
	 */
	private static String parseJson(StringBuilder builder) throws JSONException {
		JSONObject json = new JSONObject(builder.toString());
		JSONObject responseData = json.getJSONObject("responseData");
		JSONArray results = responseData.getJSONArray("results");
		if (results.length() == 0) {
			return null;
		}
		String url = null;
		for (int i = 0; i < results.length(); i++) {
			try {
				url = results.getJSONObject(i).getString("url");
				try {
					InputStream is = new URL(url).openStream();
					is.close();
					return url;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				MabisLogger.getLogger().log(Level.WARNING,
						"Image at {0} seems to be unavailable", url);
			}
		}
		return url;
	}
}
