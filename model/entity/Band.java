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
 * @author kornicameister
 * 
 */
// TODO update comments
public class Band extends BaseTable {
	private String description = null;
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
	}

	/**
	 * Construct Band class with primary key and set of foreing keys
	 * 
	 * @param pk
	 * @param keys
	 */
	public Band(int pk, ForeignKey... keys) {
		super(pk, keys);
	}

	@Override
	protected void initInternalFields() {
		this.description = new String("empty");
		try {
			this.lastFMUrl = new URL(urlPattern
					+ this.getOriginalTitle().replaceAll(" ", "+"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.tagCloud = new ArrayList<Genre>();
		this.masterGenre = new Genre();
		this.tableName = TableType.BAND.toString();
		this.constraints.add(TableType.COVER);
		this.constraints.add(TableType.GENRE);
	}

	public String getName() {
		return this.getOriginalTitle();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	public ArrayList<Genre> getTagCloud() {
		return tagCloud;
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

}
