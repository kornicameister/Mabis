/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import model.enums.CoverType;
import model.enums.TableType;

/**
 * @author kornicameister
 * 
 */
// TODO update commnents and make them more sql dependable
public class Cover extends BaseTable {
	private CoverType type = null;
	private String imagePath = null;
	private String checkSum = null;
	private final static String defaultCover = "src/resources/defaultCover.png";

	/**
	 * Constructs Cover with primary key
	 * 
	 * @param pk
	 */
	public Cover(int pk) {
		super(pk);
	}

	/**
	 * @param originalTitle
	 * @throws FileNotFoundException
	 */
	public Cover(String url) throws FileNotFoundException {
		super();
		this.imagePath = url;
		this.generateCheckSum();
	}

	/**
	 * @return the type
	 */
	public CoverType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(CoverType type) {
		this.type = type;
	}

	/**
	 * @return the checkSum
	 */
	public String getCheckSum() {
		return checkSum;
	}

	/**
	 * Method opens an image, and generates hash checksum basing of image file
	 * binary content <b>Notice</> that this.checkSum will always be 36b lenght
	 * 
	 * @param checkSum
	 *            the checkSum to set
	 * @throws FileNotFoundException
	 */
	public void generateCheckSum() throws FileNotFoundException {
		String m;
		if (this.imagePath.isEmpty()) {
			m = this.getClass().getResource(defaultCover).toString();
		} else {
			m = this.imagePath;
		}
		this.checkSum = database.Utilities.md5sum(new FileInputStream(m));
	}
	@Override
	protected void initInternalFields() {
		this.tableName = TableType.COVER.toString();
		this.type = CoverType.UNDEFINED;
		this.imagePath = new String("");
		this.checkSum = new String("");
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) throws FileNotFoundException {
		if (this.imagePath.equals(imagePath)) {
			return;
		}
		this.imagePath = imagePath;
		this.generateCheckSum();
	}
}
