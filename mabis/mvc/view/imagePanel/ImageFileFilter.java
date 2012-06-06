/**
 * 
 */
package mvc.view.imagePanel;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import mvc.view.enums.ImageFormats;

/**
 * Klasa, ktorej obiekt, mozna nastepnie dodac do {@link JFileChooser} pozwala
 * na listowanie w widoku {@link JFileChooser} jedynie zdjec
 * 
 * @author kornicameister
 * @see ImageFormats
 */
public class ImageFileFilter extends FileFilter {
	public ImageFileFilter() {
		super();
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		ArrayList<String> availableExt = ImageFormats.getFormats();
		for (String e : availableExt) {
			if (e.equals(ext)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		ArrayList<String> availableExt = ImageFormats.getFormats();
		String str = new String();
		for (String a : availableExt) {
			str += a + ",";
		}
		str = str.substring(0, str.lastIndexOf(","));
		return str;
	}

}
