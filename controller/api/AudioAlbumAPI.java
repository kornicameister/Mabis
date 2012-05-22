/**
 * 
 */
package controller.api;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author kornicameister
 *
 */
public class AudioAlbumAPI extends ApiAccess {
	private final static String API_KEY = "&api_key=9dcb20fc9c4a1bcfa67e6e85f7c59f1e";
	private final static String QUERY_URL = "http://ws.audioscrobbler.com/2.0/?";

	@Override
	public void query(TreeMap<String, String> params)
			throws IOException {
	}

}
