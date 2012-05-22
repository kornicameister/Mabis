/**
 * package model.entity in MABIS
 * by kornicameister
 */
package model.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import model.BaseTable;
import model.enums.ImageType;
import model.enums.TableType;
import settings.GlobalPaths;
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
	private String imageFilePath = null;

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
		this.imageFilePath = cover;
		this.generateCheckSum(new File(cover));
		this.type = t;
	}

	public Picture(File cover, ImageType t) throws IOException {
		super();
		this.imageFilePath = cover.getCanonicalPath();
		this.generateCheckSum(cover);
		this.type = t;
	}

	public Picture(URL url, ImageType t) throws IOException {
		super();
		saveImageFromURL(url);
		this.type = t;
	}

	/**
	 * Metoda pobiera zdjęcie ze wskazanego url, w domyśle ma działać z linkami
	 * z GoogleBookApi.
	 * 
	 * @param url
	 *            adres zdjęcia
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void saveImageFromURL(URL url) throws IOException,
			FileNotFoundException {
		this.saveHash(url);
		URLConnection urlConn = url.openConnection();
		urlConn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0");
		urlConn.connect();

		// saving file
		InputStream is = urlConn.getInputStream();

		File output = new File(GlobalPaths.TMP + this.titles[0]);
		this.imageFilePath = output.getAbsolutePath();

		FileOutputStream fos = new FileOutputStream(output);
		byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) != -1) {
			fos.write(b, 0, length);
		}
		fos.close();
		is.close();
		this.imageFilePath = output.getAbsolutePath();
	}

	private void saveHash(URL imageUrl) throws IOException {
		URLConnection conn = imageUrl.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0");
		conn.connect();
		try {
			this.titles[0] = Hasher.hashStream(conn.getInputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public String getImagePath() {
		return this.imageFilePath;
	}

	public final File getImageFile() {
		return new File(this.imageFilePath);
	}

	public void setImageFile(String imagePath) throws IOException {
		this.imageFilePath = imagePath;
		this.generateCheckSum(new File(this.imageFilePath));
	}

	public void setImageFile(File imageFile) throws IOException {
		this.imageFilePath = imageFile.getCanonicalPath();
		this.generateCheckSum(imageFile);
	}

	public void setImageFile(String f, String checksum) throws IOException {
		this.imageFilePath = f;
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
		this.imageFilePath = null;
		this.tableType = TableType.PICTURE;
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

	@Override
	public Object[] toColumnIdentifiers() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
			data.add(d);
		}
		data.add("Picture");
		data.add("Hash");
		data.add("Path");
		return data.toArray();
	}

	@Override
	public Object[] toRowData() {
		ArrayList<Object> data = new ArrayList<Object>();
		for (Object d : super.toColumnIdentifiers()) {
			data.add(d);
		}
		data.add(new ImageIcon(this.imageFilePath));
		data.add(this.titles[0]);
		data.add(this.imageFilePath);
		return data.toArray();
	}
}
