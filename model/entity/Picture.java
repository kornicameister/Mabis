/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import model.BaseTable;
import model.enums.ImageType;
import model.enums.TableType;
import utilities.Hasher;

/**
 * Klasa Picture jest obiektową wersją tabeli bazy danych o następującej
 * strukturze </br> | idCover </br> | image </br> | hash
 * 
 * @author kornicameister
 * 
 */
// TODO update commnents and make them more sql dependable
public class Picture extends BaseTable implements Serializable {
	private static final long serialVersionUID = -1350787093697204874L;
	private final ImageType type;
	private String imageFile = null;

	public Picture() {
		super();
		this.type = ImageType.UNDEFINED;
	}

	public Picture(int pk, ImageType t) {
		super(pk);
		this.type = t;
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
	public Picture(String cover, ImageType t) throws FileNotFoundException {
		super();
		this.imageFile = cover;
		this.generateCheckSum(new File(cover));
		this.type = t;
	}

	public Picture(File cover, ImageType t) throws IOException {
		super();
		this.imageFile = cover.getCanonicalPath();
		this.generateCheckSum(cover);
		this.type = t;
	}

	public String getImagePath() {
		return this.imageFile;
	}

	public final File getImageFile() {
		return new File(this.imageFile);
	}

	public void setImageFile(String imagePath) throws IOException {
		this.imageFile = imagePath;
		this.generateCheckSum(new File(this.imageFile));
	}

	public void setImageFile(File imageFile) throws IOException {
		this.imageFile = imageFile.getCanonicalPath();
		this.generateCheckSum(imageFile);
	}

	public void setImageFile(String f, String checksum) throws IOException {
		this.imageFile = f;
		this.titles[0] = checksum;
	}

	@Override
	public String[] metaData() {
		String tmp[] = { "idCover", "object" };
		return tmp;
	}

	public ImageType getType() {
		return type;
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
	private void generateCheckSum(File f) throws FileNotFoundException {
		this.titles[0] = Hasher.hashStream(new FileInputStream(f));
	}

	@Override
	protected void initInternalFields() {
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
