/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import utilities.Hasher;

import model.enums.CoverType;
import model.enums.TableType;

/**
 * This class maps itself to mabis.cover table Table structure: </br> | idCover
 * </br> | image </br> | hash
 * 
 * @author kornicameister
 * 
 */
// TODO update commnents and make them more sql dependable
public class Picture extends BaseTable {
	private CoverType type = null;
	private File imageFile;

	public Picture() {
		super();
	}

	public Picture(int pk) {
		super(pk);
	}

	/**
	 * This constructor can produce FileNotFoundException as it creates Picture
	 * with file picture set, and this requires having checksum of this file
	 * calculated !!! <b>Notice that invalid file (i.e. file that does not
	 * exist) will produce this exception
	 * 
	 * @param cover
	 * @throws FileNotFoundException
	 */
	public Picture(File cover) throws FileNotFoundException {
		super();
		this.imageFile = cover;
		this.generateCheckSum();
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) throws FileNotFoundException {
		this.imageFile = imageFile;
		this.generateCheckSum();
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idCover", "image", "hash" };
		return tmp;
	}

	public CoverType getType() {
		return type;
	}

	public void setType(CoverType type) {
		this.type = type;
	}

	public String getCheckSum() {
		return this.titles[0];
	}

	/**
	 * Method opens an image, and generates hash checksum basing of image file
	 * binary content <b>Notice</> that this.checkSum will always be 36b lenght
	 * 
	 * @param checkSum
	 *            the checkSum to set
	 * @throws FileNotFoundException
	 */
	private void generateCheckSum() throws FileNotFoundException {
		this.titles[0] = Hasher.hashStream(new FileInputStream(
				this.imageFile));
	}

	@Override
	protected void initInternalFields() {
		this.type = CoverType.UNDEFINED;
		this.imageFile = null;
		this.tableName = TableType.PICTURE.toString();
		this.titles[0] = "";
	}

	@Override
	public String toString() {
		String str = super.toString();
		str += "----------\n";
		str += "[TYPE: " + this.getType() + "]\n";
		str += "[CHCSM: " + this.getCheckSum() + "]\n";
		return str;
	}
}
