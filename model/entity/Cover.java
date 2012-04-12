/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;

import model.enums.CoverType;
import model.enums.TableType;

/**
 * This class maps itself to mabis.cover table Table structure: </br> | idCover </br> | image </br> | hash
 * @author kornicameister
 * 
 */
// TODO update commnents and make them more sql dependable
public class Cover extends BaseTable {
	private CoverType type = null;
	private ImageIcon image = null;
	private File imageFile;
	private final static String defaultCover = "src/resources/defaultCover.png";

	/**
	 * Constructs Cover with primary key
	 * 
	 * @param pk
	 */
	public Cover(int pk) {
		super(pk);
	}
	
	public Cover(File cover){
		super();
		this.imageFile = cover;
		this.image = new ImageIcon(cover.getAbsolutePath());
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
	public void generateCheckSum() throws FileNotFoundException {
		File m;
		if (this.image == null) {
			m = new File(this.getClass().getResource(defaultCover).getFile());
		} else {
			m = this.imageFile;
		}
		this.titles[0] = database.Utilities.md5sum(new FileInputStream(m));
	}
	
	@Override
	protected void initInternalFields() {
		this.tableName = TableType.COVER.toString();
		this.type = CoverType.UNDEFINED;
		this.imageFile = null;
	}
	
	public ImageIcon getImage(){
		return this.image;
	}
	
	public void setImage(Image image){
		this.image = new ImageIcon(image);
	}
}
