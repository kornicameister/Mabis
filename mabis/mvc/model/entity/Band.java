/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import mvc.model.enums.AuthorType;
import mvc.model.enums.TableType;

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
public class Band extends Author implements Serializable {
	private static final long serialVersionUID = -7352842615967671155L;
	private URL lastFMUrl = null;
	private ArrayList<Genre> tagCloud = null;
	private Genre masterGenre = null;
	private static final String urlPattern = "http://www.lastfm.pl/music/band_name/";

	public Band() {
		super();
	}

	/**
	 * Construct Band class with bandName
	 * 
	 * @param bandName
	 *            band of the name in normal human-readable format
	 */
	public Band(String bandName) {
		super();
		this.setTitle(bandName);
		this.createLastFMUrl();
	}

	public Band(String bandName, AuthorType type) {
		super();
		this.setTitle(bandName);
		this.createLastFMUrl();
		this.type = type;
	}

	private void createLastFMUrl() {
		try {
			this.lastFMUrl = new URL(urlPattern
					+ this.getTitle().replaceAll(" ", "+"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initInternalFields() {
		this.tagCloud = new ArrayList<Genre>();
		this.masterGenre = new Genre();
		this.tableType = TableType.BAND;
	}

	public String getName() {
		return this.getTitle();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.getSubtitle();
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.setSubTitle(description);
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
		return this.tagCloud;
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
		return this.getPrimaryKey() + ": " + this.getName();
	}

}
