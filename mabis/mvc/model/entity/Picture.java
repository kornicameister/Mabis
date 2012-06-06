/**
 * package mabis.mvc.model.entity in MABIS
 * by kornicameister
 */
package mvc.model.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import mvc.model.BaseTable;
import mvc.model.enums.PictureType;
import mvc.model.enums.TableType;
import settings.GlobalPaths;
import utilities.Hasher;

/**
 * Klasa Picture jest obiektową wersją tabeli bazy danych picture
 * 
 * @author kornicameister
 * 
 */
// TODO update commnents and make them more sql dependable
public class Picture extends BaseTable implements Serializable {
	private static final long serialVersionUID = -1350787093697204874L;
	private PictureType type;
	private String imageFilePath = null;

	/**
	 * @see BaseTable#BaseTable()
	 */
	public Picture() {
		super();
	}

	/**
	 * Tworzy nowy obiekt {@link Picture} bazujac na jej kluczu glownym oraz
	 * typie tego zdjecia
	 * 
	 * @param pk
	 * @param t
	 */
	public Picture(int pk, PictureType t) {
		super(pk);
		this.type = t;
	}

	/**
	 * Ten konstruktor moze wyprodukowac wyjatek {@link FileNotFoundException},
	 * jeśli sciezka do podanego <i>cover</i> bedzie wskazywac na nie istniejacy
	 * fizycznie plik
	 * 
	 * @param cover
	 *            sciezka do pliku zdjecia
	 * @param t
	 *            typ zdjecia
	 * @throws FileNotFoundException
	 */
	public Picture(String cover, PictureType t) throws FileNotFoundException {
		super();
		this.imageFilePath = cover;
		this.generateCheckSum(new File(cover));
		this.type = t;
	}

	/**
	 * Konstruktor dziala tak samo jak
	 * {@link Picture#Picture(String, PictureType)} z ta roznica, ze zamiast
	 * sciezki do pliku podawana jest od razu referencja do pliku wskazujacego
	 * na zdjecie
	 * 
	 * @param cover
	 * @param t
	 * @throws IOException
	 */
	public Picture(File cover, PictureType t) throws IOException {
		super();
		this.imageFilePath = cover.getCanonicalPath();
		this.generateCheckSum(cover);
		this.type = t;
	}

	/**
	 * Tworzy nowy obiekt zdjecia bazujac na przekaznym do tej klasy obiektowi
	 * URL
	 * 
	 * @param url
	 * @param t
	 * @throws IOException
	 */
	public Picture(URL url, PictureType t) throws IOException {
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
		urlConn.addRequestProperty("Referer", "www.google.com");
		urlConn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0");
		urlConn.connect();

		// saving file
		InputStream is = urlConn.getInputStream();

		File output = new File(GlobalPaths.TMP + this.titles[0]);
		this.imageFilePath = output.getAbsolutePath();

		FileOutputStream fos = new FileOutputStream(output);
		byte[] b = new byte[4096];
		int length;
		while ((length = is.read(b)) != -1) {
			fos.write(b, 0, length);
		}
		fos.close();
		is.close();
		this.imageFilePath = output.getAbsolutePath();
	}

	/**
	 * Metoda zapisuje hash z podanego URL
	 * 
	 * @param imageUrl
	 * @throws IOException
	 */
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
		String tmp[] = {"idCover", "object"};
		return tmp;
	}

	public PictureType getType() {
		return type;
	}

	public void setType(PictureType type) {
		this.type = type;
	}

	public String getCheckSum() {
		return this.titles[0];
	}

	/**
	 * Method opens an image, and generates hash checksum basing of image file
	 * binary content <b>Notice</> that this.checkSum will always be 36b lenght
	 * 
	 * @param f
	 *            plik, dla którego zostanie wygenerowana suma kontrolna
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
		str += "[TYPE: " + this.getType() + "]\n";
		str += "[CHCSM: " + this.getCheckSum() + "]\n";
		str += "[PATH: " + this.getImagePath() + "]\n";
		return str;
	}
}
