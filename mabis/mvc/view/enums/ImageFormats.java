package mvc.view.enums;

import java.util.ArrayList;

import mvc.view.imagePanel.ImageFileFilter;
import mvc.view.imagePanel.ImageFilePreview;

/**
 * Enum, gdzie zlokalizowane sa formaty plikow - <b>zdjec</b>, obslugiwanych w
 * {@link ImageFileFilter} oraz {@link ImageFilePreview}
 * 
 * @author kornicameister
 * 
 */
public enum ImageFormats {
	PNG("png"), JPG("jpg"), JPEG("jpeg"), GIF("gif"), TIFF("tiff"), TIF("tif");

	private String format;

	private ImageFormats(String f) {
		this.format = f;
	}

	@Override
	public String toString() {
		return this.format;
	}

	/**
	 * statyczna publiczna metoda, zwraca {@link ArrayList} wszystkich mozliwych
	 * formatow plikow
	 * 
	 * @return formaty plikow jako string
	 */
	static public ArrayList<String> getFormats() {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(PNG.toString());
		tmp.add(JPEG.toString());
		tmp.add(JPG.toString());
		tmp.add(GIF.toString());
		tmp.add(TIF.toString());
		tmp.add(TIFF.toString());
		return tmp;
	}
}
