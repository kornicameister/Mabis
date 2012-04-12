package view.enums;

import java.util.ArrayList;

/**
 * Enum that holds all available image formats
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
