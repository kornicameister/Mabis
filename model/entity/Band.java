/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import model.enums.TableType;
import model.utilities.ForeignKey;

/**
 * This class maps itself to Mabis.band table
 * 
 * Table structure: </br> | idBand </br> | name </br> | description </br> | url
 * </br> | picture </br> | masterGenre </br> | tagCloud </br>
 * 
 * @author kornicameister
 * 
 */
// TODO update comments
public class Band extends Author {
	private URL lastFMUrl = null;
	private ArrayList<Genre> tagCloud = null;
	private Genre masterGenre = null;
	private static final String urlPattern = "http://www.lastfm.pl/music/band_name";

	/**
	 * Construct Band class with bandName
	 * 
	 * @param bandName
	 *            band of the name in normal human-readable format
	 */
	public Band(String bandName) {
		super();
		this.setOriginalTitle(bandName);
		this.createLastFMUrl();
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idBand", "name", "description", "url",
				"picture", "masterGenre", "tagCloud"};
		return tmp;
	}
	
	private void createLastFMUrl() {
		try {
			this.lastFMUrl = new URL(urlPattern
					+ this.getOriginalTitle().replaceAll(" ", "+"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Construct Band class with primary key and set of foreing keys
	 * 
	 * @param pk
	 * @param keys
	 */
	public Band(int pk, ForeignKey... keys) {
		super(pk);
		for (ForeignKey k : keys) {
			this.addForeingKey(k);
		}
	}

	@Override
	protected void initInternalFields() {
		this.tagCloud = new ArrayList<Genre>();
		this.masterGenre = new Genre();
		this.constraints.add(TableType.PICTURE);
		this.constraints.add(TableType.GENRE);
		this.tableName = TableType.BAND.toString();
	}

	public String getName() {
		return this.getOriginalTitle();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.getLocalizedTitle();
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.setLocalizedTitle(description);
	}

	/**
	 * @return the lastFMUrl
	 */
	public URL getLastFMUrl() {
		return lastFMUrl;
	}

	/**
	 * @param lastFMUrl
	 *            the lastFMUrl to set
	 */
	public void setLastFMUrl(URL lastFMUrl) {
		this.lastFMUrl = lastFMUrl;
	}

	/**
	 * @return the tagCloud
	 */
	public String getTagCloud() {
		String t = new String();
		for (short i = 0; i < this.tagCloud.size(); i++) {
			t += this.tagCloud.get(i).getGenre() + ",";
		}
		return t.substring(0, t.length() - 1);
	}

	/**
	 * @param tagCloud
	 *            the tagCloud to set
	 */
	public void setTagCloud(ArrayList<Genre> tagCloud) {
		this.tagCloud = tagCloud;
	}

	/**
	 * @return the masterGenre
	 */
	public Genre getMasterGenre() {
		return masterGenre;
	}

	/**
	 * @param masterGenre
	 *            the masterGenre to set
	 */
	public void setMasterGenre(Genre masterGenre) {
		this.masterGenre = masterGenre;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[NAME: " + this.getName() + "]\n";
		str += "[LASTFM: " + this.getLastFMUrl().toString() + "]\n";
		str += "[GENRE: " + this.getMasterGenre() + "]\n";
		str += "[TAGCLOUD: " + this.getTagCloud() + "]\n";
		return str;
	}

}
